package com.davismiyashiro.expenses.view.opentab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.davismiyashiro.expenses.R;
import com.davismiyashiro.expenses.datatypes.Expense;
import com.davismiyashiro.expenses.datatypes.Participant;
import com.davismiyashiro.expenses.datatypes.ReceiptItem;
import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.BaseCompatActivity;
import com.davismiyashiro.expenses.view.addexpense.ExpenseActivity;
import com.davismiyashiro.expenses.view.addtab.AddTabActivity;
import com.davismiyashiro.expenses.view.opentab.ExpenseFragment.OnExpenseFragmentInteractionListener;
import com.davismiyashiro.expenses.view.opentab.ParticipantFragment.OnParticipantListFragmentInteractionListener;
import com.davismiyashiro.expenses.view.addparticipant.ParticipantActivity;
import com.davismiyashiro.expenses.view.sendreceipt.ReceiptActivity;

import timber.log.Timber;

import static com.davismiyashiro.expenses.view.opentab.ReceiptFragment.*;


public class OpenTabActivity extends BaseCompatActivity implements OnParticipantListFragmentInteractionListener, OnExpenseFragmentInteractionListener, OnReceiptFragmentInteractionListener{

    public static final String TAB_REQUEST = "com.davismiyashiro.expenses.view.opentab.OpenTabActivity";
    public static final int UPDATE_RESULT = 1;
    private ParticipantInterfaces.UserActionsListener mPresenter;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    ParticipantFragment partFragment;
    ExpenseFragment expFragment;
    ReceiptFragment receiptFragment;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabPartFrag;
    private Tab mTab;

    public static Intent newInstance (Context context, Tab tab) {
        Intent intent = new Intent(context, OpenTabActivity.class);
        intent.putExtra(TAB_REQUEST, tab);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_open_tab);
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Timber.d("onPageScrolled:" + position);
                //fabPartFrag.hide();
            }

            @Override
            public void onPageSelected(int position) {
                Timber.d("onPageSelected:".concat(String.valueOf(position)));
                switch (position) {
                    case 0:
                        fabPartFrag.setImageResource(R.drawable.ic_person_add_white_24dp);
                        //fabPartFrag.show();
                        break;
                    case 1:
                        fabPartFrag.setImageResource(R.drawable.ic_note_add_white_24dp);
                        //fabPartFrag.show();
                        break;
                    case 2:
                        fabPartFrag.setImageResource(R.drawable.ic_share_white_24dp);
                        //fabPartFrag.show();
                        break;

                    default:
                        fabPartFrag.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //tabLayout.addTab(tabLayout.newTab().setText("Works?"));

        fabPartFrag = (FloatingActionButton) findViewById(R.id.fab_part_frag);
        fabPartFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pagerPosition = mViewPager.getCurrentItem();
                Intent addData = new Intent();
                switch (pagerPosition) {
                    case 0:
                        addData = ParticipantActivity.newInstance(getBaseContext(), mTab, null);
                        break;

                    case 1:
                        addData = ExpenseActivity.newIntent(getBaseContext(), mTab, null);
                        fabPartFrag.hide();
                        break;

                    case 2:
                        addData = ReceiptActivity.newInstance(getBaseContext(), mTab);
                        break;

                    default:
                        break;
                }
                startActivity(addData);
            }
        });

        mTab = getIntent().getParcelableExtra(TAB_REQUEST);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        ActionBar ab = getSupportActionBar();
        ab.setTitle(mTab.getGroupName());

        fabPartFrag.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_open_tab, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                startActivityForResult(AddTabActivity.newIntent(this, mTab), UPDATE_RESULT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult");
        // Check which request we're responding to
        if (requestCode == UPDATE_RESULT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Do something with the contact here (bigger example below)
                mTab = data.getParcelableExtra(AddTabActivity.EDIT_TAB);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
        }
    }

    @Override
    public void onParticipantListFragmentInteraction(Participant item) {
        startActivity(ParticipantActivity.newInstance(this, mTab, item));
    }

    @Override
    public void onParticipantListFragmentLongClick(Participant item) {
        mPresenter.removeParticipant(item);
    }

    @Override
    public void onExpenseFragmentInteraction(Expense expense) {
        startActivity(ExpenseActivity.newIntent(this, mTab, expense));
    }

    @Override
    public void onExpenseFragmentLongClick(Expense expense) {
//        mPresenter.removeExpense(expense); //TODO: Fix this!

        //Workaround to update receipt fragment
        mSectionsPagerAdapter.notifyChangeInPosition(1);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceiptFragmentInteraction(ReceiptItem receiptItem) {
        //ActivityHelper.showToast(this, receiptItem.getParticipantName() + " Edit Receipt Item?");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "PARTICIPANTS", "EXPENSES", "RECEIPT" };
        private Context mContext;
//        TabRepository mRepository = Injection.provideTabsRepository(getApplicationContext());
        private long baseId = 0;

        public SectionsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment)super.instantiateItem(container, position);

            switch (position) {
                case 0:
                    partFragment = (ParticipantFragment) createdFragment;
                    break;
                case 1:
                    expFragment = (ExpenseFragment) createdFragment;
                    break;
                default:
                    receiptFragment = (ReceiptFragment) createdFragment;
            }

            return createdFragment;
        }

        // getItem is called to instantiate the fragment for the given page.
        @Override
        public Fragment getItem(int position) {
            Timber.d("getItem.position: ".concat(String.valueOf(position)));
            Fragment fragment = new Fragment();


            if (position == 0){
                partFragment = ParticipantFragment.newInstance(mTab);
                return partFragment;
            }

            if (position == 1) {
                expFragment = ExpenseFragment.newInstance(mTab);
                return expFragment;
            }

            if (position == 2) {
                receiptFragment = ReceiptFragment.newInstance(mTab);
                return receiptFragment;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

        //this is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }
    }
}
