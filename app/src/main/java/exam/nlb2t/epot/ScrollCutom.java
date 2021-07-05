package exam.nlb2t.epot;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.logging.LogRecord;

public class ScrollCutom extends RecyclerView.OnScrollListener{
    public int MAX_NUMBER_ITEM_IN_PAGE = 5;
    private int MAX_NUMBER_PAGE_IN_CACHE = 3;
    public final int REMAIN_NUMBER_ITEM_BEFORE_LOAD = 2;
    private int mNumberPage = 1;
    private List<Object> mList;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoad;
    private boolean isRemove;
    private boolean isEndPage;
    //private final RecyclerView.Adapter mAdapter;

    private boolean isBottomPages(int lastCurrentPositionItem) {
        if (MAX_NUMBER_ITEM_IN_PAGE > 0)
        while (isLoad && lastCurrentPositionItem == MAX_NUMBER_ITEM_IN_PAGE * MAX_NUMBER_PAGE_IN_CACHE); //wait for load next page finish
        return false;
    }
    private boolean isTopPages(int startCurrentPositionItem) {
        while (isLoad && startCurrentPositionItem == 0); //wait for load previous page finish
        return false;
    }

    public void setListPage(List<Object> list) {
        mList = list;
    }

    Handler mHandler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            if (msg.what == 1) {
//                mHandler.removeMessages(0);
//                mHandler.removeMessages(1);
//            }
//        }
    };

    public ScrollCutom(LinearLayoutManager linearLayoutManager) {
        //this.mAdapter = adapter;
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void loadNextPage(int index_item_end_list) {

    }
    public void loadNextPageUI(int index_item_end_list) {

    }
    public void loadPreviousPage(int index_item_start_list) {

    }
    public void loadPreviousPageUI(int index_item_start_list, int count) {

    }

    public void setIsEndPage(boolean value) {
        isEndPage = value;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        int visibleItemsCount = mLinearLayoutManager.getChildCount();
        int passVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int numberPageBeforeLoad = totalItemCount / MAX_NUMBER_ITEM_IN_PAGE + (totalItemCount % MAX_NUMBER_ITEM_IN_PAGE == 0? 0:1);

        if (dy>0) {
            if (passVisibleItems + visibleItemsCount >= totalItemCount - REMAIN_NUMBER_ITEM_BEFORE_LOAD) {
                if (!isBottomPages(passVisibleItems + visibleItemsCount - 1) && !isLoad) {
                    if (!isEndPage) {
                        loadPage(mNumberPage, true);
                        if (numberPageBeforeLoad == MAX_NUMBER_PAGE_IN_CACHE) removePage(mNumberPage - MAX_NUMBER_PAGE_IN_CACHE + 1);
                        mNumberPage++;
                    }
                }
            }
        }
        else {
            if (passVisibleItems <= REMAIN_NUMBER_ITEM_BEFORE_LOAD) {
                if (!isTopPages(passVisibleItems) && !isLoad) {
                    if (mNumberPage > MAX_NUMBER_PAGE_IN_CACHE) {
                        loadPage(mNumberPage - MAX_NUMBER_PAGE_IN_CACHE, false);
                        if (numberPageBeforeLoad == MAX_NUMBER_PAGE_IN_CACHE) {
                            removePage(mNumberPage - 1);
                            mNumberPage--;
                        }
                    }
                }
            }
        }
    }

    private void loadPage(int PageNumber, boolean isloadNextPage) {
        synchronized (ScrollCutom.this) {
            if (isloadNextPage) {
                isLoad = true;
                new Thread(()-> {
                    int index = PageNumber * MAX_NUMBER_ITEM_IN_PAGE - 1;
                    loadNextPage(index);
                    mHandler.postDelayed(()->{
                        loadNextPageUI(index);
                        isLoad = false;
                    }, 100);
                }).start();
            }
            else {
                isLoad = true;
                loadPreviousPage((PageNumber+1) * MAX_NUMBER_ITEM_IN_PAGE);
            }
        }
    }

    private void removePage(int PageNumber) {
        int indexPage_to_remove = PageNumber - (mNumberPage - MAX_NUMBER_PAGE_IN_CACHE);
        
        if ((indexPage_to_remove + 1) * MAX_NUMBER_ITEM_IN_PAGE < mList.size()) {

        }
    }
}
