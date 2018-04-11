package com.msg.android.lib.core.intf;

import android.view.View;
import android.widget.TextView;

/**
 * Created by msg on 16/11/4.
 */
public interface ITitleViewProxy {

    TextView getTitleTextView();

    View getTitleView();

    View getLeftButtonUseSpaceView();

    View getRightButtonUseSpaceView();

    View getLeftButton();

    View getRightButton();

    View getTitleShowView();

}
