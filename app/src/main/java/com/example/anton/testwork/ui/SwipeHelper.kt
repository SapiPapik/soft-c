package com.example.anton.testwork.ui

import android.graphics.*
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*

@Suppress("DEPRECATION")
abstract class SwipeHelper(
        context: FragmentActivity?,
        private val recyclerView: RecyclerView
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    // указывается часть ширины экрана, которая отводится для кнопки
    // при = 8 -> 1/8 ширины экрана под кнопку
    private val buttonSize = 6
    private var buttons: ArrayList<UnderlayButton> = ArrayList()
    private var swipedPos = -1
    private var swipeThreshold = 0.5f
    private val buttonsBuffer: HashMap<Int, List<UnderlayButton>> = HashMap()
    private val recoverQueue: Queue<Int> = object : LinkedList<Int>() {
        override fun add(element: Int) =
                if (contains(element)) false else super.add(element)
    }
    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            for (button in buttons) {
                if (button.onClick(e.x, e.y))
                    break
            }
            return true
        }
    }
    private var gestureDetector = GestureDetector(context, gestureListener)

    private val onTouchListener = View.OnTouchListener { _, e ->
        if (swipedPos < 0) return@OnTouchListener false
        val point = Point(e.rawX.toInt(), e.rawY.toInt())
        val swipedViewHolder =
                recyclerView.findViewHolderForAdapterPosition(swipedPos) ?: return@OnTouchListener false

        val swipedItem = swipedViewHolder.itemView
        val rect = Rect()
        swipedItem.getGlobalVisibleRect(rect)
        if (e.action == MotionEvent.ACTION_DOWN ||
                e.action == MotionEvent.ACTION_UP ||
                e.action == MotionEvent.ACTION_MOVE) {
            if (rect.top < point.y && rect.bottom > point.y)
                gestureDetector.onTouchEvent(e)
            else {
                recoverQueue.add(swipedPos)
                swipedPos = -1
                recoverSwipedItem()
            }
        }
        false
    }

    init {
        this.recyclerView.setOnTouchListener(onTouchListener)
    }

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipedPos != pos)
            recoverQueue.add(swipedPos)
        swipedPos = pos
        if (buttonsBuffer.containsKey(swipedPos))
            buttonsBuffer[swipedPos]?.let { buttons.addAll(it) }
        else
            buttons.clear()
        buttons.forEach { it.pos = pos }
        swipeThreshold = 0.5f * buttons.size * viewHolder.itemView.width / buttonSize / 5
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = swipeThreshold

    override fun getSwipeEscapeVelocity(defaultValue: Float) = 0.1f * defaultValue

    override fun getSwipeVelocityThreshold(defaultValue: Float) = 5.0f * defaultValue

    override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView
        if (pos < 0) {
            swipedPos = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                val buffer = ArrayList<UnderlayButton>()
                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer)
                    buttonsBuffer.put(pos, buffer)
                } else {
                    buttonsBuffer[pos]?.let { buffer.addAll(it) }
                }
                viewHolder.itemView.width
                translationX = dX * buffer.size / buttonSize
                drawButtons(c, itemView, buffer, pos, translationX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    private fun recoverSwipedItem() {
        while (recoverQueue.isNotEmpty()) {
            val pos = recoverQueue.poll()
            if (pos > -1) {
                recyclerView.adapter.notifyItemChanged(pos)
            }
        }
    }

    private fun drawButtons(c: Canvas, itemView: View, buffer: List<UnderlayButton>, pos: Int, dX: Float) {
        var right = itemView.right.toFloat()
        val dButtonWidth = (-1) * dX / buffer.size
        for (button in buffer) {
            val left = right - dButtonWidth
            val rect = RectF(left, itemView.top.toFloat(), right, itemView.bottom.toFloat())
            button.onDraw(c, rect, pos)
            right = left
        }
    }

    abstract fun instantiateUnderlayButton(
            viewHolder: RecyclerView.ViewHolder,
            underlayButtons: ArrayList<UnderlayButton>
    )

    class UnderlayButton(
            private val icon: Bitmap,
            private var color: Int,
            private val clickListener: UnderlayButtonClickListener
    ) {
        var pos: Int = 0
        private var clickRegion: RectF? = null

        fun onClick(x: Float, y: Float): Boolean {
            clickRegion?.let {
                if (it.contains(x, y)) run {
                    clickListener.onClick(pos)
                    return true
                }
            }
            return false
        }

        fun onDraw(c: Canvas, rect: RectF, pos: Int) {
            val p = Paint()
            p.color = color
            c.drawRect(rect, p)

            val top = (rect.height() - icon.height) / 2
            val left = (rect.width() - icon.width) / 2
            c.drawBitmap(icon, rect.left + left, rect.top + top, p)
            clickRegion = rect
            this.pos = pos
        }
    }

    interface UnderlayButtonClickListener {
        fun onClick(pos: Int)
    }
}