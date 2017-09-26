package com.gly.quickgoods.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import gly.quickgoods.R;

/**
 * Created by gly on 2017/9/14.
 */

@SuppressLint("AppCompatCustomView")
public class DrawableLeftText extends View {
    private final Rect myTextBound;
    private final Rect rect;
    private final Paint myPaint;
    private int myImageHeight;
    private int myImageWidth;
    private int myImageScaleType;
    private Bitmap myImageSrc;
    private int myTextAndImgPadding;
    private int myTextColor;
    private String myText = "";
    private int myTextSize;
    private int myWidth;
    private int myHeight;
    private int IMAGE_SCALE_FILLXY = 1;
    private int IMAGE_SCALE_CENTER = 0;

    public DrawableLeftText(Context context) {
        this(context, null);
    }

    public DrawableLeftText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableLeftText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DrawableLeftText, defStyleAttr, 0);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);//这步类似取出Map集合的key值
            switch (attr) {//然后根据key值去取出相应的value
                case R.styleable.DrawableLeftText_myText:
                    myText = array.getString(attr);
                    break;
                case R.styleable.DrawableLeftText_myTextSize:
                    myTextSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DrawableLeftText_myTextColor:
                    myTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DrawableLeftText_myTextAndImgPadding:
                    myTextAndImgPadding = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DrawableLeftText_myImageSrc:
                    myImageSrc = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    break;
                case R.styleable.DrawableLeftText_myImageScaleType:
                    myImageScaleType = array.getInt(attr, 0);
                    break;
                case R.styleable.DrawableLeftText_myImageWidth:
                    myImageWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DrawableLeftText_myImageHeight:
                    myImageHeight = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        array.recycle();
        rect = new Rect();
        myTextBound = new Rect();
        myPaint = new Paint();
        myPaint.setTextSize(myTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制整个组件的边框即外围边框
//        myPaint.setStrokeWidth(4);
//        myPaint.setStyle(Paint.Style.STROKE);
//        myPaint.setColor(Color.RED);
//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), myPaint);
        rect.left = getPaddingLeft();
        rect.right = myWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = myHeight - getPaddingBottom();
        myPaint.setColor(myTextColor);
        myPaint.setTypeface(Typeface.defaultFromStyle(android.R.style.TextAppearance));
        myPaint.setStyle(Paint.Style.FILL);
        int textX = (myWidth - (myImageWidth + myTextAndImgPadding + myTextBound.width())) / 2 + (myImageWidth + myTextAndImgPadding);
        int textY = myTextBound.height() + (myHeight - myTextBound.height()) / 2;
        if (myTextBound.width() > myWidth) {
            TextPaint paint = new TextPaint(myPaint);
            System.out.println("over-------------------->");
            String msg = TextUtils.ellipsize(myText, paint, myWidth - getPaddingLeft() - myImageWidth - myTextAndImgPadding, TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, textX, textY, myPaint);
        } else {
            System.out.println("not over--------------------->");
            canvas.drawText(myText, textX, textY, myPaint);
        }
        rect.right -= (myTextBound.width() + myTextAndImgPadding + ((myWidth - (myImageWidth + myTextAndImgPadding + myTextBound.width())) / 2)) + getPaddingRight();
        if (myImageScaleType == IMAGE_SCALE_FILLXY) {
            canvas.drawBitmap(myImageSrc, null, rect, myPaint);
            System.out.println("scale_xy");
        } else if (myImageScaleType == IMAGE_SCALE_CENTER) {
            rect.top = myHeight / 2 - myImageHeight / 2;
            rect.bottom = myImageHeight + (myHeight - myImageHeight) / 2;
            rect.left = (myWidth - (myImageWidth + myTextAndImgPadding + myTextBound.width())) / 2;
            canvas.drawBitmap(myImageSrc, null, rect, myPaint);
        }
    }

    /**
     * @author zhongqihong
     * 重写setMyText()方法
     * invalidate()方法的调用主要是用来刷新View的，必须在主线程(UI线程)使用，比如在修改某个View的显示时
     * 只有调用invalidate()方法才会重新绘制UI界面，invalidate作用主要是将原来的旧的View从主UI线程给POP掉
     * requestLayout()方法调用主要是用来在重新绘制组件时，调用invalidate方法后，需要重新计算或者重新测量新绘制的
     * View的高度和宽度，否则会出现新的View显示不完全，因为没调用该方法，还是使用原来旧的View尺寸。
     */
    public void setMyText(String str) {
        myText = str;
        myPaint.getTextBounds(myText, 0, myText.length(), myTextBound);
        invalidate();
        requestLayout();
    }

    /**
     * @author zhongqihong
     * 重写getMyText()方法
     */
    public String getMyText() {
        return myText;
    }

    /**
     * @author zhongqihong
     * 重写setMyImageSource()方法
     */
    public void setMyImageSource(int imageId) {
        myImageSrc = BitmapFactory.decodeResource(getResources(), imageId);
        invalidate();
        requestLayout();
    }

    /**
     * @author zhongqihong
     * 重写setImageDimesion(int ImageWidth,int ImageHeight)
     */
    public void setImageDimension(int ImageWidth, int ImageHeight) {
        myImageWidth = ImageWidth;
        myImageHeight = ImageHeight;
        invalidate();
        requestLayout();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        /**
         * @author zhongqihong
         * 此处的处理是：当指定明确的图片的尺寸时，我们就直接获取指定的尺寸大小，如果为0则表示没有指定图片
         * 大小那么就直接使用myImageSrc.getHeight()，myImageSrc.getWidth()图片本身的尺寸
         * */
        if (myImageHeight == 0) {
            myImageHeight = myImageSrc.getHeight();
        }
        if (myImageWidth == 0) {
            myImageWidth = myImageSrc.getWidth();
        }
        //测量宽度
//        if (widthMode == MeasureSpec.EXACTLY) {
        myWidth = widthSize;
//        } else {
//            int desired = getPaddingLeft() + myImageSrc.getWidth() + myTextAndImgPadding + myTextBound.width() + getPaddingRight();
//            if (widthMode == MeasureSpec.AT_MOST) {
//                myWidth = Math.min(desired, widthSize);
//            }
//        }
        //测量高度
//        if (heightMode == MeasureSpec.EXACTLY) {
        myHeight = heightSize;
//        } else {
//            int maxDesiredHeight = Math.max(myTextBound.height(), myImageSrc.getHeight());
//            System.out.println("myImageSrc.getHeight------>" + myImageSrc.getHeight() + "    maxDesiredHeight----->" + maxDesiredHeight);
//            int desired = getPaddingTop() + maxDesiredHeight + getPaddingBottom();
//            System.out.println("desired------>" + desired);
//            myHeight = Math.min(desired, heightSize);
//        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        System.out.println("I am onMeasure:---myWidth----->" + myWidth + "------------>myHeight---->" + myHeight);
    }
}
