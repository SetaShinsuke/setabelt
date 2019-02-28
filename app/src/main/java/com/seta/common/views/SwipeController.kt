package com.seta.common.views

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE
import android.support.v7.widget.helper.ItemTouchHelper.LEFT
import android.view.MotionEvent


/***
 * 划出去后自动回弹
 */
class SwipeController(val buttonWidth: Float) : ItemTouchHelper.Callback() {
    companion object {
        val BTN_GONE = 0
        val BTN_RIGHT_VISIBLE = 1
        val BTN_LEFT_VISIBLE = 1
    }

    var swipeBack = false
    var buttonShowedState = BTN_GONE
    var currentItemViewHolder: RecyclerView.ViewHolder? = null

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int = ItemTouchHelper.Callback.makeMovementFlags(0, LEFT)

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        var x = dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != BTN_GONE) {
                if (buttonShowedState == BTN_LEFT_VISIBLE) {
                    x = Math.max(dX, buttonWidth)
                }
                if (buttonShowedState == BTN_RIGHT_VISIBLE) {
                    x = Math.min(dX, -buttonWidth)
                }
                super.onChildDraw(c, recyclerView, viewHolder, x, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, x, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == BTN_GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, x, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder
    }

    private fun drawBtns(c: Canvas, viewHolder: RecyclerView.ViewHolder) {

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(c: Canvas,
                                 recyclerView: RecyclerView?,
                                 viewHolder: RecyclerView.ViewHolder,
                                 dX: Float, dY: Float,
                                 actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView?.setOnTouchListener { v, event ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -buttonWidth) {
                    buttonShowedState = BTN_RIGHT_VISIBLE
                } else if (dX > buttonWidth) {
                    buttonShowedState = BTN_LEFT_VISIBLE
                }

                if (buttonShowedState != BTN_GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    setItemsClickable(recyclerView, false)
                }
            }
            return@setOnTouchListener false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(c: Canvas,
                                     recyclerView: RecyclerView,
                                     viewHolder: RecyclerView.ViewHolder,
                                     dX: Float, dY: Float,
                                     actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(c: Canvas,
                                   recyclerView: RecyclerView,
                                   viewHolder: RecyclerView.ViewHolder,
                                   dX: Float, dY: Float,
                                   actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                super@SwipeController.onChildDraw(c, recyclerView, viewHolder, 0f, dY, actionState, isCurrentlyActive)
                recyclerView.setOnTouchListener { _, _ ->
                    false
                }
                setItemsClickable(recyclerView, true)
                swipeBack = false

//                if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.x, event.y)) {
//                    if (buttonShowedState === ButtonsState.LEFT_VISIBLE) {
//                        buttonsActions.onLeftClicked(viewHolder.adapterPosition)
//                    } else if (buttonShowedState === ButtonsState.RIGHT_VISIBLE) {
//                        buttonsActions.onRightClicked(viewHolder.adapterPosition)
//                    }
//                }
                buttonShowedState = BTN_GONE
                currentItemViewHolder = null
            }
            return@setOnTouchListener false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView,
                                  isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }
}