package com.example.guide.option

import android.app.Activity
import android.content.Context
import android.graphics.*
import com.example.guide.SmartGuide

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment


/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 自定义裁剪方式配置项
 */
class CustomClipOption : BaseClipOption() {
    // 对齐方式
    private var alignX: SmartGuide.AlignX = SmartGuide.AlignX.ALIGN_LEFT
    private var alignY: SmartGuide.AlignY = SmartGuide.AlignY.ALIGN_TOP

    // 裁剪大小
    private var clipWidth = 0
    private var clipHeight = 0

    // 容器大小
    private var parentWidth = 0f
    private var parentHeight = 0f

    companion object {
        fun newClipOption(): CustomClipOption = CustomClipOption()
    }

    /**
     * 设置裁剪区域大小
     */
    fun setClipSize(width: Int, height: Int): CustomClipOption {
        clipWidth = width
        clipHeight = height
        return this
    }

    /**
     * 设置裁剪区域水平对齐方式
     */
    fun setAlignX(alignX: SmartGuide.AlignX): CustomClipOption {
        this.alignX = alignX
        return this
    }

    /**
     * 设置裁剪区域垂直对齐方式
     */
    fun setAlignY(alignY: SmartGuide.AlignY): CustomClipOption {
        this.alignY = alignY
        return this
    }

    /**
     * 设置裁剪区域水平偏移量
     */
    fun setOffsetX(offsetX: Float): CustomClipOption {
        this.offsetX = offsetX
        return this
    }

    /**
     * 设置裁剪区域垂直偏移量
     */
    fun setOffsetY(offsetY: Float): CustomClipOption {
        this.offsetY = offsetY
        return this
    }

    /**
     * 针对圆形裁剪区域设置半径
     */
    fun clipRadius(radius: Float): CustomClipOption {
        this.radius = radius
        return this
    }


    /**
     * 设置不规则裁剪图形PNG资源
     */
    fun asIrregularShape(context: Context, @DrawableRes bitmapId: Int): CustomClipOption {
        irregularClip = BitmapFactory.decodeResource(context.resources, bitmapId)
        return this
    }

    /**
     * 设置不规则裁剪图形PNG资源
     */
    fun asIrregularShape(bitmap: Bitmap): CustomClipOption {
        irregularClip = bitmap
        return this
    }

    /**
     * 是否镂空区域事件穿透
     */
    fun setEventPassThrough(eventPassThrough: Boolean): CustomClipOption {
        this.eventPassThrough = eventPassThrough
        return this
    }

    /**
     * 构建裁剪的bitmap区域
     */
    override fun build(activity: Activity?) {
        buildDstSizeBitmap()
    }

    /**
     * 构建裁剪的bitmap区域
     */
    override fun build(activity: Fragment?) {
        buildDstSizeBitmap()
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

    // 初始化裁剪区域
    private fun initRect(parentWidth: Float, parentHeight: Float) {
        if (rectF != null || parentHeight != this.parentHeight || parentWidth != this.parentWidth) {
            var top = 0f
            var left = 0f
            left = if (alignX === SmartGuide.AlignX.ALIGN_RIGHT) {
                parentWidth - offsetX - clipWidth
            } else {
                offsetX
            }
            top = if (alignY === SmartGuide.AlignY.ALIGN_BOTTOM) {
                parentHeight - offsetY - clipHeight
            } else {
                offsetY
            }
            rectF = RectF(left, top, left + clipWidth, top + clipHeight)
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
        if (rectF == null || irregularClip == null) {
            return
        }
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawBitmap(irregularClip!!, rectF!!.left, rectF!!.top, paint)
        paint.xfermode = null
    }

    // 缩放不规则bitmap到裁剪区域大小
    private fun buildDstSizeBitmap() {
        if (irregularClip != null) {
            val bitmap = Bitmap.createScaledBitmap(irregularClip!!, clipWidth, clipHeight, true)
            if (!bitmap.isRecycled) {
                irregularClip = bitmap
            }
        }
    }

}