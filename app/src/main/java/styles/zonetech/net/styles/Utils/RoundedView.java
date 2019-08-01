package styles.zonetech.net.styles.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class RoundedView extends LinearLayout {

    private FrameLayout mapView;
    private Bitmap windowFrame;
    private Context context;
    /**
     * Creates a Google Map View with rounded corners
     * Constructor when created in XML
     * @param context
     * @param attrs
     */
    public RoundedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    /**
     * Creates a Google Map View with rounded corners
     * Contructor when created in code
     * @param context
     */

    public RoundedView(Context context) {
        super(context);
        this.context=context;
        init();
    }

    private void init() {
        mapView = new FrameLayout(context);
        mapView.setLayoutParams(new LinearLayout.LayoutParams(0,200));

        addView(mapView);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas); // Call super first (this draws the map) we then draw on top of it

        if(windowFrame == null){
            createWindowFrame(); // Lazy creation of the window frame, this is needed as we don't know the width & height of the screen until draw time
        }

        canvas.drawBitmap(windowFrame, 0, 0, null);
    }

    protected void createWindowFrame() {
        windowFrame = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444); // Create a new image we will draw over the map
        Canvas osCanvas = new Canvas(windowFrame); // Create a   canvas to draw onto the new image

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());
        RectF innerRectangle = new RectF(0.1f, 0.1f, getWidth()-0.1f, getHeight()-0.1f);

        //float cornerRadius = getWidth() / 20f; // The angle of your corners
        float cornerRadius = dpTopixel(context,15);// The angle of your corners

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // Anti alias allows for smooth corners
        paint.setColor(Color.WHITE); // This is the color of your activity background
        osCanvas.drawRect(outerRectangle, paint);


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)); // A out B https://en.wikipedia.org/wiki/File:Alpha_compositing.svg
        osCanvas.drawRoundRect(innerRectangle, cornerRadius, cornerRadius, paint);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        windowFrame = null; // If the layout changes null our frame so it can be recreated with the new width and height
    }

    public static float dpTopixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;
        return pixel;
    }
}

