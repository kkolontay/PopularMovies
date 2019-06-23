package com.kkolontay.popularmovies.View.DetailMovie;

import android.os.Bundle;
import android.util.Log;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class VideoFragment extends YouTubePlayerSupportFragment {
    private static final String DEVELOPER_ANDROID_KEY = PlayerConfig.YOUTUBE_KEY;
    static final String TAG = VideoFragment.class.getSimpleName();

    public YouTubePlayer getPlayer() {
        return playerClass;
    }

    private YouTubePlayer playerClass;


    @Override
    public void onDestroy() {
        super.onDestroy();
        playerClass.release();
    }

    public static VideoFragment newInstance(String url) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        videoFragment.setArguments(bundle);
        videoFragment.init(0);
        return videoFragment;
    }

    private void init(final int time) {
        initialize(DEVELOPER_ANDROID_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
            Log.v(TAG, arg1.toString());

            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer
                    player, boolean wasRestored) {

                player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
               String  id = getArguments().getString("url");
                if (!wasRestored) {
                    player.loadVideo(id, time);
                    playerClass = player;
                }
            }
        });
    }
}