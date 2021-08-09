package aiven.guide.view.demo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.guide.GuideView
import com.example.guide.OnGuidClickListener
import com.example.guide.SmartGuide
import com.example.guide.option.CustomClipOption
import com.example.guide.option.DescOption

/**
 * Author: ChenYouSheng
 * Date: 2021/8/9
 * Email: chenyousheng@lizhi.fm
 * Desc:
 */
class Main2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_main2)

        SmartGuide.newGuide(this, guideHeight = dip2px(260f))
            .setLinearGradientBackgroundColor(Color.parseColor("#B3000000"),Color.parseColor("#00000000"))
            .newLayer("textChannel")
            .buildCustClipOption(object : SmartGuide.ClipOptionBuilder<CustomClipOption> {
                override fun buildOption(): CustomClipOption {
                    return CustomClipOption.newClipOption()
                        .setAlignX(SmartGuide.AlignX.ALIGN_LEFT)
                        .setAlignY(SmartGuide.AlignY.ALIGN_TOP)
                        .setOffsetX(dip2px(13f).toFloat())
                        .setOffsetY(dip2px(44f).toFloat())
                        .setClipSize(dip2px(44f), dip2px(44f))
                        .clipRadius(dip2px(22f).toFloat())
                }
            })
            .buildDescOption(object : SmartGuide.DescOptionBuilder {
                override fun buildOption(): DescOption {
                    return DescOption.newDescOption(applicationContext) //设置介绍图片与clipInfo的对齐信息
                        .setIntroBmp(R.drawable.ic_desc)
                        .setAlign(SmartGuide.AlignX.ALIGN_RIGHT, SmartGuide.AlignY.ALIGN_TOP)
                        .setSize(dip2px(227f), dip2px(64f))
                        .setOffset(dip2px(101f).toFloat(), dip2px(46f).toFloat())
                }
            })
            .buildChildView(object : SmartGuide.ViewBuilder {
                override fun buildChildView(parent: GuideView): View {
                    val lottie = LottieAnimationView(parent.context)
                    lottie.layoutParams = ViewGroup.MarginLayoutParams(dip2px(50f), dip2px(50f)).apply {
                        topMargin = dip2px(66f)
                        leftMargin = dip2px(42f)
                    }
                    lottie.setAnimation("data.json")
                    lottie.imageAssetsFolder = "images"
                    lottie.loop(true)
                    lottie.playAnimation()
                    return lottie
                }
            })
            .setOnGuidClickListener(object : OnGuidClickListener {
                override fun setOutsideTouchable(guide: SmartGuide): Boolean = false

                override fun clipClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    Toast.makeText(applicationContext, "点击裁剪区域", Toast.LENGTH_SHORT).show()
                }

                override fun descClicked(guide: SmartGuide, view: GuideView, tag: String) {
                    Toast.makeText(applicationContext, "点击描述区域", Toast.LENGTH_SHORT).show()
                }
            })
            .show()
    }

    fun dip2px(dipValue: Float): Int {
        val scale: Float = applicationContext.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}