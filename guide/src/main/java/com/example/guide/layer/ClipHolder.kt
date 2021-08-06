package com.example.guide.layer

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.fragment.app.Fragment
import com.example.guide.option.BaseClipOption


/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 裁剪区域处理类
 */
class ClipHolder : BaseLayerHolder() {

    private var targetOps: BaseClipOption? = null

    fun setTargetOption(targetOps: BaseClipOption?) {
        this.targetOps = targetOps
    }

    override fun build(activity: Activity?) {
        targetOps?.build(activity)
    }

    override fun build(fragment: Fragment?) {
        targetOps?.build(fragment)
    }

    override fun draw(canvas: Canvas, paint: Paint, clipRectF: RectF?, parentWidth: Float, parentHeight: Float) {
        targetOps?.draw(canvas, paint, parentWidth, parentHeight);
    }

    fun getRectF(): RectF? {
        return targetOps?.rectF
    }

    fun isEventPassThrough(): Boolean {
        return targetOps?.eventPassThrough ?: false
    }
}