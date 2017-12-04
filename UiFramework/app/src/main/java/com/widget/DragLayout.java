package com.widget;

//添加测试编译注释

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;
import com.uidemo.R;


/**
 * 使用ViewRragHelper实现侧滑效果功能
 */
public class DragLayout extends FrameLayout {
    private static final boolean IS_SHOW_SHADOW = true;
    //手势处理类
    private GestureDetectorCompat gestureDetector;
    //视图拖拽移动帮助类
    private ViewDragHelper dragHelper;
    //滑动监听器
    private DragListener dragListener;
    //水平拖拽的距离
    private int range;
    //宽度
    private int width;
    //高度
    private int height;
    //main视图距离在ViewGroup距离左边的距离
    private int mainLeft;
    private Context context;
    private ImageView ivShadow;
    //左侧布局
    private RelativeLayout vgLeft;
    //右侧(主界面布局)
    private CustomRelativeLayout vgMain;
    //页面状态 默认为关闭
    private Status status = Status.CLOSE;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    //设置是否允许侧滑
    private boolean isDrag = true;

    private ImageView shadowView;
    /**
     * Edge flag indicating that the left edge should be affected.
     */
    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;

    /**
     * Edge flag indicating that the right edge should be affected.
     */
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;

    /**
     * Edge flag indicating that the bottom edge should be affected.
     */
    public static final int EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM;

    /**
     * Edge flag set indicating all edges should be affected.
     */
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT | EDGE_BOTTOM;
    private static final int[] EDGE_FLAGS = {
            EDGE_LEFT, EDGE_RIGHT, EDGE_BOTTOM, EDGE_ALL
    };

    /**
     * 实现子View的拖拽滑动，实现Callback当中相关的方法
     */
    private final ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {
        /**
         * 表示水平方向滑动，同时方法中会进行判断边界值，mainleft代表移动距离
         * @param child Child view being dragged
         * @param left Attempted motion along the X axis
         * @param dx Proposed change in position for left
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mainLeft + dx < 0) {
                return 0;
            } else if (mainLeft + dx > range) {
                return range;
            } else {
                return left;
            }
        }

        /**
         * 拦截所有的子View ,直接返回true,表示所有的子View都可以进行拖拽移动,child == vgMain表示
         * 只有main页面可以拖拽
         * @param child Child the user is attempting to capture
         * @param pointerId ID of the pointer attempting the capture
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == vgLeft;
        }

        /**
         * 设置水平方向滑动的最远距离
         * @param child Child view to check  屏幕宽度
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return width;
        }

        /**
         * 当拖拽的子View，手势释放的时候回调的方法， 然后根据左滑或者右滑的距离进行判断打开或者关闭
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel > 0) {
                open();
            } else if (xvel < 0) {
                close();
            } else if (releasedChild == vgMain && mainLeft > range * 0.3) {
                open();
            } else if (releasedChild == vgLeft && mainLeft > range * 0.7) {
                open();
            } else {
                close();
            }
        }

        /**
         * 子View被拖拽 移动的时候回调的方法
         * 根据移动坐标位置，然后进行重新定义left view和main view。同时调用dispathDragEvent()方法进行拖拽事件相关处理分发同时根据状态来回调接口:
         * @param changedView View whose position changed
         * @param left New X coordinate of the left edge of the view
         * @param top New Y coordinate of the top edge of the view
         * @param dx Change in X position from the last call
         * @param dy Change in Y position from the last call
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if (changedView == vgMain) {
                mainLeft = left;
            } else {
                mainLeft = mainLeft + left;
            }
            if (mainLeft < 0) {
                mainLeft = 0;
            } else if (mainLeft > range) {
                mainLeft = range;
            }

            if (IS_SHOW_SHADOW) {
                ivShadow.layout(mainLeft, 0, mainLeft + width, height);
            }
            if (changedView == vgLeft) {
                vgLeft.layout(0, 0, width, height);
                vgMain.layout(mainLeft, 0, mainLeft + width, height);
            }

            dispatchDragEvent(mainLeft);
            // 为了兼容低版本, 每次修改值之后, 进行重绘
            invalidate();
        }

        /**
         * 边缘触摸事件
         * @param edgeFlags A combination of edge flags describing the edge(s)
         *                  currently touched
         * @param pointerId ID of the pointer touching the described edge(s)
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        /**
         * 边缘拖拽实现，设置左边拖拽
         * @param edgeFlags A combination of edge flags describing the edge(s)
         *                  dragged
         * @param pointerId ID of the pointer touching the described edge(s)
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //触摸到左边界的时候 我们capture住vgMain
            dragHelper.captureChildView(vgMain, pointerId);
        }

    };

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    /**
     * 创建ViewDragHelper实例
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetector = new GestureDetectorCompat(context, mGestureListener);
        dragHelper = ViewDragHelper.create(this, 1f, dragHelperCallback);
        //实现边缘拖拽实现，分为滑动左边缘还是右边缘：EDGE_LEFT和EDGE_RIGHT
//        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout, defStyle,
                R.style.SwipeBackLayout);
        int edgeSize = a.getDimensionPixelSize(R.styleable.SwipeBackLayout_edge_size, -1);
        if (edgeSize > 0)
            setEdgeSize(edgeSize);
        int mode = EDGE_FLAGS[a.getInt(R.styleable.SwipeBackLayout_edge_flag, 0)];
        dragHelper.setEdgeTrackingEnabled(mode);
    }

    /**
     * 设置边缘拖拽的距离
     *
     * @param size
     */
    public void setEdgeSize(int size) {
        dragHelper.setEdgeSize(size);
    }

    public void setDrag(boolean isDrag) {
        this.isDrag = isDrag;
        if (isDrag) {
            //这里有个Bug,当isDrag从false变为true是，mDragHelper的mCallBack在
            //首次滑动时不响应，再次滑动才响应，只好在此调用下，让mDragHelper恢复下状态
            dragHelper.abort();
        }
    }

    /**
     * \在SimpleOnGestureListener的onScroll方法中判断，如果是横向向右滑动，且侧滑是关闭状态，且isDrag的tag为true时，让ViewDragHelper响应对应滑动事件（滑出），
     * 如果是横向向左滑动，且侧滑是开启状态，且isDrag的tag为true时，让ViewDragHelper响应对应滑动事件（滑入），
     */
    SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if ((Math.abs(distanceX) > Math.abs(distanceY)) && distanceX < 0 && isDrag != false && status == Status.CLOSE) {
                return true;
            } else if ((Math.abs(distanceX) > Math.abs(distanceY)) && distanceX > 0 && isDrag != false && status == Status.OPEN) {
                return true;
            } else {
                return false;
            }
        }
    };

    /**
     * 滑动相关回调接口
     */
    public interface DragListener {
        //界面打开
        public void onOpen();

        //界面关闭
        public void onClose();

        //界面滑动过程中
        public void onDrag(float percent);
    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    /**
     * 布局加载完成回调
     * 做一些初始化的操作
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (IS_SHOW_SHADOW) {
            ivShadow = new ImageView(context);
            ivShadow.setImageResource(R.mipmap.shadow);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(ivShadow, 1, lp);
        }
        //左侧界面
        vgLeft = (RelativeLayout) getChildAt(0);
        //右侧(主)界面
        vgMain = (CustomRelativeLayout) getChildAt(IS_SHOW_SHADOW ? 2 : 1);
        vgMain.setDragLayout(this);
        vgLeft.setClickable(true);
        vgMain.setClickable(true);


    }

    public ViewGroup getVgMain() {
        return vgMain;
    }

    public ViewGroup getVgLeft() {
        return vgLeft;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = vgLeft.getMeasuredWidth();
        height = vgLeft.getMeasuredHeight();
        //可以水平拖拽滑动的距离 一共为屏幕宽度的82%
        range = (int) (width * 0.82f);
    }

    /**
     * 调用进行left和main 视图进行位置布局
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        vgLeft.layout(0, 0, width, height);
        vgMain.layout(mainLeft, 0, mainLeft + width, height);
    }

    /**
     * 拦截触摸事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
    }

    /**
     * 将拦截的到事件给ViewDragHelper进行处理
     *
     * @param e
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        try {
            dragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 进行处理拖拽事件
     *
     * @param mainLeft
     */
    private void dispatchDragEvent(int mainLeft) {
        if (dragListener == null) {
            return;
        }
        float percent = mainLeft / (float) range;

        //滑动动画效果
        animateView(percent);
        //进行回调滑动的百分比
        dragListener.onDrag(percent);
        Status lastStatus = status;
        if (lastStatus != getStatus() && status == Status.CLOSE) {
            dragListener.onClose();
        } else if (lastStatus != getStatus() && status == Status.OPEN) {
            dragListener.onOpen();
        }
    }

    /**
     * 根据滑动的距离的比例,进行平移动画
     *
     * @param percent
     */
    private void animateView(float percent) {
        // 主面板：缩放
        float f1 = 1 - percent * 0.5f;
        ViewHelper.setScaleX(vgMain, 1f);
        ViewHelper.setScaleY(vgMain, 1f);
        // 左面板：缩放、平移、透明度
        ViewHelper.setScaleX(vgLeft, 1f);
        ViewHelper.setScaleY(vgLeft, 1f);

        ViewHelper.setTranslationX(vgLeft, -vgLeft.getWidth() / 2.5f + vgLeft.getWidth() / 2.5f * percent);
//        ViewHelper.setAlpha(vgLeft, percent);
        // 背景：颜色渐变
        if (IS_SHOW_SHADOW) {
            //阴影效果视图大小进行缩放
            ViewHelper.setScaleX(ivShadow, f1 * 1.2f * (1 - percent * 0.10f));
            ViewHelper.setScaleY(ivShadow, f1 * 1.85f * (1 - percent * 0.10f));
        }
    }

    /**
     * 有加速度,当我们停止滑动的时候，该不会立即停止动画效果
     */
    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 页面状态(滑动,打开,关闭)
     */
    public enum Status {
        DRAG, OPEN, CLOSE
    }

    /**
     * 页面状态设置
     *
     * @return
     */
    public Status getStatus() {
        if (mainLeft == 0) {
            status = Status.CLOSE;
        } else if (mainLeft == range) {
            status = Status.OPEN;
        } else {
            status = Status.DRAG;
        }
        return status;
    }

    public void open() {
        open(true);
    }

    public void open(boolean animate) {
        if (animate) {
            //继续滑动
            if (dragHelper.smoothSlideViewTo(vgMain, range, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vgMain.layout(range, 0, range * 2, height);
            dispatchDragEvent(range);
        }
    }

    public void close() {
        close(true);
    }

    public void close(boolean animate) {
        if (animate) {
            //继续滑动
            if (dragHelper.smoothSlideViewTo(vgMain, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vgMain.layout(0, 0, width, height);
            dispatchDragEvent(0);
        }
    }

}
