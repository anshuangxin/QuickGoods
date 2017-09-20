package gly.quickgoods.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import gly.quickgoods.R;

@SuppressLint("AppCompatCustomView")
public class CircularImageView extends ImageView {
    private static final int LONG_CLICK_HAND = 1000;
    private boolean isSelected;
    private int borderWidth;
    private Bitmap image;
    private Paint selectPaint;
    private Paint paintBorder;
    private Paint circlePaint;
    private String text = "";
    private float textHeight;
    private Paint textPaint;
    private float mHeight;
    private float mWidth;
    private float bitmapWidth;
    private float btnScale;
    private Matrix imgMatrix;
    private RectF btnRect;
    private Path btnPath;

    private int borderColor;
    private Paint linePaint;
    private Paint messageCountPaint;
    private float messageCountH;

    public CircularImageView(Context context) {
        this(context, null);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.circularImageViewStyle);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @Override
    public void setBackgroundColor(int color) {
        circlePaint.setColor(color);
        invalidate();
    }


    public void setText(String text) {
        this.image = null;
        if (!TextUtils.isEmpty(text) && text.length() > 2) {
            this.text = text.substring(text.length() - 2, text.length());
        } else if (!TextUtils.isEmpty(text)) {
            this.text = text;
        }
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        this.image = bm;
        this.text = "";
        invalidate();
    }

    @Override
    public void setImageResource(int resId) {
        this.image = BitmapFactory.decodeResource(getResources(), resId);
        this.text = "";
        invalidate();
    }

    @Override
    public void setBackgroundResource(int resid) {
        setImageResource(resid);
    }

    private void initBitMap() {
        // TODO Auto-generated method stub
        btnScale = bitmapWidth / image.getWidth();
        imgMatrix.reset();
        imgMatrix.setScale(btnScale, btnScale);
        imgMatrix.postTranslate((mWidth - image.getWidth() * btnScale) / 2f,
                (mHeight - image.getHeight() * btnScale) / 2f);
        btnRect = new RectF(0, 0, image.getWidth(), image.getHeight());
        imgMatrix.mapRect(btnRect);
        btnPath = new Path();
        btnPath.arcTo(btnRect, 0, 359);
        btnPath.close();
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
//        setVisibility(GONE);
        // Initialize paint objects
        imgMatrix = new Matrix();
        if (null == circlePaint) {
            circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            circlePaint.setColor(Color.TRANSPARENT);
        }
        circlePaint.setStyle(Paint.Style.FILL);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        selectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectPaint.setColor(Color.parseColor("#20000000"));

        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setStyle(Paint.Style.STROKE);


        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);

        messageCountPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        messageCountPaint.setTypeface(Typeface.DEFAULT_BOLD);
        messageCountPaint.setTextAlign(Paint.Align.CENTER);

        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.CircularImageView, defStyle, 0);
        borderWidth = attributes.getDimensionPixelOffset(
                R.styleable.CircularImageView_border_width, 0);
        borderColor = attributes.getColor(
                R.styleable.CircularImageView_border_color, 0);
        paintBorder.setColor(borderColor);
        paintBorder.setStrokeWidth(borderWidth);
        setClickable(false);
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    @Override
    public void setBackground(Drawable background) {
        if (background instanceof ColorDrawable) {
            ColorDrawable c = (ColorDrawable) background;
            if (null == circlePaint) {
                circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            }
            circlePaint.setColor(c.getColor());
        }
    }

    public void addShadow() {
        setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f,
                Color.parseColor("#FFFFFF"));
    }


    boolean isLongClick = false;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isClickable()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSelected = true;
                isLongClick = false;
                handler.sendEmptyMessageDelayed(LONG_CLICK_HAND, 500);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!isLongClick) {
                    performClick();
                }
            case MotionEvent.ACTION_CANCEL:
                isSelected = false;
                handler.removeMessages(LONG_CLICK_HAND);
                invalidate();
                break;
        }
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LONG_CLICK_HAND:
                    isLongClick = true;
                    performLongClick();
                    break;
            }
        }
    };

    @Override
    public void invalidate() {
        setVisibility(VISIBLE);
        super.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        float center = mWidth / 2f;
        canvas.drawCircle(center, mHeight / 2, center, circlePaint);
        if (null != image) {
            initBitMap();
            canvas.save();
            canvas.clipPath(btnPath);
            canvas.drawBitmap(image, imgMatrix, null);
            canvas.restore();
        } else if (!TextUtils.isEmpty(text)) {
            textPaint.setTextSize(textHeight);
            canvas.drawText(text, center, (mHeight + textHeight) / 2f - mHeight
                    / 20f, textPaint);
        }
        if (0 != borderWidth) {
            canvas.drawCircle(center, center, center - borderWidth / 2, paintBorder);
        }

        if (isSelected) {
            canvas.drawCircle(center, mHeight / 2, center, selectPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        bitmapWidth = mWidth;
        textHeight = mHeight / 3f;
        textPaint.setTextSize(textHeight);

        linePaint.setStrokeWidth(mWidth / 36f);

        messageCountH = mHeight / 3;
        messageCountPaint.setTextSize(messageCountH / 2f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(widthMeasureSpec, 0));
        int childHeightSize = getMeasuredHeight();
        int childWidthSize = childHeightSize;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public String getText() {
        return text;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        setImageBitmap(bd.getBitmap());
    }

}