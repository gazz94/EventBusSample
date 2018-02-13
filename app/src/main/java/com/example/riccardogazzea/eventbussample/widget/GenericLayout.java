package com.example.riccardogazzea.eventbussample.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baseandroid.events.EventDispatcher;
import com.example.riccardogazzea.eventbussample.R;
import com.example.riccardogazzea.eventbussample.model.GenericMediaModel;

/**
 * Created on 13/02/18.
 *
 * @author Umberto Marini
 */
public class GenericLayout extends RelativeLayout {

    private GenericMediaModel mMediaModel;

    private TextView mTextTextView;

    public GenericLayout(Context context) {
        super(context);
        init(context);
    }

    public GenericLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GenericLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GenericLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.listitem_genericlayout, this, true);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.listitem_height)));
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));

        mTextTextView = findViewById(R.id.genericlayout_test_text);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventDispatcher.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventDispatcher.unregister(this);
    }

    public void setGenericMediaModel(GenericMediaModel mediaModel) {
        mMediaModel = mediaModel;
    }
}
