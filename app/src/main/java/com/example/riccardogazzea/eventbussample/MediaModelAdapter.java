package com.example.riccardogazzea.eventbussample;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.riccardogazzea.eventbussample.model.MediaModel;
import com.example.riccardogazzea.eventbussample.model.VideoMediaModel;
import com.example.riccardogazzea.eventbussample.widget.GenericLayout;
import com.example.riccardogazzea.eventbussample.widget.VideoLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by riccardogazzea on 06/02/2018.
 */

public class MediaModelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_TYPE_GENERIC, VIEW_TYPE_VIDEO})
    private @interface ViewType {
    }

    private static final int VIEW_TYPE_GENERIC = 0;
    private static final int VIEW_TYPE_VIDEO = 1;

    private List<MediaModel> mData;

    /**
     * Default constructor
     */
    public MediaModelAdapter(List<MediaModel> data) {
        mData = data;
    }

    public MediaModel getItem(int position) {
        return mData != null ? mData.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @ViewType
    @Override
    public int getItemViewType(int position) {
        @ViewType int viewType = VIEW_TYPE_GENERIC;

        MediaModel mm = getItem(position);
        if (mm != null) {
            if (mm.isVideo()) {
                viewType = VIEW_TYPE_VIDEO;
            }
        }

        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (VIEW_TYPE_VIDEO == viewType) {
            return new VideoViewHolder(new VideoLayout(parent.getContext()));
        } else {// VIEW_TYPE_GENERIC
            return new GenericViewHolder(new GenericLayout(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        @ViewType int viewType = getItemViewType(position);
        if (VIEW_TYPE_VIDEO == viewType) {
            MediaModel mm = getItem(position);

            final VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.getItemView().setVideoMediaModel((VideoMediaModel) mm);
            videoViewHolder.getItemView().setTag(mm);
        } else {// VIEW_TYPE_GENERIC
            // do nothing
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).getItemView().stop();
        }
    }

    private class VideoViewHolder extends RecyclerView.ViewHolder {

        public VideoViewHolder(View itemView) {
            super(itemView);
        }

        public VideoLayout getItemView() {
            return (VideoLayout) itemView;
        }
    }

    private class GenericViewHolder extends RecyclerView.ViewHolder {

        public GenericViewHolder(View itemView) {
            super(itemView);
        }

        public GenericLayout getItemView() {
            return (GenericLayout) itemView;
        }
    }
}
