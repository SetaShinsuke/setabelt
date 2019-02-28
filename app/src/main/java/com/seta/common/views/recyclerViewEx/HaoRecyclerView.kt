package com.seta.common.views.recyclerViewEx

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.widget.AdapterView

/**
 * Created by fangxiao on 15/11/19.
 */
class HaoRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    private var mContext: Context? = null

    private var loadMoreListener: LoadMoreListener? = null
    //是否可加载更多
    private var canloadMore = true

    private var mAdapter: RecyclerView.Adapter<*>? = null

    private var mFooterAdapter: RecyclerView.Adapter<*>? = null

    private var isLoadingData = false
    //加载更多布局
    private var loadingMoreFooter: LoadingMoreFooter? = null

    private val mDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            mFooterAdapter?.notifyDataSetChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mFooterAdapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mFooterAdapter?.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            mFooterAdapter?.notifyItemRangeChanged(positionStart, itemCount, payload)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mFooterAdapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mFooterAdapter?.notifyItemMoved(fromPosition, toPosition)
        }
    }

    init {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        if (isInEditMode) {
            return
        }
        val footView = LoadingMoreFooter(mContext)
        addFootView(footView)
        footView.setGone()
    }


    //点击监听
    fun setOnItemClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
        if (mFooterAdapter != null && mFooterAdapter is FooterAdapter) {
            (mFooterAdapter as FooterAdapter).setOnItemClickListener(onItemClickListener)
        }
    }


    //长按监听
    fun setOnItemLongClickListener(listener: AdapterView.OnItemLongClickListener) {
        if (mFooterAdapter != null && mFooterAdapter is FooterAdapter) {
            (mFooterAdapter as FooterAdapter).setOnItemLongClickListener(listener)
        }
    }

    /**
     * 底部加载更多视图
     *
     * @param view
     */
    fun addFootView(view: LoadingMoreFooter) {
        loadingMoreFooter = view
    }

    //    //设置底部加载中效果
    //    public void setFootLoadingView(View view) {
    //        if (loadingMoreFooter != null) {
    //            loadingMoreFooter.addFootLoadingView(view);
    //        }
    //    }
    //
    //    //设置底部到底了布局
    //    public void setFootEndView(View view) {
    //        if (loadingMoreFooter != null) {
    //            loadingMoreFooter.addFootEndView(view);
    //        }
    //    }

    //下拉刷新后初始化底部状态
    fun refreshComplete() {
        if (loadingMoreFooter != null) {
            loadingMoreFooter!!.setGone()
        }
        isLoadingData = false
    }

    fun complete() {
        isLoadingData = false
    }

    fun loadMoreComplete() {
        if (loadingMoreFooter != null) {
            loadingMoreFooter!!.setGone()
        }
        isLoadingData = false
    }

    //到底了
    fun setEnd() {
        if (loadingMoreFooter != null) {
            loadingMoreFooter!!.setEnd()
        }
        isLoadingData = true
    }

    fun setLoading() {
        if (loadingMoreFooter != null) {
            loadingMoreFooter!!.setLoading()
        }
    }

    fun setError() {
        if (loadingMoreFooter != null) {
            loadingMoreFooter!!.setError()
        }
        isLoadingData = false
    }

    //设置是否可加载更多
    fun setCanloadMore(flag: Boolean) {
        canloadMore = flag
    }

    //设置加载更多监听
    fun setLoadMoreListener(onLoadMore: (() -> Unit)) {
        loadMoreListener = object : LoadMoreListener {
            override fun onLoadMore() {
                onLoadMore.invoke()
            }
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        mAdapter = adapter
        mFooterAdapter = FooterAdapter(this, loadingMoreFooter, adapter)
        super.setAdapter(mFooterAdapter)
        mAdapter!!.registerAdapterDataObserver(mDataObserver)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE && loadMoreListener != null && !isLoadingData && canloadMore) {
            val layoutManager = layoutManager
            val lastVisibleItemPosition: Int
            if (layoutManager is GridLayoutManager) {
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val into = IntArray(layoutManager.spanCount)
                layoutManager.findLastVisibleItemPositions(into)
                lastVisibleItemPosition = last(into)
            } else {
                lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            if (layoutManager.childCount > 0 && lastVisibleItemPosition >= layoutManager.itemCount - 1) {
                if (loadingMoreFooter != null) {
                    loadingMoreFooter!!.setVisible()
                }
                isLoadingData = true
                loadMoreListener!!.onLoadMore()
            }
        }
    }


    //取到最后的一个节点
    private fun last(lastPositions: IntArray): Int {
        var last = lastPositions[0]
        for (value in lastPositions) {
            if (value > last) {
                last = value
            }
        }
        return last
    }


}
