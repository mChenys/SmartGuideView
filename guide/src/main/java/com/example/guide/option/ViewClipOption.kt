package com.example.guide.option

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt


/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 根据View的位置确定裁剪区域配置项
 */
class ViewClipOption : BaseClipOption() {

    @IdRes
    private var clipViewId = 0
    private var clipDestView: View? = null
    private var padding = 0f
    private var parentWidth = 0f
    private var parentHeight = 0f

    companion object {
        fun newClipOption(): ViewClipOption = ViewClipOption()
    }

    /**
     * 设置目标View
     */
    fun setDstView(view: View?): ViewClipOption {
        clipDestView = view
        return this
    }

    /**
     * 设置目标View的id
     */
    fun setDstView(@IdRes viewId: Int): ViewClipOption {
        clipViewId = viewId
        return this
    }

    /**
     * 基于目标View区域设置padding
     */
    fun setPadding(padding: Float): ViewClipOption {
        this.padding = padding
        return this
    }

    /**
     * 设置水平偏移量
     */
    fun setOffsetX(offsetX: Float): ViewClipOption {
        this.offsetX = offsetX
        return this
    }

    /**
     * 设置垂直偏移量
     */
    fun setOffsetY(offsetY: Float): ViewClipOption {
        this.offsetY = offsetY
        return this
    }

    /**
     * 针对圆形裁剪区域设置半径
     */
    fun clipRadius(radius: Float): ViewClipOption {
        this.radius = radius
        return this
    }

    /**
     * 设置不规则裁剪图形PNG资源
     */
    fun asIrregularShape(context: Context, @DrawableRes bitmapId: Int): ViewClipOption {
        irregularClip = BitmapFactory.decodeResource(context.resources, bitmapId)
        return this
    }

    /**
     * 设置不规则裁剪图形PNG资源
     */
    fun asIrregularShape(bitmap: Bitmap): ViewClipOption {
        irregularClip = bitmap
        return this
    }


    /**
     * 是否镂空区域事件穿透
     */
    fun setEventPassThrough(eventPassThrough: Boolean): ViewClipOption {
        this.eventPassThrough = eventPassThrough
        return this
    }

    /**
     * 获取目标View
     */
    override fun build(activity: Activity?) {
        if (clipDestView == null && clipViewId != 0) {
            clipDestView = activity?.findViewById(clipViewId)
        }
    }

    /**
     * 获取目标View
     */
    override fun build(fragment: Fragment?) {
        if (clipDestView == null && clipViewId != 0 && fragment?.view != null) {
            clipDestView = fragment.view?.findViewById(clipViewId)
        }
    }

    /**
     * 绘制裁剪区域
     */
    override fun draw(canvas: Canvas, paint: Paint, parentWidth: Float, parentHeight: Float) {
        initRect(parentWidth, parentHeight);
        this.parentHeight = parentHeight;
        this.parentWidth = parentWidth;
        if (irregularClip == null) {
            // 按裁剪图形绘制区域
            drawClipShape(canvas, paint);
        } else {
            // 按bitmap绘制
            drawClipBitmap(canvas, paint);
        }
    }

    // 绘制裁剪区域
    private fun drawClipShape(canvas: Canvas, paint: Paint) {
        if (rectF == null) {
            return
        }
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(rectF!!, radius, radius, paint)
        paint.xfermode = null
    }

    // 绘制裁剪区域
    private fun drawClipBitmap(canvas: Canvas, paint: Paint) {
        if (rectF == null) {
            return
        }
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawBitmap(irregularClip!!, rectF!!.left, rectF!!.top, paint)
        paint.xfermode = null
    }


    // 根据View的位置确定裁剪区域
    private fun initRect(parentWidth: Float, parentHeight: Float) {
        if (rectF != null || parentHeight != this.parentHeight || parentWidth != this.parentWidth) {
            if (clipDestView != null) {
                val pos = IntArray(2)
                clipDestView!!.getLocationInWindow(pos)
                val left = pos[0] + offsetX - padding
                val top = pos[1] + offsetY - padding
                val width = clipDestView!!.measuredWidth + padding * 2
                val height = clipDestView!!.height + padding * 2
                rectF = RectF(left, top, left + width, top + height)
                buildDstSizeBitmap()
            }
        }
    }

    private fun buildDstSizeBitmap() {
        if (irregularClip != null && rectF != null) {
            val bitmap = Bitmap.createScaledBitmap(
                irregularClip!!, rectF!!.width().roundToInt(), rectF!!.height().roundToInt(), true
            )
            if (!bitmap.isRecycled) {
                irregularClip = bitmap
            }
        }
    }

}