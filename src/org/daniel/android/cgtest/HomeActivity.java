package org.daniel.android.cgtest;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class HomeActivity extends Activity implements ActionBar.OnNavigationListener {
    private HomeAdapter mSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeFragment())
                    .commit();
        }
        mSpinnerAdapter = new HomeAdapter();
        mSpinnerAdapter.setData(Arrays.asList(new String[]{"alpha", "bravo", "chalie", "delta"}));
        ActionBar actionbar = getActionBar();
        actionbar.setTitle("");
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionbar.setListNavigationCallbacks(mSpinnerAdapter, this);
        actionbar.setSelectedNavigationItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return true;
    }

    private class HomeAdapter extends BaseAdapter {
        List<String> contentList = new ArrayList<String>();

        public void setData(Collection<String> data) {
            contentList.clear();

            if (data != null) {
                contentList.addAll(data);
            }
        }

        @Override
        public int getCount() {
            return contentList.size();
        }

        @Override
        public String getItem(int position) {
            return contentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return contentList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) convertView;
            if (tv == null) {
                tv = new TextView(getApplication());
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tv.setPadding(20, 20, 20, 20);
            }

            tv.setText(getItem(position));

            return tv;
        }
    }
}
