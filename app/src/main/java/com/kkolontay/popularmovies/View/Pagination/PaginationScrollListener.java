package com.kkolontay.popularmovies.View.Pagination;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


public class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private final static int VISIBLE_THRESHOLD = 2;
    private final GridLayoutManager gridLayoutManager;
    private boolean loading;
    private OnLoadMoreListener listener;
    private boolean pauseListening = false;


    private boolean END_OF_FEED_ADDED = false;

    public PaginationScrollListener(GridLayoutManager gridLayoutManager, OnLoadMoreListener listener) {
        this.gridLayoutManager = gridLayoutManager;
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dx == 0 && dy == 0)
            return;
        int totalItemCount = gridLayoutManager.getItemCount();
        int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
        if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD && totalItemCount != 0 && !END_OF_FEED_ADDED && !pauseListening) {
            if (listener != null) {
                listener.onLoadMore();
            }
            loading = true;
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void addEndOfRequests() {
        this.END_OF_FEED_ADDED = true;
    }
}


