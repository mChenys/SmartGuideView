package aiven.guide.view.layer;


import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import aiven.guide.view.clip.BaseClipPosition;
import aiven.guide.view.face.IntroPanel;
/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 引导层,处理裁剪区和指示区
 */
class Layer {
    @NonNull
    private ClipHolder clipHolder;
    @NonNull
    private FaceHolder faceHolder;
    @NonNull
    protected String tag;

    public Layer(String tag) {
        this.clipHolder = new ClipHolder();
        this.faceHolder = new FaceHolder();
        this.tag = tag;
    }


    public void setClipTarget(BaseClipPosition target){
        clipHolder.setTarget(target);
    }

    public void setFacePanel(IntroPanel facePanel){
        this.faceHolder.setFacePanel(facePanel);
    }

    /**
     * 图层绘制入口
     * @param canvas
     * @param paint
     * @param clip
     * @param parentWidth
     * @param parentHeight
     */
    public void draw(Canvas canvas, Paint paint, boolean clip, float parentWidth, float parentHeight){
        if(clip){
            clipHolder.draw(canvas,paint,null,parentWidth,parentHeight);
        }else{
            faceHolder.draw(canvas,paint, clipHolder.getRectF(),parentWidth,parentHeight);
        }
    }

    protected void build(@Nullable Activity activity) {
        if(clipHolder != null){
            clipHolder.build(activity);
        }
        if(faceHolder != null){
            faceHolder.build(activity);
        }
    }


    protected void build(@Nullable Fragment fragment) {
        if(clipHolder != null){
            clipHolder.build(fragment);
        }
        if(faceHolder != null){
            faceHolder.build(fragment);
        }
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public boolean isClipEventPassThrough(){
        if(clipHolder != null){
            return clipHolder.isEventPassThrough();
        }
        return false;
    }


    public boolean isTouchInClip(float x,float y){
        if(clipHolder != null && clipHolder.getRectF() != null){
            if(clipHolder.getRectF().contains(x,y)){
                return true;
            }
        }
        return false;
    }

    public boolean isTouchInIntro(float x,float y){
        if(faceHolder != null && faceHolder.getRectF() != null){
            if(faceHolder.getRectF().contains(x,y)){
                return true;
            }
        }
        return false;
    }
}
