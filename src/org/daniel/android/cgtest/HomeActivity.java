package org.daniel.android.cgtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import org.daniel.android.cgtest.widgets.SweepStackLayout;
import org.daniel.android.cgtest.widgets.VideoItemView;

public class HomeActivity extends Activity implements SweepStackLayout.Callback {
    private VideoItemView mViewHolder = null;
    private SweepStackLayout mSweepStack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSweepStack = (SweepStackLayout) findViewById(R.id.sweep);
        mSweepStack.setCallback(this);
    }

    @Override
    public View getNext() {
        VideoItemView v = mViewHolder;
        mViewHolder = null;

        if (v == null) {
            v = new VideoItemView(getApplicationContext());
        }

        //加载数据

        return v;
    }

    @Override
    public void onPop(View v, int direction) {
        mViewHolder = (VideoItemView) v;
    }

    @Override
    public void onClick() {

    }
}
