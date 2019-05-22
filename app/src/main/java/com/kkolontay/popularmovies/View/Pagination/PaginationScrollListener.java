package com.kkolontay.popularmovies.View.Pagination;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

//public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
//
//    GridLayoutManager layoutManager;
//
//    public PaginationScrollListener(GridLayoutManager layoutManager) {
//        this.layoutManager = layoutManager;
//    }
//
//    @Override
//    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        super.onScrolled(recyclerView, dx, dy);
//
//        int visibleItemCount = layoutManager.getChildCount();
//        int totalItemCount = layoutManager.getItemCount();
//        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//        if (!isLoading() && !isLastPage()) {
//            if ((visibleItemCount + firstVisibleItemPosition) >=
//                    totalItemCount && firstVisibleItemPosition >= 0) {
//                loadMoreItems();
//            }
//        }
//    }
//
//    protected abstract void loadMoreItems();
//    public abstract int getTotalPageCount();
//    public abstract boolean isLastPage();
//    public abstract boolean isLoading();
//}

public class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private final static int VISIBLE_THRESHOLD = 2;
    private GridLayoutManager gridLayoutManager;
    private boolean loading; // LOAD MORE Progress dialog
    private OnLoadMoreListener listener;
    private boolean pauseListening = false;


    private boolean END_OF_FEED_ADDED = false;
   // private int NUM_LOAD_ITEMS = 10;

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

    public void pauseScrollListener(boolean pauseListening) {
        this.pauseListening = pauseListening;
    }
}


