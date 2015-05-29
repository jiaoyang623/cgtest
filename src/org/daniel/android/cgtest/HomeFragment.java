package org.daniel.android.cgtest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.daniel.android.cgtest.widgets.SweepStackLayout;
import org.daniel.android.cgtest.widgets.VideoItemView;

/**
 * @author jiaoyang<br>
 *         email: jiaoyang623@qq.com
 * @version 1.0
 * @date May 29 2015 9:20 AM
 */
public class HomeFragment extends Fragment implements
        SweepStackLayout.Callback, View.OnClickListener {
    private VideoItemView mViewHolder = null;
    private SweepStackLayout mSweepStack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mSweepStack = (SweepStackLayout) rootView.findViewById(R.id.sweep);
        mSweepStack.setCallback(this);
        rootView.findViewById(R.id.buttonNegtive).setOnClickListener(this);
        rootView.findViewById(R.id.buttonPositive).setOnClickListener(this);

        return rootView;
    }


    @Override
    public View getNext() {
        VideoItemView v = mViewHolder;
        mViewHolder = null;

        if (v == null) {
            v = new VideoItemView(getActivity());
        }

        //加载数据

        return v;
    }

    @Override
    public void onPop(View v, int direction) {
        mViewHolder = (VideoItemView) v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNegtive:
                mSweepStack.sweep(-1);
                break;
            case R.id.buttonPositive:
                mSweepStack.sweep(1);
                break;
        }
    }
}
