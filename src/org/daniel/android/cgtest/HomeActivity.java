package org.daniel.android.cgtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import org.daniel.android.cgtest.widgets.SweepStackLayout;

public class HomeActivity extends Activity {
    private SweepStackLayout mSweepStack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSweepStack = (SweepStackLayout) findViewById(R.id.sweep);

        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(getApplication());
            imageView.setImageResource(R.drawable.mario);
            mSweepStack.addView(imageView);
        }
    }
}
