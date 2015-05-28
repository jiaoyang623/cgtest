package org.daniel.android.cgtest.utils;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * TextUtils
 * Created by daniel on 9/24/14.
 */
public class TU {
    /**
     * 打印警告的log，jy
     */
    public static void e(Object... objs) {
        Log.e("jy", str(objs));
    }

    /**
     * 打印log，jy
     */
    public static void j(Object... objs) {
        Log.i("jy", str(objs));
    }

    public static String str(Object... objs) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objs) {
            sb.append(obj);
            sb.append(", ");
        }
        return sb.toString();
    }

    public static TextView setText(View containerView, int resId, String text) {
        TextView textView = (TextView) containerView.findViewById(resId);
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);

        return textView;
    }

    public static TextView setText(View containerView, int resId, String text, int color) {
        TextView textView = (TextView) containerView.findViewById(resId);
        textView.setText(text);
        textView.setTextColor(color);
        textView.setVisibility(View.VISIBLE);

        return textView;
    }

    public static boolean isEmpty(String content) {
        if (content == null || content.length() == 0) {
            return true;
        } else {
            int len = content.length();
            for (int i = 0; i < len; i++) {
                if (!Character.isWhitespace(content.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }
}