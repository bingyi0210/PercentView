package com.dengyun.jiawei.percentview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @title
 * @desc Created by liujiawei on 2018/3/19.
 */

public class PercentView extends View {
    private Paint mPaint;
    private Paint mBgPaint;
    private Paint btnGgPaint;
    private Paint btnTextPaint;
    private int mCenterX,mCenterY;
    private float mRadius =500;//外层区域
    private float mCirRaius =400;//圆半径
    private float mRectWidth =500;//按键背景宽度
    private String percentText ="60分";
    private String btnText ="一键加速";
    private float fontSize =250;// 分数字体大小
    private float fontSize_btn =80;//按键字体大小
    private int percent =60;//分数大小
    private Region globalRegion, btnRegion;
    private OnBtnClickListener listener;
    private Matrix matrix;
    private Context context;
    private  float fontWidth;//分数字体宽度

    public PercentView(Context context) {
        this(context,null);
    }

    public PercentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context  = context;
        matrix = new Matrix();
        //分数画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //背景画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.parseColor("#FF0ABBEC"));
        //按键文字画笔
        btnTextPaint = new Paint();
        btnTextPaint.setColor(Color.parseColor("#FF0ABBEC"));
        //按键背景画笔
        btnGgPaint = new Paint();
        btnGgPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w/2;
        mCenterY = h/2;
        globalRegion = new Region(-w/2,-h/2,w/2,h/2);
        btnRegion  = new Region();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mCenterX,mCenterY);
        if (matrix.isIdentity()){
            canvas.getMatrix().invert(matrix);
        }
        //绘制背景
        RectF rectF = new RectF(-mRadius,-mRadius,mRadius,mRadius);
        canvas.drawRect(rectF,mBgPaint);

        // 绘制分度盘
        for (int i=0;i<100;i++) {
            int x = (int) (mCirRaius * cos(i * 3.6f * Math.PI / 180-Math.PI /2));
            int y = (int) (mCirRaius * sin(i * 3.6f * Math.PI / 180-Math.PI /2));
            if (i<=percent){
                mPaint.setColor(Color.WHITE);
            }else {
                mPaint.setColor(Color.GRAY);
            }
            canvas.drawCircle(x, y, 5, mPaint);

        }
        //绘制分数
        percentText = percent + "";
        Paint.FontMetrics metrics = new Paint.FontMetrics();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(fontSize);
        fontWidth = mPaint.measureText(percentText);
        float fenWidth = btnTextPaint.measureText("分");
        float totalWidth = fontWidth + fenWidth;
        canvas.drawText(percentText, -totalWidth / 2, 0, mPaint);
        btnTextPaint.setColor(Color.WHITE);
        btnTextPaint.setTextSize(fontSize_btn);
        canvas.drawText("分",totalWidth/2-fenWidth,0,btnTextPaint);
        btnTextPaint.setColor(Color.parseColor("#FF0ABBEC"));
        mPaint.setTextSize(fontSize_btn);
        //绘制按键
        RectF btnRect = new RectF(-mRectWidth/2,50,mRectWidth/2,200);
        Path path  = new Path();
        path.addRoundRect(btnRect,75,75, Path.Direction.CW);
        btnRegion.setPath(path,globalRegion);//绑定按键区域

        canvas.drawPath(path,btnGgPaint);
        fontWidth = btnTextPaint.measureText(btnText);
        canvas.drawText(btnText,-fontWidth/2,155,btnTextPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //设置区域按键监听
        float pts[] = new float[2];
        pts[0] =  event.getX();
        pts[1] =   event.getY();
        matrix.mapPoints(pts);
        int Px = (int) pts[0];
        int Py = (int) pts[1];
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                if (btnRegion.contains(Px,Py)){
                    btnGgPaint.setColor(Color.GRAY);
                    btnTextPaint.setColor(Color.WHITE);
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (btnRegion.contains(Px,Py)){
                    listener.onPercentUp();
                    btnGgPaint.setColor(Color.WHITE);
                    btnTextPaint.setColor(Color.parseColor("#FF0ABBEC"));
                    invalidate();
                }

                break;
        }
        return true;
    }
    //动态改变分度值
    public void changePercent(int i) {
        percent = i;
        invalidate();
    }

    public interface OnBtnClickListener{
          void  onPercentUp();
    }
    public void setOnBtnClickListener(OnBtnClickListener listener){
        this.listener = listener;
    }
}
