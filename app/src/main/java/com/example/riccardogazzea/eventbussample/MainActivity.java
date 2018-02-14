package com.example.riccardogazzea.eventbussample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baseandroid.events.EventDispatcher;
import com.baseandroid.events.rx.RxEventProcessor;
import com.crashlytics.android.Crashlytics;
import com.example.riccardogazzea.eventbussample.events.UiRecyclerStateIdleEvent;
import com.example.riccardogazzea.eventbussample.events.UiRecyclerStateNotIdleEvent;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    MediaModelAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());
        // l'EventProcessor dovrebbe idealmente essere inizializzato a livello di Application
        EventDispatcher.useEventProcessor(RxEventProcessor.newInstance());

        // init adapter
        mAdapter = new MediaModelAdapter(MockUtils.VIDEOS);

        // init recycler
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyRecyclerViewIdleState();
                } else {
                    notifyRecyclerViewNotIdleState();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        // simulate UiRecyclerStateIdleEvent to play first video of the list
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                notifyRecyclerViewIdleState();
            }
        });
    }

    private void notifyRecyclerViewIdleState() {
        final int firstCompletelyVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (RecyclerView.NO_POSITION != firstCompletelyVisibleItemPosition) {
            final View view = mLayoutManager.findViewByPosition(firstCompletelyVisibleItemPosition);
            EventDispatcher.post(new UiRecyclerStateIdleEvent(view.getTag()));
        }
    }

    private void notifyRecyclerViewNotIdleState() {
        EventDispatcher.post(new UiRecyclerStateNotIdleEvent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter = null;
    }
}
