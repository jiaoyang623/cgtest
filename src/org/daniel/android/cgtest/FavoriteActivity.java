package org.daniel.android.cgtest;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * @author jiaoyang<br>
 *         email: jiaoyang623@qq.com
 * @version 1.0
 * @date May 29 2015 7:33 PM
 */
public class FavoriteActivity extends Activity {
    private ListView mListView;
    private FavoriteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = new ListView(getApplicationContext());
        mAdapter = new FavoriteAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mAdapter);
        mListView.setOnItemLongClickListener(mAdapter);
        mListView.setDivider(new ColorDrawable(0x55ffffff));
        mListView.setDividerHeight(1);
        setContentView(mListView);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FavoriteAdapter extends BaseAdapter implements
            AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        @Override
        public int getCount() {
            return 20;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = parent.inflate(getApplicationContext(), R.layout.item_favorite, null);
                convertView.setTag(holder);
                holder.image = (ImageView) convertView.findViewById(R.id.imageContent);
                holder.content = (TextView) convertView.findViewById(R.id.textContent);
                holder.description = (TextView) convertView.findViewById(R.id.textDesc);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.content.setText("Ubuntu is an open source software platform that runs everywhere from the smartphone, the tablet and the PC to the server and the cloud.");
            holder.description.setText("0:57 · 54.3K views");

            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
        }
    }

    private class ViewHolder {
        ImageView image;
        TextView content;
        TextView description;
    }
}
