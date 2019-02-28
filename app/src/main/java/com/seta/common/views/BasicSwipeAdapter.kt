package com.seta.common.views

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by SETA_WORK on 2017/7/19.
 * 封装基本的 Adapter
 */

/**
 * @param layoutResId: 每个条目布局 resId
 * @param data: 需要设置的数据
 * @param bindFunc: ViewHolder 绑定数据
 */
class BasicSwipeAdapter<D>(val layoutResId: Int, val frontViewId: Int? = null, val backViewId: Int? = null, val backViewWidth: Float = 0f, var data: List<D> = ArrayList<D>(), val bindFunc: (View, Int, D) -> Unit)
    : RecyclerView.Adapter<BasicSwipeAdapter.BaseSwipeHolder<D>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSwipeHolder<D> {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return BaseSwipeHolder(view, backViewId, frontViewId, backViewWidth, bindFunc)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseSwipeHolder<D>, position: Int) = holder.bindData(position, data[position])

    fun refreshData(data: List<D>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun addData(data: List<D>) {
        val newData = ArrayList<D>()
        newData.addAll(this.data)
        newData.addAll(data)
        this.data = newData
        notifyDataSetChanged()
    }

    fun notifyItemChanged(item: D) {
        notifyItemChanged(data.indexOf(item))
    }

    /**
     * ViewHolder 类
     */
    class BaseSwipeHolder<in T>(view: View, val frontViewId: Int? = null, val backViewId: Int? = null, val backViewWidth: Float, val bindFunc: (View, Int, T) -> Unit)
        : RecyclerView.ViewHolder(view), HolderSwipable {
        //绑定数据
        fun bindData(position: Int, item: T) {
            with(item) {
                bindFunc(itemView, position, item)
            }
        }

        override fun getActionWidth() = backViewWidth
        override fun getFrontView(): View? = frontViewId?.let { itemView.findViewById(it) }
        override fun getBackView(): View? = backViewId?.let { itemView.findViewById(it) }
    }

    interface HolderSwipable {
        fun getActionWidth(): Float
        fun getFrontView(): View?
        fun getBackView(): View?
    }
}