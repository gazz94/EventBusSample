package com.example.riccardogazzea.eventbussample;

import android.os.Bundle;

import com.baseandroid.events.rx.annotations.RxSubscribe;
import com.example.riccardogazzea.eventbussample.model.MediaModel;
import com.example.riccardogazzea.eventbussample.widget.InputTextMediaLayout;

public class NewContentActivity extends BaseActivity {

    public static final String EXTRA_MEDIA_MODEL = "NewContentActivity.Extra.MediaModel";

    InputTextMediaLayout mInputTextMediaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcontent);

        // init VideoInputTextLayout and mock media model
        mInputTextMediaLayout = findViewById(R.id.newcontent_videoinputtextlayout);
        mInputTextMediaLayout.setMediaModel((MediaModel) getIntent().getParcelableExtra(EXTRA_MEDIA_MODEL));
    }

    @RxSubscribe
    public void onConsumeEvent(InputTextMediaLayout.OnDoneClickEvent event) {
        // TODO implement some stuff
        finish();
    }
}
