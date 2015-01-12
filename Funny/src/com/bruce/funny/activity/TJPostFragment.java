package com.bruce.funny.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bruce.funny.R;
import com.bruce.funny.util.DensityUtil;
import com.bruce.funny.widget.XListView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class TJPostFragment extends TJFragment {
    private List<String> mData;
    private MyAdapter adapter;
    private XListView mXlistView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_list, container, false);
        final PtrFrameLayout frame = (PtrFrameLayout) rootView.findViewById(R.id.store_house_ptr_frame);

        // header
        final StoreHouseHeader header = new StoreHouseHeader(mActivity);
        header.setPadding(0, DensityUtil.dip2px(mActivity, 15), 0, 0);

        header.initWithString(getString(R.string.app_name));

        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        frame.postDelayed(new Runnable() {
            @Override
            public void run() {
                frame.autoRefresh(false);
            }
        }, 100);

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                Log.i("tangjian", "onRefreshBegin");
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, 5000);
            }
        });

        mXlistView = (XListView)rootView.findViewById(R.id.listview);
        mXlistView.setPullLoadEnable(true);
        adapter = new MyAdapter();
        mXlistView.setAdapter(adapter);
        mXlistView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Log.i("tangjian", "onRefresh");
                new MyAsyncTask().execute(0);
            }

            @Override
            public void onLoadMore() {
                Log.i("tangjian", "onLoadMore");
                new MyAsyncTask().execute(1);
            }
        });
        return rootView;
    }
    public class MyAsyncTask extends AsyncTask<Integer, Void, Void>{
        @Override
        protected Void doInBackground(Integer[] params) {
            if(params[0] == 0){
                SystemClock.sleep(3000);
                mData.add(0, "onRefresh");
            }else{
                SystemClock.sleep(3000);
                mData.add("onLoadMore");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void o) {
            adapter.notifyDataSetChanged();
            mXlistView.stopRefresh();
            mXlistView.stopLoadMore();
        }
    }

    private class MyAdapter extends BaseAdapter {
        public MyAdapter(){
            super();
            mData = new ArrayList<String>();
            mData.add("a");
            mData.add("b");
            mData.add("c");
            mData.add("c");
            mData.add("c");
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = new TextView(mActivity);
            textView.setPadding(0, 35, 0, 35);
            textView.setTextColor(getResources().getColor(android.R.color.white));
            textView.setText(mData.get(position));
            return textView;
        }

    }
}
