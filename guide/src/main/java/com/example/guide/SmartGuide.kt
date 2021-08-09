package com.example.guide

import android.app.Activity
import android.content.Context
import com.example.guide.layer.LayerCreator
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import java.lang.StringBuilder

import com.example.guide.option.BaseClipOption
import com.example.guide.option.DescOption


/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc:
 */
class SmartGuide private constructor(context: Context) {
    // 垂直对齐方式
    sealed class AlignY {
        object ALIGN_TOP : AlignY()
        object ALIGN_BOTTOM : AlignY()
    }

    // 水平对齐方式
    sealed class AlignX {
        object ALIGN_LEFT : AlignX()
        object ALIGN_RIGHT : AlignX()
    }

    /**
     * 构建裁剪配置项接口
     */
    interface ClipOptionBuilder<out T : BaseClipOption> {
        fun buildOption(): T
    }

    /**
     * 构建描述配置项接口
     */
    interface DescOptionBuilder {
        fun buildOption(): DescOption
    }

    /**
     * 构建子View
     */
    interface ViewBuilder {
        fun buildChildView(parent: GuideView): View
    }

    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private val mView: GuideView = GuideView(context)

    /**
     * 引导层的宽高
     */
    private var guideWidth: Int = FrameLayout.LayoutParams.MATCH_PARENT
    private var guideHeight: Int = FrameLayout.LayoutParams.MATCH_PARENT

    companion object {
        fun newGuide(
            activity: Activity,
            guideWidth: Int = FrameLayout.LayoutParams.MATCH_PARENT,
            guideHeight: Int = FrameLayout.LayoutParams.MATCH_PARENT
        ): SmartGuide {
            return SmartGuide(activity).apply {
                this.mActivity = activity
                this.guideWidth = guideWidth
                this.guideHeight = guideHeight
            }
        }

        fun newGuide(
            fragment: Fragment,
            guideWidth: Int = FrameLayout.LayoutParams.MATCH_PARENT,
            guideHeight: Int = FrameLayout.LayoutParams.MATCH_PARENT
        ): SmartGuide {
            return SmartGuide(fragment.requireActivity()).apply {
                this.mFragment = fragment
                this.guideWidth = guideWidth
                this.guideHeight = guideHeight
            }
        }
    }

    /**
     * layer图层背景色
     */
    fun setBackgroundColor(color: Int): SmartGuide {
        mView.setBackgroundColor(color)
        return this
    }

    /**
     * 设置渐变背景色
     */
    fun setLinearGradientBackgroundColor(@ColorInt beginColor: Int, @ColorInt endColor: Int): SmartGuide {
        mView.setLinearGradientBackgroundColor(beginColor, endColor)
        return this
    }


    /**
     * 新建一个引导
     */
    fun newLayer(): LayerCreator {
        return LayerCreator.newCreator(mView, this, createTag())
    }

    /**
     * 新建一个引导
     */
    fun newLayer(tag: String): LayerCreator {
        var tag = tag
        if (TextUtils.isEmpty(tag)) {
            tag = createTag()
        }
        return LayerCreator.newCreator(mView, this, tag)
    }

    /**
     * 显示
     */
    fun show() {
        mView.layoutParams = FrameLayout.LayoutParams(guideWidth, guideHeight)
        if (mActivity != null) {
            mView.build(mActivity!!)
        } else if (mFragment != null) {
            mView.build(mFragment!!)
        }
    }

    /**
     * 清空图层
     */
    fun clearLayers() {
        mView.clearLayers()
    }

    /**
     * 根据tag删除图层
     */
    fun removeLayerByTag(tag: String?) {
        mView.removeLayerByTag(tag!!)
    }

    /**
     * 隐藏
     */
    fun dismiss() {
        mView.dismiss()
    }


    // 构建唯一的图层tag
    private fun createTag(): String {
        val builder = StringBuilder()
        builder.append("layer_")
            .append(System.currentTimeMillis())
            .append((Math.random() * 99999).toInt())
            .append("_")
            .append((Math.random() * 99999).toInt())
        return builder.toString()
    }

}