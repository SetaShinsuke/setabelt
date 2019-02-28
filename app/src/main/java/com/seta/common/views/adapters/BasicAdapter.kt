package com.seta.common.views.adapters

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
class BasicAdapter<D>(val layoutResId: Int, var data: List<D> = ArrayList<D>(), val bindFunc: (View, Int, D) -> Unit)
    : RecyclerView.Adapter<BasicAdapter.BaseHolder<D>>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<D> {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return BaseHolder(view, bindFunc)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseHolder<D>, position: Int) = holder.bindData(position, data[position])

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
    class BaseHolder<in T>(view: View, val bindFunc: (View, Int, T) -> Unit)
        : RecyclerView.ViewHolder(view) {
        //绑定数据
        fun bindData(position: Int, item: T) {
            with(item) {
                bindFunc(itemView, position, item)
            }
        }
    }
}