package com.example.guide.option

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.fragment.app.Fragment

/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 裁剪区域基类配置项
 */
abstract class BaseClipOption {
    protected var offsetX: Float = 0f // 水平偏移量x
    protected var offsetY: Float = 0f // 水平偏移量y
    protected var radius: Float = 0f // 裁剪区域半径
    protected var irregularClip: Bitmap? = null // 不规则图
    var rectF: RectF? = null // 裁剪区域
    var eventPassThrough = false // 透传事件

    /**
     * 构建裁剪区域
     */
    open fun build(activity: Activity?) {}
    open fun build(activity: Fragment?) {}

    /**
     * 绘制裁剪区域
     */
    abstract fun draw(canvas: Canvas, paint: Paint, parentWidth: Float, parentHeight: Float)

}