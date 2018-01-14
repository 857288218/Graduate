package com.example.rjq.myapplication.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;

import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.View;

import android.view.ViewOutlineProvider;

import android.widget.ImageSwitcher;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.example.rjq.myapplication.R;
public class SliderLayout extends RelativeLayout implements View.OnTouchListener, ViewSwitcher.ViewFactory {
    private Context context;
    private ArcShape arcShape = ArcShape.inSide;
    private float arcHeight = getResources().getDimension(R.dimen.sl_arc_height);
    private ArcPosition arcPosition = ArcPosition.bottom;
    private int height = 0;
    private int width = 0;
    private Path clipPath;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    //Arc Shape
    private enum ArcShape {
        inSide, outSide
    }

    //Arc Position
    private enum ArcPosition {
        top,
        bottom,
        left,
        right
    }

    public SliderLayout(Context context) {
        super(context);
        this.context = context;
        initView(context, null, 0);
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context, attrs, 0);
    }

    public SliderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs, defStyleAttr);
    }

    /**
     * init
     *
     * @param context      context
     * @param attrs        attrs
     * @param defStyleAttr defStyleAttr
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SliderLayout, defStyleAttr, 0);
        if (array != null) {
            //get the shape of arc
            int intArcShape = array.getInt(R.styleable.SliderLayout_sl_arc_shape, arcShape.ordinal());
            for (ArcShape shape : ArcShape.values()) {
                if (shape.ordinal() == intArcShape) {
                    arcShape = shape;
                    break;
                }
            }
            //get the position of arc
            int intArcPosition = array.getInt(R.styleable.SliderLayout_sl_arc_position, arcPosition.ordinal());
            for (ArcPosition position : ArcPosition.values()) {
                if (position.ordinal() == intArcPosition) {
                    arcPosition = position;
                    break;
                }
            }

            arcHeight = array.getDimension(R.styleable.SliderLayout_sl_arc_height, arcHeight);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundColor(0xFFFFFFFF);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT));

        return imageView;
    }

    /**
     * create path
     *
     * @return Path
     */
    private Path createClipPath() {
        final Path path = new Path();

        switch (arcPosition) {
            case bottom: {
                if (arcShape == ArcShape.inSide) {
                    path.moveTo(0, 0);
                    path.lineTo(0, height);
                    path.quadTo(width / 2, height - 2 * arcHeight, width, height);
                    path.lineTo(width, 0);
                    path.close();
                } else {
                    path.moveTo(0, 0);
                    path.lineTo(0, height - arcHeight);
                    path.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
                    path.lineTo(width, 0);
                    path.close();
                }
                break;
            }
            case top:
                if (arcShape == ArcShape.inSide) {
                    path.moveTo(0, height);
                    path.lineTo(0, 0);
                    path.quadTo(width / 2, 2 * arcHeight, width, 0);
                    path.lineTo(width, height);
                    path.close();
                } else {
                    path.moveTo(0, arcHeight);
                    path.quadTo(width / 2, -arcHeight, width, arcHeight);
                    path.lineTo(width, height);
                    path.lineTo(0, height);
                    path.close();
                }
                break;
            case left:
                if (arcShape == ArcShape.inSide) {
                    path.moveTo(width, 0);
                    path.lineTo(0, 0);
                    path.quadTo(arcHeight * 2, height / 2, 0, height);
                    path.lineTo(width, height);
                    path.close();
                } else {
                    path.moveTo(width, 0);
                    path.lineTo(arcHeight, 0);
                    path.quadTo(-arcHeight, height / 2, arcHeight, height);
                    path.lineTo(width, height);
                    path.close();
                }
                break;
            case right:
                if (arcShape == ArcShape.inSide) {
                    path.moveTo(0, 0);
                    path.lineTo(width, 0);
                    path.quadTo(width - arcHeight * 2, height / 2, width, height);
                    path.lineTo(0, height);
                    path.close();
                } else {
                    path.moveTo(0, 0);
                    path.lineTo(width - arcHeight, 0);
                    path.quadTo(width + arcHeight, height / 2, width - arcHeight, height);
                    path.lineTo(0, height);
                    path.close();
                }
                break;
        }
        return path;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            calculateLayout();
        }
    }

    /**
     *calculate layout
     */
    private void calculateLayout() {
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        if (width > 0 && height > 0) {

            clipPath = createClipPath();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && arcShape != ArcShape.inSide) {
                setOutlineProvider(new ViewOutlineProvider() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setConvexPath(clipPath);
                    }
                });
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (arcHeight > 0) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
            canvas.drawPath(clipPath, paint);
            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            super.dispatchDraw(canvas);
        }
    }
}
