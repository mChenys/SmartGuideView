package aiven.guide.view.base;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Author: ChenYouSheng
 * Date: 2021/8/5
 * Email: chenyousheng@lizhi.fm
 * Desc: 图层基类
 */
public abstract class LayerBaseHolder {

    abstract public void build(@Nullable Activity activity);
    abstract public void build(@Nullable Fragment fragment);

    abstract public void draw(Canvas canvas, Paint paint,@Nullable RectF clipRectF, float parentWidth, float parentHeight);
}
