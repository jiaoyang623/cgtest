package org.daniel.android.cgtest.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import org.daniel.android.cgtest.R;

/**
 * @author jiaoyang<br>
 *         email: jiaoyang623@qq.com
 * @version 1.0
 * @date May 28 2015 9:12 PM
 */
public class VideoItemView extends RelativeLayout implements SweepStackLayout.Progressable {
    public VideoItemView(Context context) {
        super(context);
    }

    public VideoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_video, this);

    }

    @Override
    public void changeProgress(float progress) {

    }
}
