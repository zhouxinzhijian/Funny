package com.bruce.funny.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.funny.R;
import com.bruce.funny.view.NavigationGridView;
import com.bruce.funny.view.slide_expandable_listview.SlideExpandableListView;

import java.util.HashSet;
import java.util.Set;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends TJFragment {
    
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    
    /**
     * Per the design guidelines, you should show the drawer on launch until the
     * user manually expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private static final String PREF_CHECKED_GRIDVIEW_POSITION = "pref_checked_gridview_position";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    
    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;
    
    private DrawerLayout mDrawerLayout;
    private SlideExpandableListView mDrawerListView;
    private View mFragmentContainerView;
    
    private int mCurrentSelectedPosition = -1;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    
    public NavigationDrawerFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Read in the flag indicating whether or not the user has demonstrated
        // awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        
        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (SlideExpandableListView) rootView.findViewById(R.id.list);
        mDrawerListView.setAdapter(new NavigationListAdapter(), R.id.classify_filter, R.id.filter_gridview);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return rootView;
    }

    private class NavigationListAdapter extends BaseAdapter{
        int[] icon = new int[]{
                R.drawable.ic_drawer_picture_normal,
                R.drawable.ic_drawer_text_normal,
                R.drawable.ic_drawer_classify_normal,
                R.drawable.ic_drawer_collect_normal,
                R.drawable.ic_drawer_following_normal,
                R.drawable.ic_drawer_feedback_normal};
        int[] text = new int[]{
                R.string.navigation_picture,
                R.string.navigation_text,
                R.string.navigation_classify,
                R.string.navigation_collect,
                R.string.navigation_following,
                R.string.navigation_feedback,
        };
        @Override
        public int getCount() {
            return icon.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 2 ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView navigationTextView;
            if(convertView == null){
                View rootView = View.inflate(mActivity, R.layout.fragment_navigation_drawer_list_item, null);
                navigationTextView = (TextView)rootView.findViewById(R.id.navigation_type);
                navigationTextView.setText(text[position]);
                navigationTextView.setCompoundDrawablesWithIntrinsicBounds(icon[position], 0, 0, 0);
                navigationTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectItem(position);
                    }
                });
                if(getItemViewType(position) == 1){
                    rootView.findViewById(R.id.classify_filter).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.div_line).setVisibility(View.VISIBLE);
                    ((ViewStub)rootView.findViewById(R.id.filter_gridview_layout)).inflate();
                    NavigationGridView filterGridView = (NavigationGridView)rootView.findViewById(R.id.filter_gridview);
                    filterGridView.setAdapter(new FilterAdapterView(filterGridView));
                    Set<String> stringSet = PreferenceManager.getDefaultSharedPreferences(mActivity).getStringSet(PREF_CHECKED_GRIDVIEW_POSITION, null);
                    if(stringSet != null){
                        for(String str : stringSet){
                            filterGridView.setItemChecked(Integer.valueOf(str), true);
                        }
                    }
                }
                rootView.setTag(navigationTextView);
                convertView = rootView;
            }else{
                navigationTextView = (TextView)convertView.getTag();
            }
            if (mDrawerListView.getCheckedItemPosition() == position ){
                navigationTextView.setBackgroundResource(R.color.action_bar_indicator_color);
            }else{
                navigationTextView.setBackgroundResource(android.R.color.transparent);
            }
            return convertView;
        }
        private class FilterAdapterView extends BaseAdapter implements View.OnClickListener{
            int[] icon = new int[]{
                    R.drawable.ic_drawer_picture_normal,
                    R.drawable.ic_drawer_text_normal,
                    R.drawable.ic_drawer_classify_normal,
                    R.drawable.ic_drawer_collect_normal,
                    R.drawable.ic_drawer_following_normal,
                    R.drawable.ic_drawer_feedback_normal};
            int[] text = new int[]{
                    R.string.navigation_picture,
                    R.string.navigation_text,
                    R.string.navigation_classify,
                    R.string.navigation_collect,
                    R.string.navigation_following,
                    R.string.navigation_feedback,
            };
            private GridView mGridView;
            public FilterAdapterView(GridView mGridView){
                this.mGridView = mGridView;
            }
            @Override
            public int getCount() {
                return icon.length;
            }
            @Override
            public Object getItem(int position) {
                return position;
            }
            @Override
            public long getItemId(int position) {
                return position;
            }
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View rootView = View.inflate(mActivity, R.layout.fragment_navigation_filter_grid_item, null);
                rootView.setOnClickListener(this);
                rootView.setTag(text[position]);
                CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.check_id);
                checkBox.setChecked(mGridView.getCheckedItemPositions().get(position));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mGridView.setItemChecked(position, isChecked);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                        Set<String> stringSet = preferences.getStringSet(PREF_CHECKED_GRIDVIEW_POSITION, new HashSet<String>());
                        if(isChecked){
                            stringSet.add(String.valueOf(position));
                        }else{
                            stringSet.remove(String.valueOf(position));
                        }
                        preferences.edit().putStringSet(PREF_CHECKED_GRIDVIEW_POSITION, stringSet).apply();
                    }
                });
                TextView filterText = (TextView) rootView.findViewById(R.id.filter_text);
                filterText.setText(text[position]);
                filterText.setCompoundDrawablesWithIntrinsicBounds(0, icon[position], 0, 0);
                return rootView;
            }

            @Override
            public void onClick(View v){
                switch ((Integer)v.getTag()){
                    //TODO:
                    default:
                        Toast.makeText(mActivity, (Integer)v.getTag(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of
        // actions in the action bar.
        setHasOptionsMenu(true);
    }
    
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }
    
    /**
     * Users of this fragment must call this method to set up the navigation
     * drawer interactions.
     * 
     * @param fragmentId The android:id of this fragment in its activity's
     *            layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = mActivity.findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        
        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(mActivity, /*
                                                                  * host
                                                                  * Activity
                                                                  */
        mDrawerLayout, /* DrawerLayout object */
        R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
        R.string.navigation_drawer_open, /*
                                          * "open drawer" description for
                                          * accessibility
                                          */
        R.string.navigation_drawer_close /*
                                          * "close drawer" description for
                                          * accessibility
                                          */
        )
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                
                mActivity.invalidateOptionsMenu(); // calls
                                                       // onPrepareOptionsMenu()
            }
            
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to
                    // prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                
                mActivity.invalidateOptionsMenu(); // calls
                                                       // onPrepareOptionsMenu()
            }
        };
        
        // If the user hasn't 'learned' about the drawer, open it to introduce
        // them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
        
        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable()
        {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    
    private void selectItem(int position) {
        if(position == -1){ //For the first time
            position = 0;
        }
        if (mDrawerLayout != null && position <= 2) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if(mCurrentSelectedPosition != position && position <= 2){
            if (mDrawerListView != null) {
                mDrawerListView.setItemChecked(position, true);
            }
            if (mCallbacks != null) {
                mCallbacks.onNavigationDrawerItemSelected(position);
            }
            mCurrentSelectedPosition = position;
        }else{
            switch (position) {
                case 3: //collect
                    Toast.makeText(mActivity, R.string.navigation_collect, Toast.LENGTH_SHORT).show();
                    break;
                case 4: //following
                    Toast.makeText(mActivity, R.string.navigation_following, Toast.LENGTH_SHORT).show();
                    break;
                case 5: //feedback
                    startActivity(new Intent(mActivity, TJFeedBackActivity.class));
                    break;
            }
            if (mDrawerListView != null) {
                mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
            }
        }
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar.
        // See also
        // showGlobalContextActionBar, which controls the top-left area of the
        // action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(mActivity, "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Per the navigation drawer design guidelines, updates the action bar to
     * show the global app 'context', rather than just what's in the current
     * screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }
    
    private ActionBar getActionBar() {
        return mActivity.getActionBar();
    }
    
    /**
     * Callbacks interface that all activities using this fragment must
     * implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
