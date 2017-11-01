package com.davismiyashiro.expenses.view.managetabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davismiyashiro.expenses.Injection;
import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.view.addtab.AddTabActivity;
import com.davismiyashiro.expenses.model.BaseCompatActivity;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.view.opentab.OpenTabActivity;

import java.util.ArrayList;
import java.util.List;

public class ChooseTabsActivity extends BaseCompatActivity implements ChooseTabsInterfaces.View {

    private FloatingActionButton fabAddTab;
    private ChooseTabsInterfaces.UserActionsListener mPresenter;
    private RecyclerView mTabsRecyclerView;
    private TabsAdapter mTabsAdapter;

    private LinearLayout mTabsView;
    private View mNoTabsView;
    private ImageView mNoTabsIcon;
    private TextView mNoTabsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.tabs);
        super.onCreate(savedInstanceState);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mTabsRecyclerView = (RecyclerView) findViewById(R.id.tabs_recycler_view);
        mTabsRecyclerView.setLayoutManager(new
                GridLayoutManager(this, 2));
        mTabsAdapter = new TabsAdapter(new ArrayList<Tab>(), this);
        mTabsRecyclerView.setAdapter(mTabsAdapter);
        mTabsView = (LinearLayout) findViewById(R.id.tabs_root);

        mNoTabsView = findViewById(R.id.tabs_root_no_data);
        mNoTabsIcon = (ImageView) findViewById(R.id.tabs_no_data_icon);
        mNoTabsText = (TextView) findViewById(R.id.tabs_no_data_text_view);

        fabAddTab = (FloatingActionButton) findViewById(R.id.fab_manage_tabs);
        fabAddTab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.addTab();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //finish();
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter = new ChooseTabsPresenterImpl(this, Injection.provideTabsRepository(getApplicationContext()));

        mPresenter.loadTabs(false);
    }

    @Override
    public void showAddTab() {
        //Intent intent = new Intent(this, AddTabActivity.class);
        startActivity(AddTabActivity.newIntent(this, null));
    }

    @Override
    public void setProgressIndicator(boolean active) {
    }

    @Override
    public void showTabs(List<Tab> tabs) {
        mTabsAdapter.replaceData(tabs);

        mTabsView.setVisibility(View.VISIBLE);
        mNoTabsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoTabs() {
        loadShowNoTabsView(getString(R.string.tabs_no_data_text), R.drawable.ic_launcher);
    }

    private void loadShowNoTabsView (String message, int icon) {
        mTabsView.setVisibility(View.GONE);
        mNoTabsView.setVisibility(View.VISIBLE);

        mNoTabsText.setText(message);
        mNoTabsIcon.setImageDrawable(ContextCompat.getDrawable(this, icon));
    }

    @Override
    public void showTabDetailUi(Tab tab) {
        startActivity(OpenTabActivity.newInstance(this, tab));
    }

    /**
     * Receives tabs and bind them to the ViewHolder
     */
    private class TabsAdapter extends RecyclerView.Adapter<TabsAdapter.TabRowHolder> {

        private List<Tab> mTabs;
        private Context mContext;

        public TabsAdapter(List<Tab> tabs, Context context) {
            setList (tabs);
            mContext = context;
        }

        @Override
        public TabRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater =
                    LayoutInflater.from(getApplicationContext());
            View view = layoutInflater
                    .inflate(R.layout.list_item_tab, parent,
                            false);

            return new TabRowHolder(view);
        }

        @Override
        public void onBindViewHolder(TabRowHolder rowHolder, int position) {
            Tab tab = mTabs.get(position);
            //rowHolder.mTabTitleTextView.setText(tab.getGroupName());
            rowHolder.bindTab(tab);
        }

        public void replaceData(List<Tab> tabs) {
            setList(tabs);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mTabs.size();
        }

        private void setList(List<Tab> tabs) {
            mTabs = tabs;
        }

        public Tab getItem(int position) {
            return mTabs.get(position);
        }

        public void removeItemAtPosition(int position) {
            mTabs.remove(position);
            notifyItemRemoved(position);
        }

        public class TabRowHolder extends RecyclerView.ViewHolder {
            public TextView mTabTitleTextView;

            public TabRowHolder(View itemView) {
                super(itemView);
                mTabTitleTextView = (TextView) itemView.findViewById(R.id.list_item_tab_title);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Tab tab = getItem(position);

                        mPresenter.openTab(tab.getGroupId());
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setTitle("Delete Tab");
                        alertDialogBuilder.setMessage("Are you sure you want to delete this Tab ?");

                        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                int position = getAdapterPosition();

                                mPresenter.removeTab(getItem(position).getGroupId());
                                removeItemAtPosition(position);
                                arg0.dismiss();
                            }
                        });

                        alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

//                        alertDialogBuilder.create().show();
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        return true;
                    }
                });
            }

            public void bindTab (Tab tab) {
                mTabTitleTextView.setText(tab.getGroupName());
            }
        }
    }
}
