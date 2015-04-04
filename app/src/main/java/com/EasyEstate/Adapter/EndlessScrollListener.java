package com.EasyEstate.Adapter;

import android.widget.AbsListView;

/**
 * Created by canturker on 04/04/15.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener{
    private int visibleThreshold = 7;
    private int currentPage = 0;
    private int previousTotalItemCount= 0;
    private boolean loading = true;
    private int startingPageIndex  = 0;
    public EndlessScrollListener(){

    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentPage, totalItemCount);
            currentPage++;
            loading = true;
        }
    }
    public abstract void onLoadMore(int page, int totalItemsCount);
}
