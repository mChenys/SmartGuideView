package com.example.guide.layer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.example.guide.option.DescOption

/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 新手指引描述处理类
 */
class DescHolder : BaseLayerHolder() {
    private var targetOps: DescOption? = null

    fun setTargetOption(targetOps: DescOption?) {
        this.targetOps = targetOps
    }

    /**
     * 绘制描述区域
     */
    override fun draw(canvas: Canvas, paint: Paint, clipRectF: RectF?, parentWidth: Float, parentHeight: Float) {
        targetOps?.draw(canvas, paint, clipRectF, parentWidth, parentHeight)
    }

    /**
     * 获取描述图片的区域
     */
    fun getRectF(): RectF? = targetOps?.rectF
}