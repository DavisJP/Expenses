package com.davismiyashiro.expenses.view.addtab;

import com.davismiyashiro.expenses.datatypes.Tab;
import com.davismiyashiro.expenses.model.Repository;

/**
 * Created by Davis on 23/01/2016.
 */
public class AddTabPresenterImpl implements AddTabInterfaces.UserActionsListener{

    private AddTabInterfaces.View mView;
    private Repository mModel;
    private Tab mTab;

    public AddTabPresenterImpl (AddTabInterfaces.View view, Repository model, Tab tab) {
        mView = view;
        mModel = model;
        mTab = tab;
    }

    @Override
    public void addTab(String name) {
        mTab = new Tab(name);
        mModel.saveTab(mTab);

        mView.setTabValid();
    }

    @Override
    public void loadTab() {
        mModel.getTab(mTab.getGroupId(), new Repository.GetTabCallback() {
            @Override
            public void onTabLoaded(Tab tab) {
                mTab = tab;
                mView.setTabName(mTab.getGroupName());
            }
        });
    }

    @Override
    public void updateTab(String name) {
        mTab = new Tab(mTab.getGroupId(), name);
        mModel.updateTab(mTab);

        mView.updatedTab(mTab);
    }

//    private boolean isTabNew () {
//        return mTab == null;
//    }
}
