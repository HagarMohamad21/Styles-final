package styles.zonetech.net.styles.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

public class RoundedDrawable extends Drawable {

    private final float radius;
    private final Bitmap bitmap;
    private final BitmapShader bitmapshader;
    private final Paint paint = new Paint();
    private final RectF shaderrect = new RectF();

    public RoundedDrawable(Bitmap bitmap, float density, float radius, int width, int height, boolean crop, Context context )
    {
        if(crop)
        {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap , (int) (width * density) , (int) (height * density));
        }
        this.bitmap = bitmap;
        //this.radius = dpTopixel(context,radius);
        this.radius = (radius * density + 0.5f);
        shaderrect.set(0 , 0 , width * density + 0.5f , height * density + 0.5f);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF() , shaderrect , Matrix.ScaleToFit.FILL);
        bitmapshader = new BitmapShader(bitmap , Shader.TileMode.CLAMP , Shader.TileMode.CLAMP);
        bitmapshader.setLocalMatrix(matrix);
        paint.setShader(bitmapshader);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawRoundRect(shaderrect , radius , radius , paint);
    }

    @Override
    public void setAlpha(int alpha)
    {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter)
    {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity()
    {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth()
    {
        return bitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight()
    {
        return bitmap.getHeight();
    }


    public static float dpTopixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;
        return pixel;
    }


}
