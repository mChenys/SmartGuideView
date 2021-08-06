package com.example.guide

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewConfiguration
import com.example.guide.layer.Layer

import android.app.Activity
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.FrameLayout

import android.view.MotionEvent
import androidx.fragment.app.Fragment
import kotlin.math.abs


/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 引导层View
 */
class GuideView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    def: Int = 0
) : FrameLayout(context, attrs, def) {

    companion object {
        private const val TAG = "GuideView"
    }

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mLayerList = mutableListOf<Layer>()
    private var drawTaskId = 0
    private var mRectF: RectF? = null
    private var backgroundColor: Int = 0X80000000.toInt()
    private val mMinTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var mClickListener: InnerOnGuidClickListener? = null

    /**
     * 引导层点击事件监听器
     */
    interface InnerOnGuidClickListener {
        /**
         * 引导层销毁回调
         */
        fun destroyed()

        /**
         * 点击蒙层非裁剪和信息区域回调，返回true，直接退出引导，返回false则不退出
         * @return
         */
        fun emptyClicked(): Boolean

        /**
         * 引导镂空区域点击回调，如果镂空区域设置了事件透传，则不回调
         * @param tag
         */
        fun clipClicked(tag: String)

        /**
         * 引导介绍区域点击回调
         * @param tag
         */
        fun descClicked(tag: String)
    }

    /**
     * 设置背景颜色
     */
    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
    }

    /**
     * 设置点击事件
     */
    fun setOnInnerOnGuidClickListener(listener: InnerOnGuidClickListener?) {
        this.mClickListener = listener
    }

    init {
        setWillNotDraw(false)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = right - left
        val height = bottom - top
        if (mRectF == null || mRectF!!.width() < width || mRectF!!.height() < height) {
            mRectF = RectF(0F, 0F, width.toFloat(), height.toFloat())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mRectF == null) {
            return
        }
        drawTaskId = canvas.saveLayer(mRectF, mPaint)
        // 绘制蒙层背景
        canvas.drawColor(backgroundColor)
        // 绘制裁剪区域
        for (i in mLayerList.indices) {
            mLayerList[i].draw(canvas, mPaint, true, mRectF!!.width(), mRectF!!.height())
        }
        // 绘制引导区域
        for (i in mLayerList.indices) {
            mLayerList[i].draw(canvas, mPaint, false, mRectF!!.width(), mRectF!!.height())
        }
        canvas.restoreToCount(drawTaskId)
    }

    /**
     * 添加图层
     * @param layer
     */
    fun addLayer(layer: Layer?) {
        if (layer == null) {
            return
        }
        mLayerList.add(layer)
        postInvalidate()
    }

    /**
     * 构建图层
     */
    fun build(activity: Activity) {
        val rootView: FrameLayout = activity.window.decorView as FrameLayout
        val oldView: View? = rootView.findViewWithTag(TAG)
        if (oldView != null) {
            rootView.removeView(oldView)
        }
        tag = TAG
        rootView.addView(
            this, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        for (i in mLayerList.indices) {
            mLayerList[i].build(activity)
        }
        postInvalidate()
    }

    /**
     * 构建图层
     */
    fun build(fragment: Fragment) {
        val rootView: FrameLayout = fragment.requireActivity().window.decorView as FrameLayout
        val oldView: View? = rootView.findViewWithTag(TAG)
        if (null != oldView) {
            rootView.removeView(oldView)
        }
        tag = TAG
        rootView.addView(
            this, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        for (i in mLayerList.indices) {
            mLayerList[i].build(fragment)
        }
        postInvalidate()
    }

    /**
     * 清空图层
     */
    fun clearLayers() {
        mLayerList.clear()
        postInvalidate()
    }

    /**
     * 删除某图层
     */
    fun removeLayerByTag(tag: String) {
        if (!TextUtils.isEmpty(tag)) {
            for (index in mLayerList.count() - 1 downTo 0) {
                if (tag == mLayerList[index].tag) {
                    mLayerList.removeAt(index)
                }
            }
        }
        postInvalidate()
    }

    /**
     * 关闭图层
     */
    fun dismiss() {
        val parentView: ViewGroup = parent as ViewGroup
        parentView.removeView(this)
        mClickListener?.destroyed()
    }


    // 点击处理
    private var isTouchIn = false
    private var mPressX = 0f
    private var mPressY = 0f
    private var mLastPressTime: Long = 0


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isTouchIn = false
                mPressX = event.x
                mPressY = event.y
                mLastPressTime = System.currentTimeMillis()
                val result = checkIsLayerIn(mPressX, mPressY)
                isTouchIn = !(result[0] && result[1])
            }
            MotionEvent.ACTION_MOVE -> return true
            MotionEvent.ACTION_UP -> {
                val x: Float = event.x
                val y: Float = event.y
                if (isTouchIn) {
                    if (System.currentTimeMillis() - mLastPressTime < 300) {
                        if (abs(x - mPressX) < mMinTouchSlop && abs(y - mPressY) < mMinTouchSlop) {
                            executeClick(mPressX, mPressY)
                        }
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> isTouchIn = false
            else -> {
            }
        }
        return if (isTouchIn) isTouchIn else super.onTouchEvent(event)
    }

    // 执行点击
    private fun executeClick(x: Float, y: Float) {
        if (mClickListener == null) {
            return
        }
        for (i in mLayerList.indices) {
            val item: Layer = mLayerList[i]
            if (item.isTouchInClip(x, y)) {
                mClickListener!!.clipClicked(item.tag)
                return
            } else if (item.isTouchInIntro(x, y)) {
                mClickListener!!.descClicked(item.tag)
                return
            }
        }
        if (isTouchIn && mClickListener != null) {
            val result = mClickListener!!.emptyClicked()
            if (result) {
                dismiss()
            }
        }
    }

    // 判断是否在点击区域
    private fun checkIsLayerIn(x: Float, y: Float): BooleanArray {
        val result = booleanArrayOf(false, false)
        for (i in mLayerList.indices) {
            val item: Layer = mLayerList[i]
            if (item.isTouchInClip(x, y)) {
                result[0] = true
                result[1] = item.isClipEventPassThrough()
                break
            }
        }
        return result
    }
}