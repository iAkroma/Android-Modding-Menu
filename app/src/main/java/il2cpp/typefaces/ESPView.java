package il2cpp.typefaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import il2cpp.Main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ESPView extends View implements Runnable {
    Paint mStrokePaint;
    Paint mFilledPaint;
    Paint mTextPaint;
    Thread mThread;
    int FPS = 60;
    long sleepTime;
    Date time;
    SimpleDateFormat timeForm, dateForm;
    public native void SetTime(String value);
	public native void SetDate(String value);
	Context ctx;

	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(
			bm, 0, 0, width, height, matrix, false);
		bm.recycle();
		return resizedBitmap;
	}
	
    public ESPView(Context context) {
        super(context, null, 0);
		ctx = context;
        InitializePaints();
        setFocusableInTouchMode(false);
        setBackgroundColor(Color.TRANSPARENT);
        
		time = new Date();
        timeForm = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        dateForm = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sleepTime = 1000 / FPS;
        mThread = new Thread(this);
        mThread.start();
		
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (canvas != null && getVisibility() == VISIBLE) {
            
			ClearCanvas(canvas);
            Menu.DrawOn(this, canvas);
		}
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        while (mThread.isAlive() && !mThread.isInterrupted()) {
            try {
                long t1 = System.currentTimeMillis();
                postInvalidate();
                long td = System.currentTimeMillis() - t1;
                Thread.sleep(Math.max(Math.min(0, sleepTime - td), sleepTime));
            } catch (InterruptedException it) {
                Log.e("OverlayThread", it.getMessage());
            }
        }
    }

    public void InitializePaints() {
        mStrokePaint = new Paint();
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setColor(Color.rgb(0, 0, 0));

        mFilledPaint = new Paint();
        mFilledPaint.setStyle(Paint.Style.FILL);
        mFilledPaint.setAntiAlias(true);
        mFilledPaint.setColor(Color.rgb(0, 0, 0));

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.rgb(0, 0, 0));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStrokeWidth(1.1f);
    }

    public void ClearCanvas(Canvas cvs) {
        cvs.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    public void DrawLine(Canvas cvs, int a, int r, int g, int b, float lineWidth, float fromX, float fromY, float toX, float toY, boolean shadow) {
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(a);
        mStrokePaint.setStrokeWidth(lineWidth);
		if (shadow) mStrokePaint.setShadowLayer(6.0f, 0.0f, 0.0f, Color.argb(100, 0, 0, 0));
        cvs.drawLine(fromX, fromY, toX, toY, mStrokePaint);
    }
	
	public void DrawImage(Canvas cvs, String img, int x, int y, int width, int height) {
		byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
		Bitmap bpm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
		bpm = getResizedBitmap(bpm, width, height);
		cvs.drawBitmap(bpm, x, y, mStrokePaint);
	}

    public void DrawText(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size, boolean shadow) {
        mTextPaint.setColor(Color.rgb(r, g, b));
        mTextPaint.setAlpha(a);
		
        if (getRight() > 1920 || getBottom() > 1920) {
            if (shadow) {mTextPaint.setShadowLayer(3.0f, 0.0f, 0.0f, Color.rgb(0, 0, 0)); } else {mTextPaint.setShadowLayer(0.0f, 0.0f, 0.0f, Color.argb(0, 0, 0, 0));}
            mTextPaint.setTextSize(4 + size);
            }
        else if (getRight() == 1920 || getBottom() == 1920) {
            mTextPaint.setTextSize(2 + size);
            if (shadow) { mTextPaint.setShadowLayer(3.0f, 0.0f, 0.0f, Color.rgb(0, 0, 0)); } else {mTextPaint.setShadowLayer(0.0f, 0.0f, 0.0f, Color.argb(0, 0, 0, 0));}
            }
        else
            mTextPaint.setTextSize(size);
        if (shadow) {mTextPaint.setShadowLayer(3.0f, 0.0f, 0.0f, Color.rgb(0, 0, 0)); } else {mTextPaint.setShadowLayer(0.0f, 0.0f, 0.0f, Color.argb(0, 0, 0, 0));}
        cvs.drawText(txt, posX, posY, mTextPaint);
    }

    public void DrawCircle(Canvas cvs, int a, int r, int g, int b, float stroke, float posX, float posY, float radius) {
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(a);
        mStrokePaint.setStrokeWidth(stroke);
        cvs.drawCircle(posX, posY, radius, mStrokePaint);
    }

    public void DrawFilledCircle(Canvas cvs, int a, int r, int g, int b, float posX, float posY, float radius) {
        mFilledPaint.setColor(Color.rgb(r, g, b));
        mFilledPaint.setAlpha(a);
        cvs.drawCircle(posX, posY, radius, mFilledPaint);
    }

    public void DrawRect(Canvas cvs, int a, int r, int g, int b, int stroke, float x, float y, float x2, float y2, int radius, boolean shadow) {
        mStrokePaint.setStrokeWidth(stroke);
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(a);
		if (shadow) {mStrokePaint.setShadowLayer(6.0f, 0.0f, 0.0f, Color.argb(100, 0, 0, 0)); } else {mStrokePaint.setShadowLayer(0.0f, 0.0f, 0.0f, Color.argb(0, 0, 0, 0));}
        //cvs.drawRect(x, y, x2, y2, mStrokePaint);
		cvs.drawRoundRect(x ,y, x2, y2, radius, radius, mStrokePaint);
    }
	
    public void DrawFilledRect(Canvas cvs, int a, int r, int g, int b, float x, float y, float x2, float y2, int radius, boolean shadow) {
        mFilledPaint.setColor(Color.rgb(r, g, b));
        mFilledPaint.setAlpha(a);
		if (shadow) { mFilledPaint.setShadowLayer(6.0f, 0.0f, 0.0f, Color.argb(100, 0, 0, 0)); } else {mFilledPaint.setShadowLayer(0.0f, 0.0f, 0.0f, Color.argb(0, 0, 0, 0));}
        cvs.drawRoundRect(x, y, x2, y2, radius, radius, mFilledPaint);
    }
}
