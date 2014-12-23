package com.bruce.funny;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.bruce.funny.view.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    
    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    
    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private SparseArray<Fragment> mFragments = new SparseArray<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        PagerSlidingTabStrip mPagerSlidingTabStrip = (PagerSlidingTabStrip) getLayoutInflater().inflate(R.layout.view_page_sliding_tab_strip, null);
        // Configure several action bar elements that will be toggled by display options.
        restoreActionBar();
        getActionBar().setCustomView(mPagerSlidingTabStrip, new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT , Gravity.RIGHT));

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        Fragment detachFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(detachFragment != null) beginTransaction.detach(detachFragment);
        Fragment fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = TJPostContainerFragment.newInstance(position);
            beginTransaction.add(R.id.container, fragment).commitAllowingStateLoss();
            mFragments.put(position, fragment);
        } else {
            beginTransaction.attach(fragment).commitAllowingStateLoss();
        }
    }
    
    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
            mTitle = getString(R.string.navigation_picture);
            break;
            case 1:
            mTitle = getString(R.string.navigation_text);
            break;
        }
    }
    
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(mTitle);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
