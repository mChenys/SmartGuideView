package com.example.guide.layer

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.app.Fragment

/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 图层处理基类
 */
abstract class BaseLayerHolder {
    /**
     * 构建绘制区域
     */
    open fun build(activity: Activity?) {}
    open fun build(fragment: Fragment?) {}

    /**
     * 绘制内容
     */
    abstract fun draw(canvas: Canvas, paint: Paint, clipRectF: RectF?, parentWidth: Float, parentHeight: Float)
}