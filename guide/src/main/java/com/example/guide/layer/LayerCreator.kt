package com.example.guide.layer

import com.example.guide.GuideView
import com.example.guide.OnGuidClickListener
import com.example.guide.SmartGuide
import com.example.guide.option.CustomClipOption
import com.example.guide.option.ViewClipOption


/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 图层构建类
 */
class LayerCreator(var mView: GuideView, var smartGuide: SmartGuide, var tag: String) : GuideView.InnerOnGuidClickListener {
    private var currentLayer: Layer
    private var mListener: OnGuidClickListener? = null

    companion object {
        fun newCreator(guidView: GuideView, guide: SmartGuide, tag: String): LayerCreator {
            return LayerCreator(guidView, guide, tag)
        }
    }

    init {
        mView.setOnInnerOnGuidClickListener(this)
        currentLayer = Layer(tag)
    }

    /**
     * 添加点击事件
     */
    fun setOnGuidClickListener(listener: OnGuidClickListener?): LayerCreator {
        mListener = listener
        return this
    }

    /**
     * 根据VIEW 所在区域定位裁剪区域位置
     */
    fun buildViewClipOption(clipBuilder: SmartGuide.ClipOptionBuilder<ViewClipOption>): LayerCreator {
        currentLayer.setClipOption(clipBuilder.buildOption())
        return this
    }


    /**
     * 自定义裁剪区域位置（全屏定位）
     */
    fun buildCustClipOption(clipBuilder: SmartGuide.ClipOptionBuilder<CustomClipOption>): LayerCreator {
        currentLayer.setClipOption(clipBuilder.buildOption())
        return this
    }


    /**
     * 设置一个引导说明图形
     */
    fun buildDescOption(builder: SmartGuide.DescOptionBuilder): LayerCreator {
        currentLayer.setDescOption(builder.buildOption())
        return this
    }

    /**
     * 构建子View,可用于在蒙层上添加额外元素
     */
    fun buildChildView(builder: SmartGuide.ViewBuilder): LayerCreator {
        mView.addView(builder.buildChildView(mView))
        return this;
    }


    /**
     * 显示引导层
     */
    fun show() {
        over()
        smartGuide.show()
    }

    /**
     * 结束图层配置
     */
    fun over(): SmartGuide {
        mView.addLayer(currentLayer)
        return smartGuide
    }

    // 点击会回调
    override fun destroyed() {
        mListener?.destroyed()
    }

    override fun emptyClicked(): Boolean {
        return mListener?.setOutsideTouchable(smartGuide) ?: false
    }

    override fun clipClicked(tag: String) {
        mListener?.clipClicked(smartGuide, mView, tag)
    }

    override fun descClicked(tag: String) {
        mListener?.descClicked(smartGuide, mView, tag);
    }

}