package com.example.riccardogazzea.eventbussample;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by riccardogazzea on 06/02/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {
    private boolean isViewVideo[];

    /**
     * Default constructor
     */
    public CustomAdapter(){
        //andrà sostituito con la lista dinamica di URI/URL
        isViewVideo = new boolean[]{false, true, false, false, false, true, false};
    }

    @Override
    public int getItemCount() {
        return isViewVideo != null ? isViewVideo.length : 0;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.adapter, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //determino se la vista contiene un video o meno, mostrando il widget adeguato
        if (isViewVideo[position]){
            holder.customView.setTag(CustomView.TAG, position);
            holder.customView.setVisibility(View.VISIBLE);
        }else{
            holder.view.setTag(CustomView.TAG, position);
            holder.view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        holder.view.setTag(CustomView.TAG, null);
        holder.customView.setTag(CustomView.TAG, null);
        holder.view.setVisibility(View.INVISIBLE);
        holder.customView.setVisibility(View.INVISIBLE);
    }

    /**
     * Return if the element at the given pos is a video or not
     * @param pos position of the view
     * @return true if it is a video, false otherwise
     */
    public boolean isVideo(int pos){
        if (pos == -1 || pos >= isViewVideo.length){
            return false;
        }else{
            return isViewVideo[pos];
        }
    }

    private String TEST_URL = "https://s3-eu-west-1.amazonaws.com/nowr-dev/uservideos/1004/SampleVideo_1280x720_5mb.mp4";

    /**
     * Return the video URI for the {@link com.baseandroid.events.EventDispatcher}'s post
     * @param pos position of the view
     * @return the video uri
     */
    public Uri getVideoUri(int pos){
        return Uri.parse(TEST_URL);
    }

    class Holder extends RecyclerView.ViewHolder {
        //sarà un'imageView idealmente
        private View view;
        //estende VideoView
        private CustomView customView;
        public Holder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            customView = itemView.findViewById(R.id.view_video);
        }
    }
}
