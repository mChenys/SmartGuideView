package com.example.guide

/**
 * 引导层点击事件监听器
 */
interface OnGuidClickListener {
    /**
     * 引导层销毁回调
     */
    fun destroyed() {}

    /**
     * 点击蒙层非裁剪和信息区域回调，返回true，直接退出引导，返回false则不退出
     */
    fun setOutsideTouchable(guide: SmartGuide): Boolean = true

    /**
     * 引导镂空区域点击回调，如果镂空区域设置了事件透传，则不回调
     */
    fun clipClicked(guide: SmartGuide, view: GuideView, tag: String) {}

    /**
     * 引导介绍区域点击回调
     */
    fun descClicked(guide: SmartGuide, view: GuideView, tag: String) {}
}