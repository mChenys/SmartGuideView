package com.example.guide.option

import android.content.Context
import android.graphics.*
import com.example.guide.SmartGuide
import android.support.annotation.DrawableRes

import kotlin.math.abs


/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 描述配置项
 */
class DescOption(private val context: Context) {
    private var alignX: SmartGuide.AlignX = SmartGuide.AlignX.ALIGN_RIGHT
    private var alignY: SmartGuide.AlignY = SmartGuide.AlignY.ALIGN_BOTTOM
    private var with: Int = 0
    private var height: Int = 0
    private var offsetX: Float = 0f
    private var offsetY: Float = 0f
    private var introBitmap: Bitmap? = null
    var rectF: RectF? = null

    companion object {
        fun newDescOption(context: Context): DescOption {
            return DescOption(context)
        }
    }

    /**
     * 设置描述图片偏移位置
     */
    fun setOffset(offsetX: Float, offsetY: Float): DescOption {
        this.offsetX = offsetX
        this.offsetY = offsetY
        return this
    }

    /**
     * 设置描述图片大小
     */
    fun setSize(width: Int, height: Int): DescOption {
        this.with = width
        this.height = height
        return this
    }

    /**
     * 设置描述图片对齐方式
     */
    fun setAlign(x: SmartGuide.AlignX, y: SmartGuide.AlignY): DescOption {
        alignX = x
        alignY = y
        return this
    }

    /**
     * 设置描述图片
     */
    fun setIntroBmp(bmp: Bitmap?): DescOption {
        introBitmap = bmp
        return this
    }

    /**
     * 设置描述图片
     */
    fun setIntroBmp(@DrawableRes bmpRes: Int): DescOption {
        introBitmap = BitmapFactory.decodeResource(context.resources, bmpRes)
        return this
    }

    /**
     * 绘制描述图片
     */
    fun draw(canvas: Canvas, paint: Paint?, clipRectF: RectF?, parentWidth: Float, parentHeight: Float) {
        if (clipRectF == null) {
            return
        }
        if (introBitmap == null) {
            return
        }
        if (rectF == null) {
            initRectF(clipRectF)
        }
        if (introBitmap != null) {
            if (abs(introBitmap!!.width - with) > 1 || abs(introBitmap!!.height - height) > 1) {
                // 缩放图片
                introBitmap = Bitmap.createScaledBitmap(introBitmap!!, with, height, true)
            }
            canvas.drawBitmap(introBitmap!!, rectF!!.left, rectF!!.top, paint)
        }

    }

    // 根据裁剪区域定位描述信息的位置
    private fun initRectF(clipRectF: RectF) {
        val left: Float = if (alignX === SmartGuide.AlignX.ALIGN_LEFT) {
            // 描述图片位于裁剪区域的左侧
            clipRectF.left - with - offsetX
        } else {
            // 描述图片位于裁剪区域的右侧
            clipRectF.right + offsetX
        }
        val top: Float = if (alignY === SmartGuide.AlignY.ALIGN_TOP) {
            // 描述图片位于裁剪区域的上边
            clipRectF.top - height - offsetY
        } else {
            // 描述图片位于裁剪区域的下边
            clipRectF.bottom + offsetY
        }
        rectF = RectF(left, top, left + with, top + height)
    }


}