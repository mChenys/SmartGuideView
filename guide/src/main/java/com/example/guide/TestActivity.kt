package com.example.guide

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.WrapperListAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView
import com.example.guide.option.CustomClipOption
import com.example.guide.option.DescOption

import com.example.guide.option.ViewClipOption
import java.lang.Exception


/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc:
 */
class TestActivity : AppCompatActivity() {
    companion object {
        const val TAG_USER_HEADER = "userHeadImg"
        const val TAG_MUSIC_IMG = "music_img"
        const val TAG_IGG_SHAPE = "igg_shape"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = "kotlin测试版本"
    }

    /**
     * 通过绝对定位添加
     * @param view
     */
    fun showAbsPosLayer(view: View?) {
        //构建引导
        SmartGuide.newGuide(this)
            .initBaseColor(0x80000000.toInt()) //设置引蒙层背景颜色
            //新建一个引导
            .newLayer(TAG_USER_HEADER) //创建一个镂空区域
            .buildCustClipOption(object : SmartGuide.ClipOptionBuilder<CustomClipOption> {
                override fun buildOption(): CustomClipOption {
                    //构建镂空区域图形，支持CustomClip 和 ViewClipOption
                    return CustomClipOption.newClipOption() //设置异形图片(实现见第三个按钮)
                                                       // .asIrregularShape(applicationContext,R.mipmap.test_img)
                        .setAlignX(SmartGuide.AlignX.ALIGN_RIGHT) //设置定位水平定位偏向
                        .setAlignY(SmartGuide.AlignY.ALIGN_TOP) //设置定位垂直定位偏向
                        //.setEventPassThrough(true) //镂空区域是否事件穿透
                        .setOffsetX(
                            dip2px(14f).toFloat()
                        ) //根据水平定位偏向设置偏移，如果未ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
                        .setOffsetY(
                            (getStatusBarHeight() + dip2px(4f)).toFloat()
                        ) //设置镂空裁剪区域尺寸
                        .setClipSize(dip2px(48f), dip2px(48f))
                        .clipRadius(dip2px(24f).toFloat())
                }
            })
            .buildDescOption(object : SmartGuide.DescOptionBuilder {
                override fun buildOption(): DescOption {
                    return DescOption.newDescOption(applicationContext) //设置介绍图片与clipInfo的对齐信息
                        .setIntroBmp(R.mipmap.test_face)
                        .setAlign(SmartGuide.AlignX.ALIGN_LEFT, SmartGuide.AlignY.ALIGN_BOTTOM)
                        .setSize(dip2px(151f), dip2px(97f))
                        .setOffset(dip2px(-20f).toFloat(), 0f)
                }
            })
            .buildChildView(object : SmartGuide.ViewBuilder {
                override fun buildChildView(parent: GuideView): View {
                    val lottie = LottieAnimationView(parent.context)
                    lottie.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                        topMargin = 300
                        leftMargin = 50
                    }
                    lottie.setAnimation("robot_login.json")
                    lottie.loop(true)
                    lottie.playAnimation()
                    return lottie
                }
            })
            .setOnGuidClickListener(object : OnGuidClickListener {
                override fun emptyClicked(smartGuide: SmartGuide): Boolean { //点击蒙层空白区域
                    return true //返回true，引导消失，false不消失
                }

                override fun clipClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    //如果设置了setEventPassThrough 为true，这里这个方法将不会回调
                    Toast.makeText(applicationContext, "点击了右上角裁剪区域", Toast.LENGTH_SHORT).show()
                }

                override fun descClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    //点击文字区域
                    Toast.makeText(applicationContext, "点击了右上角裁剪区域的说明图片引导", Toast.LENGTH_SHORT).show()
                }
            })
            .show()
    }


    /**
     * 根据View 自身位置定位
     * @param view
     */
    fun showViewPosLayer(view: View?) {
        //构建引导
        SmartGuide.newGuide(this)
            .initBaseColor(0x80000000.toInt()) //设置引蒙层背景颜色
            //新建一个引导
            .newLayer(TAG_MUSIC_IMG) //创建一个镂空区域
            .buildViewClipOption(object : SmartGuide.ClipOptionBuilder<ViewClipOption> {
                override fun buildOption(): ViewClipOption {
                    return ViewClipOption.newClipOption()
                        .setDstView(R.id.text_pos)
                        .setPadding(dip2px(5f).toFloat())
                        .clipRadius(dip2px(51f).toFloat())
                }
            })
            .buildDescOption(object : SmartGuide.DescOptionBuilder {
                override fun buildOption(): DescOption {
                    return DescOption.newDescOption(applicationContext) //设置介绍图片与clipInfo的对齐信息
                        .setIntroBmp(R.mipmap.test_face_music)
                        .setAlign(SmartGuide.AlignX.ALIGN_LEFT, SmartGuide.AlignY.ALIGN_TOP)
                        .setSize(dip2px(120f), dip2px(120f))
                        .setOffset(dip2px(-100f).toFloat(), 0f)
                }
            })
            .setOnGuidClickListener(object : OnGuidClickListener {
                override fun emptyClicked(smartGuide: SmartGuide): Boolean {
                    return true
                }

                override fun clipClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    Toast.makeText(applicationContext, "点击了紫色音乐图标裁剪区域", Toast.LENGTH_SHORT).show()
                }

                override fun descClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    Toast.makeText(applicationContext, "点击了紫色音乐图标介绍图片区域", Toast.LENGTH_SHORT).show()
                }
            })
            .show()
    }


    /**
     *
     * 显示不规则异形镂空引导
     * @param view
     */
    fun showIgg(view: View?) {
        SmartGuide.newGuide(this)
            .initBaseColor(0x80000000.toInt()) //设置引蒙层背景颜色
            //新建一个引导
            .newLayer(TAG_IGG_SHAPE) //创建一个镂空区域
            .buildViewClipOption(object : SmartGuide.ClipOptionBuilder<ViewClipOption> {
                override fun buildOption(): ViewClipOption {
                    return ViewClipOption.newClipOption()
                        .setDstView(R.id.text_pos2) //设置异形图片
                        .asIrregularShape(applicationContext, R.mipmap.test_img)
                        .setPadding(dip2px(10f).toFloat())
                        .setOffsetX(dip2px(-5f).toFloat())
                        .setOffsetY(dip2px(-5f).toFloat())
                }
            })
            .buildDescOption(object : SmartGuide.DescOptionBuilder {
                override fun buildOption(): DescOption {
                    return DescOption.newDescOption(applicationContext) //设置介绍图片与clipInfo的对齐信息
                        .setIntroBmp(R.mipmap.test_face_igg)
                        .setAlign(SmartGuide.AlignX.ALIGN_RIGHT, SmartGuide.AlignY.ALIGN_TOP)
                        .setSize(dip2px(151f), dip2px(151f))
                }
            })
            .setOnGuidClickListener(object : OnGuidClickListener {
                override fun emptyClicked(smartGuide: SmartGuide): Boolean {
                    return true
                }

                override fun clipClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    Toast.makeText(applicationContext, "点击了不规则图形镂空区域", Toast.LENGTH_SHORT).show()
                }

                override fun descClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    Toast.makeText(applicationContext, "点击了不规则图形图片说明区域", Toast.LENGTH_SHORT).show()
                }
            })
            .show()
    }


    /**
     * 单屏显示多个layer
     * @param view
     */
    fun showMultLayer(view: View?) {
        SmartGuide.newGuide(this)
            .initBaseColor(0x80000000.toInt()) //设置引蒙层背景颜色
            //新建一个引导
            .newLayer(TAG_USER_HEADER) //创建一个镂空区域
            .buildCustClipOption(object : SmartGuide.ClipOptionBuilder<CustomClipOption> {
                override fun buildOption(): CustomClipOption {
                    //构建镂空区域图形，支持CustomClip 和 ViewClipOption
                    return CustomClipOption.newClipOption()
                        .setAlignX(SmartGuide.AlignX.ALIGN_RIGHT) //设置定位水平定位偏向
                        .setAlignY(SmartGuide.AlignY.ALIGN_TOP) //设置定位垂直定位偏向
                        .setOffsetX(
                            dip2px(14f).toFloat()
                        ) //根据水平定位偏向设置偏移，如果未ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
                        .setOffsetY(
                            (getStatusBarHeight() + dip2px(4f)).toFloat()
                        ) //设置镂空裁剪区域尺寸
                        .setClipSize(dip2px(48f), dip2px(48f))
                        .clipRadius(dip2px(24f).toFloat())
                }
            })
            .buildDescOption(object : SmartGuide.DescOptionBuilder {
                override fun buildOption(): DescOption {
                    return DescOption.newDescOption(applicationContext) //设置介绍图片与clipInfo的对齐信息
                        .setIntroBmp(R.mipmap.test_face)
                        .setAlign(SmartGuide.AlignX.ALIGN_LEFT, SmartGuide.AlignY.ALIGN_BOTTOM)
                        .setSize(dip2px(151f), dip2px(97f))
                        .setOffset(dip2px(-20f).toFloat(), 0f)
                }
            })
            .over() //多个newLayer必须用over作为上一个newLayer的结束连接
            .newLayer(TAG_MUSIC_IMG) //创建一个镂空区域
            .buildViewClipOption(object : SmartGuide.ClipOptionBuilder<ViewClipOption> {
                override fun buildOption(): ViewClipOption {
                    return ViewClipOption.newClipOption()
                        .setDstView(R.id.text_pos)
                        .setPadding(dip2px(5f).toFloat())
                        .clipRadius(dip2px(51f).toFloat())
                }
            })
            .buildDescOption(object : SmartGuide.DescOptionBuilder {
                override fun buildOption(): DescOption {
                    return DescOption.newDescOption(applicationContext) //设置介绍图片与clipInfo的对齐信息
                        .setIntroBmp(R.mipmap.test_face_music)
                        .setAlign(SmartGuide.AlignX.ALIGN_LEFT, SmartGuide.AlignY.ALIGN_TOP)
                        .setSize(dip2px(120f), dip2px(120f))
                        .setOffset(dip2px(-100f).toFloat(), 0f)
                }
            })
            .setOnGuidClickListener(object : OnGuidClickListener {
                override fun emptyClicked(smartGuide: SmartGuide): Boolean {
                    return true
                }

                override fun clipClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    if (TAG_USER_HEADER == tag) {
                        Toast.makeText(applicationContext, "点击了左上角头像裁剪区域", Toast.LENGTH_SHORT).show()
                    } else if (TAG_MUSIC_IMG == tag) {
                        Toast.makeText(applicationContext, "点击了紫色音乐图标裁剪区域", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun descClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    if (TAG_USER_HEADER == tag) {
                        Toast.makeText(applicationContext, "点击了左上角头像图片介绍区域", Toast.LENGTH_SHORT).show()
                    } else if (TAG_MUSIC_IMG == tag) {
                        Toast.makeText(applicationContext, "点击了紫色音乐图片介绍区域", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            .show()
    }


    /**
     * 多步骤切换展示
     * @param view
     */
    fun releaseTest(view: View?) {
        SmartGuide.newGuide(this)
            .initBaseColor(0x80000000.toInt()) //设置引蒙层背景颜色
            //新建一个引导
            .newLayer(TAG_USER_HEADER) //创建一个镂空区域
            .buildCustClipOption(object : SmartGuide.ClipOptionBuilder<CustomClipOption> {
                override fun buildOption(): CustomClipOption {
                    //构建镂空区域图形，支持CustomClip 和 ViewClipOption
                    return CustomClipOption.newClipOption() //设置异形图片(实现见第三个按钮)
                        //                                .asIrregularShape(getApplicationContext(),R.mipmap.test_img)
                        .setAlignX(SmartGuide.AlignX.ALIGN_RIGHT) //设置定位水平定位偏向
                        .setAlignY(SmartGuide.AlignY.ALIGN_TOP) //设置定位垂直定位偏向
                        .setOffsetX(
                            dip2px(14f).toFloat()
                        ) //根据水平定位偏向设置偏移，如果未ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
                        .setOffsetY(
                            (getStatusBarHeight() + dip2px(4f)).toFloat()
                        ) //设置镂空裁剪区域尺寸
                        .setClipSize(dip2px(48f), dip2px(48f))
                        .clipRadius(dip2px(24f).toFloat())
                }
            })
            .buildDescOption(object : SmartGuide.DescOptionBuilder {
                override fun buildOption(): DescOption {
                    return DescOption.newDescOption(applicationContext) //设置介绍图片与clipInfo的对齐信息
                        .setIntroBmp(R.mipmap.test_face)
                        .setAlign(SmartGuide.AlignX.ALIGN_LEFT, SmartGuide.AlignY.ALIGN_BOTTOM)
                        .setSize(dip2px(151f), dip2px(97f))
                        .setOffset(dip2px(-20f).toFloat(), 0f)
                }
            })
            .setOnGuidClickListener(object : OnGuidClickListener {
                override fun emptyClicked(smartGuide: SmartGuide): Boolean { //点击蒙层空白区域
                    return false //返回true，引导消失，false不消失
                }

                override fun descClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    // 显示下一个步骤
                    showStep2(guide)
                }
            })
            .show()
    }

    // 显示下一个步骤
    private fun showStep2(guide: SmartGuide) {
        guide.clearLayers()
        guide.newLayer(TAG_MUSIC_IMG) //创建一个镂空区域
            .buildViewClipOption(object : SmartGuide.ClipOptionBuilder<ViewClipOption> {
                override fun buildOption(): ViewClipOption {
                    return ViewClipOption.newClipOption()
                        .setDstView(R.id.text_pos)
                        .setPadding(dip2px(5f).toFloat())
                        .clipRadius(dip2px(51f).toFloat())
                }
            })
            .buildDescOption(object : SmartGuide.DescOptionBuilder {
                override fun buildOption(): DescOption {
                    return DescOption.newDescOption(applicationContext) //设置介绍图片与clipInfo的对齐信息
                        .setIntroBmp(R.mipmap.test_face_music)
                        .setAlign(SmartGuide.AlignX.ALIGN_LEFT, SmartGuide.AlignY.ALIGN_TOP)
                        .setSize(dip2px(120f), dip2px(120f))
                        .setOffset(dip2px(-100f).toFloat(), 0f)
                }
            })
            .setOnGuidClickListener(object : OnGuidClickListener {

                override fun emptyClicked(smartGuide: SmartGuide): Boolean {
                    return true
                }

                override fun clipClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    Toast.makeText(applicationContext, "点击了紫色音乐图标裁剪区域", Toast.LENGTH_SHORT).show()
                }

                override fun descClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    Toast.makeText(applicationContext, "点击了紫色音乐图标介绍图片区域", Toast.LENGTH_SHORT).show()
                }
            })
            .show()
    }


    fun dip2px(dipValue: Float): Int {
        val scale: Float = applicationContext.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(): Int {
        return try {
            // 获得状态栏高度
            val resourceId = applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
            applicationContext.resources.getDimensionPixelSize(resourceId)
        } catch (e: Exception) {
            dip2px(24f)
        }
    }


}