package com.schoofi.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class VideoPlayView extends VideoView {
    public interface PlayPauseListener {
        void onPlay();
        void onPause();
    }

    private PlayPauseListener mListener;

    public VideoPlayView(Context context) {
        super(context);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayView(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
    }

    @Override
    public void pause() {
        super.pause();
        if(mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    public void start() {
        super.start();
        if(mListener != null) {
            mListener.onPlay();
        }
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }

}
