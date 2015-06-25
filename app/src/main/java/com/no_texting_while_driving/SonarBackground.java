package com.no_texting_while_driving;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SonarBackground extends SurfaceView implements Runnable {

    private Canvas mCanvas;
    private Paint paint;
    Thread ourThread = null;
    boolean isRunning = true;
    SurfaceHolder ourHolder;
    int centerX;
    int centerY;
    int radius = 1;
    long sleep = 1000;

    public SonarBackground(Context context) {
        super(context);

        paint = new Paint();
        ourHolder = getHolder();
        ourThread = new Thread(this);
        ourThread.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {


    }

    @Override
    public void run() {
        while (isRunning) {

            if (!ourHolder.getSurface().isValid())
                continue;
            else {
                try {
                    ourThread.sleep(sleep);
                } catch (Exception e) {
                    //do nothing
                }
                paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                paint.setColor(Color.GREEN);
                paint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.NORMAL));
                mCanvas = ourHolder.lockCanvas();
                radius += 50;
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;

                //mDrawable.setBounds(x, y, width,height);
                mCanvas.drawCircle(centerX, centerY, radius, paint);
                ourHolder.unlockCanvasAndPost(mCanvas);
                postInvalidate();
            }

        }
    }

    public void stopThread() {
        ourThread.stop();
    }
}
