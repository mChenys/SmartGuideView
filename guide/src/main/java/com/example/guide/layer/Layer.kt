package com.example.guide.layer

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.app.Fragment
import com.example.guide.option.BaseClipOption
import com.example.guide.option.DescOption

/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc:  引导层,处理裁剪区和指示区
 */
class Layer(val tag: String) {
    private var clipHolder: ClipHolder = ClipHolder()
    private var descHolder: DescHolder = DescHolder()

    /**
     * 设置裁剪选项
     */
    fun setClipOption(option: BaseClipOption) {
        clipHolder.setTargetOption(option)
    }

    /**
     * 设置描述选项
     */
    fun setDescOption(option: DescOption) {
        descHolder.setTargetOption(option)
    }

    /**
     * 构建裁剪区域
     */
    fun build(activity: Activity?) {
        clipHolder.build(activity)
    }

    /**
     * 构建裁剪区域
     */
    fun build(fragment: Fragment?) {
        clipHolder.build(fragment)
    }

    /**
     * 是否可以透传点击事件
     */
    fun isClipEventPassThrough(): Boolean {
        return clipHolder.isEventPassThrough()
    }

    /**
     * 是否点击落在裁剪区域
     */
    fun isTouchInClip(x: Float, y: Float): Boolean {
        return clipHolder.getRectF()?.contains(x, y) ?: false
    }

    /**
     * 是否点击落在描述图片区域
     */
    fun isTouchInIntro(x: Float, y: Float): Boolean {
        return descHolder.getRectF()?.contains(x, y) ?: false
    }

    /**
     * 绘制裁剪区或者描述区域
     */
    fun draw(canvas: Canvas, paint: Paint, clip: Boolean, parentWidth: Float, parentHeight: Float) {
        if (clip) {
            clipHolder.draw(canvas, paint, null, parentWidth, parentHeight)
        } else {
            descHolder.draw(canvas, paint, clipHolder.getRectF(), parentWidth, parentHeight)
        }
    }
}