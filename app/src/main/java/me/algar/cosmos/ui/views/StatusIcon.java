package me.algar.cosmos.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.algar.cosmos.R;

/**
 * Created by David on 5/1/16.
 */
public class StatusIcon extends FrameLayout {
    public static final int STATUS_FAILURE = 0;
    public static final int STATUS_SUCCESS = 1;
    @Bind(R.id.status_icon_failure)
    protected View mFailureIcon;
    @Bind(R.id.status_icon_success)
    protected View mSuccessIcon;

    public StatusIcon(Context context) {
        super(context);
        init();
    }

    public StatusIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StatusIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.view_status_icon, this, true);
        ButterKnife.bind(this, root);
    }

    public void setStatus(int status){

        if (status == STATUS_SUCCESS) {
            mSuccessIcon.setVisibility(VISIBLE);
            mFailureIcon.setVisibility(GONE);
            this.setBackground(
                    ResourcesCompat.getDrawable(
                            getContext().getResources(),
                            R.drawable.green_circle,
                            null));
        } else {
            mSuccessIcon.setVisibility(GONE);
            mFailureIcon.setVisibility(VISIBLE);
            this.setBackground(
                    ResourcesCompat.getDrawable(
                            getContext().getResources(),
                            R.drawable.red_circle,
                            null));
        }
    }
}
