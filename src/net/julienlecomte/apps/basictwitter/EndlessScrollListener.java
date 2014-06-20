package net.julienlecomte.apps.basictwitter;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class EndlessScrollListener implements OnScrollListener
{
	// The minimum amount of items to have below your current scroll position
	// before loading more.
	protected int visibleThreshold = 5;

	// The current offset index of data you have loaded
	protected int currentPage = 0;

	// The total number of items in the data-set after the last load
	protected int previousTotalItemCount = 0;

	// True if we are still waiting for the last set of data to load.
	protected boolean loading = true;

	// Sets the starting page index
	protected int startingPageIndex = 0;

	public EndlessScrollListener() {}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	public EndlessScrollListener(int visibleThreshold, int startPage) {
		this.visibleThreshold = visibleThreshold;
		this.startingPageIndex = startPage;
		this.currentPage = startPage;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		// If the total item count is zero and the previous isn't, assume the
		// list is invalidated and should be reset back to initial state
		if (totalItemCount < previousTotalItemCount) {
			this.currentPage = this.startingPageIndex;
			this.previousTotalItemCount = totalItemCount;
			if (totalItemCount == 0) { this.loading = true; } 
		}

		// If it’s still loading, we check to see if the data-set count has
		// changed, if so we conclude it has finished loading and update the
		// current page number and total item count.
		if (loading && (totalItemCount > previousTotalItemCount)) {
			loading = false;
			previousTotalItemCount = totalItemCount;
			currentPage++;
		}

		// If it isn’t currently loading, we check to see if we have breached
		// the visibleThreshold and need to reload more data.
		if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			onLoadMore(currentPage + 1, totalItemCount);
			loading = true;
		}
	}

	// Defines the process for actually loading more data based on page
	public abstract void onLoadMore(int page, int totalItemsCount);
}
