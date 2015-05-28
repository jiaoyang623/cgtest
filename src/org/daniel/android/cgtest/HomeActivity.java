package org.daniel.android.cgtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import org.daniel.android.cgtest.widgets.SweepStackLayout;

public class HomeActivity extends Activity implements SweepStackLayout.Callback {
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
        ImageView imageView = new ImageView(getApplication());
        imageView.setImageResource(R.drawable.mario);

        return imageView;
    }

    @Override
    public void onPop(View v, int direction) {

    }

    @Override
    public void onClick() {

    }
}
