package org.daniel.android.cgtest.widgets;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import org.daniel.android.cgtest.R;

/**
 * @author jiaoyang<br>
 *         email: jiaoyang623@qq.com
 * @version 1.0
 * @date May 28 2015 9:12 PM
 */
public class VideoItemView extends RelativeLayout implements SweepStackLayout.Progressable, View.OnClickListener, View.OnLongClickListener {
    private ImageView mContentImage;
    private ImageView mPositiveImage;
    private ImageView mNegtiveImage;
    private TextView mContentText;
    private TextView mDescText;
    private Button mShareButton;

    public VideoItemView(Context context) {
        super(context);
        init(context);
    }

    public VideoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_video, this);
        setBackgroundColor(0xff000000);
        mContentImage = (ImageView) findViewById(R.id.imageContent);
        mPositiveImage = (ImageView) findViewById(R.id.imagePositive);
        mNegtiveImage = (ImageView) findViewById(R.id.imageNegtive);
        mContentText = (TextView) findViewById(R.id.textContent);
        mDescText = (TextView) findViewById(R.id.textDesc);
        mShareButton = (Button) findViewById(R.id.buttonShare);

        mPositiveImage.setAlpha(0f);
        mNegtiveImage.setAlpha(0f);

        mContentImage.setOnClickListener(this);
        mShareButton.setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public void changeProgress(float progress) {
        if (progress > 0) {
            mPositiveImage.setAlpha(Math.min(1, progress));
            mNegtiveImage.setAlpha(0f);
        } else if (progress < 0) {
            mPositiveImage.setAlpha(0f);
            mNegtiveImage.setAlpha(Math.min(1, -progress));
        } else {
            animTransparent(mPositiveImage);
            animTransparent(mNegtiveImage);
        }
    }

    private void animTransparent(View v) {
        if (v.getAlpha() != 0) {
            ObjectAnimator.ofFloat(v, "alpha", 0).setDuration(300).start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageContent:
                Toast.makeText(getContext(), "Play", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonShare:
                Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(getContext(), "Long Click", Toast.LENGTH_SHORT).show();
        return true;
    }
}
