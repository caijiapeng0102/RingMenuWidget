package com.caijiapeng.library.ringmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 环形菜单控件<br>
 *  由开源控件 <a href="https://code.google.com/archive/p/radial-menu-widget/">radial-menu-widget</a> 改造<br>
 *  在此感谢此开源作者
 * @author caijiapeng
 * @version 1.0.0
 * @date 2017-04-07
 * @github https://github.com/caijiapeng0102/RingMenuWidget
 * */
public class RingMenuWidget extends View {     
    /**环形菜单列表*/
    private List<RingMenuItem> menuList = new ArrayList<RingMenuItem>();
    /**中间菜单*/
    private RingMenuItem centerCircle = null;

    private float screen_density = getContext().getResources().getDisplayMetrics().density;

    /**按钮默认的背景色*/
    private int defaultColor = Color.rgb(34, 96, 120);
    /**按钮默认的背景色透明度 0-255  0表示透明*/
    private int defaultAlpha = 180;
    /**边框以及菜单背景颜色*/
    private int borderColor = Color.rgb(150, 150, 150);
    /**边框颜色透明度*/
    private int borderAlpha = 255;
    /**外环按钮边框宽度*/
    private int borderWidth = scalePX(20);

    /**按钮点击（选中）的背景色*/
    private int selectedColor = Color.rgb(70, 130, 180);
    /**按钮点击（选中）的背景色透明度*/
    private int selectedAlpha = 255;

    /**按钮禁用状态的背景色*/
    private int disabledColor = Color.rgb(34, 96, 120);
    /**按钮禁用状态的背景色透明度*/
    private int disabledAlpha = 255;

    /**图片透明度*/
    private int pictureAlpha = 255;

    /**字体颜色*/
    private int textColor = Color.rgb(255, 255, 255);
    /**字体颜色透明度*/
    private int textAlpha = 255;
    /**文本字体大小*/
    private int textSize = scalePX(15);


    /**环形按钮组件数量*/
    private int wedgeQty = 1;
    private RingMenuWedge[] Wedges = new RingMenuWedge[wedgeQty];
    /**当前选中的按钮组件*/
    private RingMenuWedge selected = null;
    private Rect[] iconRect = new Rect[wedgeQty];

    /**环形菜单内环半径*/
    private int innerRadius = scalePX(35);
    /**环形菜单外环半径*/
    private int outerRadius = scalePX(90);
    /**中间圆形的半径*/
    private int cRadius = innerRadius - scalePX(7);
    /**环形按钮图标的最小尺寸*/
    private int minIconSize = scalePX(15);
    /**环形按钮图标的最大尺寸*/
    private int maxIconSize = scalePX(35);
    /**中间按钮图标的最小尺寸*/
    private int minCenterIconSize = scalePX(15);
    /**中间按钮图标的最大尺寸*/
    private int maxCenterIconSize = scalePX(35);

    /**控件的中心X坐标*/
    private int xPosition = scalePX(120);
    /**控件的中心Y坐标*/
    private int yPosition = scalePX(120);
    /**自动定位控件中心点位置*/
    private boolean isAutoLocation = true;

    /**是否选中组件*/
    private boolean inWedge = false;
    /**所选中组件的下标*/
    private int wedgePosition = -1;
    /**是否选中中间按钮*/
    private boolean inCircle = false;

    /**是否在组件中滑动*/
    private boolean isMove = false;

    public RingMenuWidget(Context context) {
        super(context);
        init();
    }

    public RingMenuWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RingMenuWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        determineWedges();
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int state = e.getAction();
        int eventX = (int) e.getX();
        int eventY = (int) e.getY();
        if (state == MotionEvent.ACTION_DOWN) {
            inWedge = false;
            inCircle = false;
            isMove = false;
            //判断是否点中环形菜单中的某个组件
            for (int i = 0; i < Wedges.length; i++) {
                if(menuList.get(i).isEnabled()){
                    RingMenuWedge f = Wedges[i];
                    double slice = (2 * Math.PI) / wedgeQty;
                    double start = (2 * Math.PI) * (0.75);
                    inWedge = isPutInWedge(eventX, eventY, xPosition, yPosition, innerRadius, outerRadius, (i * slice) + start, slice);
                    if (inWedge) {
                        wedgePosition = i;
                        selected = f;
                        break;
                    }
                }
            }

            // 判断是否点击中间按钮
            if (centerCircle != null && centerCircle.isEnabled()) {
                inCircle = isPutInCircle(eventX, eventY, xPosition, yPosition, cRadius);
            }
        } else if (state == MotionEvent.ACTION_UP) {
            if (!isMove) {
                // execute commands...
                // put in stuff here to "return" the button that was pressed.
                if (inCircle == true) {
                    centerCircle.menuActiviated();
                } else if (selected != null) {
                    menuList.get(wedgePosition).menuActiviated();
                }
            }
            selected = null;
            inCircle = false;
        } else if (state == MotionEvent.ACTION_MOVE) {
            if (inCircle) {
                inCircle = isPutInCircle(eventX, eventY, xPosition, yPosition, cRadius);
            } else if (inWedge) {
                double slice = (2 * Math.PI) / wedgeQty;
                double start = (2 * Math.PI) * (0.75);
                inWedge = isPutInWedge(eventX, eventY, xPosition, yPosition, innerRadius, outerRadius, (wedgePosition * slice) + start, slice);
            } else {
                //如果移动了，则恢复默认状态
                isMove = true;
                selected = null;
                inWedge = false;
                inCircle = false;
                wedgePosition = -1;
            }
        }
        invalidate();
        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isAutoLocation){
            xPosition = getHeight() / 2;
            yPosition = getWidth() / 2;
            determineWedges();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(borderColor);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);


        //画圆形背景
        canvas.drawCircle(xPosition, yPosition, outerRadius, paint);

        //画环形菜单
        for (int i = 0; i < Wedges.length; i++) {
            RingMenuWedge radialMenuWedge = Wedges[i];
            RingMenuItem radialMenuItem = menuList.get(i);
            if (radialMenuWedge == selected) { //选中状态
                paint.setColor(selectedColor);
                paint.setAlpha(selectedAlpha);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(radialMenuWedge, paint);
            } else if (radialMenuItem.isEnabled()) { //默认状态
                paint.setColor(defaultColor);
                paint.setAlpha(defaultAlpha);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(radialMenuWedge, paint);
            } else {//禁用状态
                paint.setColor(disabledColor);
                paint.setAlpha(disabledAlpha);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(radialMenuWedge, paint);
            }
            Rect rf = iconRect[i];


            //填充文本与图标
            if ((menuList.get(i).getIcon() != 0) && (menuList.get(i).getLabel() != null)) {
                // 图标和文本都有
                String menuItemName = menuList.get(i).getLabel();
                paint.setColor(textColor);
                paint.setAlpha(textAlpha);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(textSize);

                Rect rect = new Rect();
                float textHeight = getTextHeight(menuItemName, paint, rect);

                Rect rf2 = new Rect();
                rf2.set(rf.left, rf.top - ((int) textHeight / 2), rf.right, rf.bottom - ((int) textHeight / 2));

                float textBottom = rf2.bottom;
                paint.getTextBounds(menuItemName, 0, menuItemName.length(), rect);
                float textLeft = rf.centerX() - rect.width() / 2;
                textBottom = textBottom + (rect.height() + 3);
                canvas.drawText(menuItemName, textLeft - rect.left, textBottom - rect.bottom, paint);

                // Puts in the Icon
                Drawable drawable = getResources().getDrawable(menuList.get(i).getIcon());
                drawable.setBounds(rf2);
                drawable.setAlpha(pictureAlpha);
                drawable.draw(canvas);

            } else if (menuList.get(i).getIcon() != 0) {
                //只有图标
                Drawable drawable = getResources().getDrawable(menuList.get(i).getIcon());
                drawable.setBounds(rf);
                drawable.setAlpha(pictureAlpha);
                drawable.draw(canvas);
            } else {
                //只有文本
                paint.setColor(textColor);
                paint.setAlpha(textAlpha);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(textSize);

                // This will look for a "new line" and split into multiple lines
                String menuItemName = menuList.get(i).getLabel();

                // 获取文本总高度
                Rect rect = new Rect();
                float textHeight = getTextHeight(menuItemName, paint, rect);

                float textBottom = rf.centerY() - (textHeight / 2);
                paint.getTextBounds(menuItemName, 0, menuItemName.length(), rect);
                float textLeft = rf.centerX() - rect.width() / 2;
                textBottom = textBottom + (rect.height() + 3);
                canvas.drawText(menuItemName, textLeft - rect.left, textBottom - rect.bottom, paint);
            }
        }

        //画边框间隙
        for (int i = 0; i < Wedges.length; i++) {
            RingMenuWedge radialMenuWedge = Wedges[i];
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            float borderSlice = (float) (360 * borderWidth / (2 * Math.PI * outerRadius));
            float startAngle01 = radialMenuWedge.getStartArc() - borderSlice / 2;
            RectF borderRectF = new RectF(xPosition - outerRadius, yPosition - outerRadius, xPosition + outerRadius, yPosition + outerRadius);
            canvas.drawArc(borderRectF, startAngle01, 0, true, paint);
        }


        //画中心按钮
        if (centerCircle != null) {
            paint.setColor(borderColor);
            paint.setAlpha(borderAlpha);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(xPosition, yPosition, cRadius, paint);
            if (inCircle == true) { //选中状态
                paint.setColor(selectedColor);
                paint.setAlpha(selectedAlpha);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(xPosition, yPosition, cRadius, paint);
            } else if(centerCircle.isEnabled()){ //默认状态
                paint.setColor(defaultColor);
                paint.setAlpha(defaultAlpha);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(xPosition, yPosition, cRadius, paint);
            }else{//禁用状态
                paint.setColor(disabledColor);
                paint.setAlpha(disabledAlpha);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(xPosition, yPosition, cRadius, paint);
            }

            // 画中心按钮的文本与图标
            if ((centerCircle.getIcon() != 0) && (centerCircle.getLabel() != null)) {
                //文本与图标都有
                String menuItemName = centerCircle.getLabel();
                String[] stringArray = menuItemName.split("\n");

                paint.setColor(textColor);
                paint.setAlpha(textAlpha);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(textSize);

                Rect rectText = new Rect();
                Rect rectIcon = new Rect();
                Drawable drawable = getResources().getDrawable(centerCircle.getIcon());

                int h = getIconSize(drawable.getIntrinsicHeight(), minCenterIconSize, maxCenterIconSize);
                int w = getIconSize(drawable.getIntrinsicWidth(), minCenterIconSize, maxIconSize);
                rectIcon.set(xPosition - w / 2, yPosition - h / 2, xPosition + w / 2, yPosition + h / 2);

                float textHeight = 0;
                for (int j = 0; j < stringArray.length; j++) {
                    paint.getTextBounds(stringArray[j], 0, stringArray[j].length(), rectText);
                    textHeight = textHeight + (rectText.height() + 3);
                }

                rectIcon.set(rectIcon.left, rectIcon.top - ((int) textHeight / 2), rectIcon.right, rectIcon.bottom - ((int) textHeight / 2));

                float textBottom = rectIcon.bottom;
                for (int j = 0; j < stringArray.length; j++) {
                    paint.getTextBounds(stringArray[j], 0, stringArray[j].length(), rectText);
                    float textLeft = xPosition - rectText.width() / 2;
                    textBottom = textBottom + (rectText.height() + 3);
                    canvas.drawText(stringArray[j], textLeft - rectText.left, textBottom - rectText.bottom, paint);
                }

                // Puts in the Icon
                drawable.setBounds(rectIcon);
                drawable.setAlpha(pictureAlpha);
                drawable.draw(canvas);


            } else if (centerCircle.getIcon() != 0) {
                //只有图标
                Rect rect = new Rect();
                Drawable drawable = getResources().getDrawable(centerCircle.getIcon());

                int h = getIconSize(drawable.getIntrinsicHeight(), minCenterIconSize, maxCenterIconSize);
                int w = getIconSize(drawable.getIntrinsicWidth(), minCenterIconSize, maxCenterIconSize);
                rect.set(xPosition - w / 2, yPosition - h / 2, xPosition + w / 2, yPosition + h / 2);

                drawable.setBounds(rect);
                drawable.setAlpha(pictureAlpha);
                drawable.draw(canvas);
            } else {
                //只有文本
                paint.setColor(textColor);
                paint.setAlpha(textAlpha);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(textSize);

                // This will look for a "new line" and split into multiple lines
                String menuItemName = centerCircle.getLabel();
                String[] stringArray = menuItemName.split("\n");

                // 获取文本总高度
                Rect rect = new Rect();
                float textHeight = 0;
                for (int j = 0; j < stringArray.length; j++) {
                    paint.getTextBounds(stringArray[j], 0, stringArray[j].length(), rect);
                    textHeight = textHeight + (rect.height() + 3);
                }

                float textBottom = yPosition - (textHeight / 2);
                for (int j = 0; j < stringArray.length; j++) {
                    paint.getTextBounds(stringArray[j], 0, stringArray[j].length(), rect);
                    float textLeft = xPosition - rect.width() / 2;
                    textBottom = textBottom + (rect.height() + 3);
                    canvas.drawText(stringArray[j], textLeft - rect.left, textBottom - rect.bottom, paint);
                }

            }
        }

    }

    /**dp转px*/
    private int scalePX(int dp_size) {
        int px_size = (int) (dp_size * screen_density + 0.5f);
        return px_size;
    }

    /**获取图标大小*/
    private int getIconSize(int iconSize, int minSize, int maxSize) {
        if (iconSize > minSize) {
            if (iconSize > maxSize) {
                return maxSize;
            } else { // iconSize < maxSize
                return iconSize;
            }
        } else { // iconSize < minSize
            return minSize;
        }
    }

    /**获取文本高度*/
    private float getTextHeight(String text, Paint paint, Rect rect) {
        float textHeight = 0;
        paint.getTextBounds(text, 0, text.length(), rect);
        textHeight = textHeight + (rect.height() + 3);
        return textHeight;
    }

    /**生成组件*/
    private void determineWedges() {
        int entriesQty = menuList.size();
        if (entriesQty > 0) {
            wedgeQty = entriesQty;
            float borderSlice = (float) (360 * borderWidth / (2 * Math.PI * outerRadius));
            float degSlice = (float) 360 / wedgeQty - borderSlice;
            float start_degSlice = 270 + borderSlice / 2;
            // 计算在哪里放置图片
            double rSlice = (2 * Math.PI) / wedgeQty;
            double rStart = (2 * Math.PI) * (0.75);

            this.Wedges = new RingMenuWedge[wedgeQty];
            this.iconRect = new Rect[wedgeQty];

            for (int i = 0; i < Wedges.length; i++) {
                this.Wedges[i] = new RingMenuWedge(xPosition, yPosition, innerRadius, outerRadius, (i * degSlice) + start_degSlice + (borderSlice * i), degSlice);
                float xCenter = (float) (Math.cos(((rSlice * i) + (rSlice * 0.5)) + rStart) * (outerRadius + innerRadius) / 2) + xPosition;
                float yCenter = (float) (Math.sin(((rSlice * i) + (rSlice * 0.5)) + rStart) * (outerRadius + innerRadius) / 2) + yPosition;

                int h = maxIconSize;
                int w = maxIconSize;
                if (menuList.get(i).getIcon() != 0) {
                    Drawable drawable = getResources().getDrawable(menuList.get(i).getIcon());
                    h = getIconSize(drawable.getIntrinsicHeight(), minIconSize, maxIconSize);
                    w = getIconSize(drawable.getIntrinsicWidth(), minIconSize, maxIconSize);
                }
                this.iconRect[i] = new Rect((int) xCenter - w / 2, (int) yCenter - h / 2, (int) xCenter + w / 2, (int) yCenter + h / 2);
            }
            invalidate(); //重画
        }
    }

    /**是否点击了组件*/
    protected boolean isPutInWedge(double px, double py, float xRadiusCenter, float yRadiusCenter, int innerRadius, int outerRadius, double startAngle, double sweepAngle) {
        double diffX = px - xRadiusCenter;
        double diffY = py - yRadiusCenter;
        double angle = Math.atan2(diffY, diffX);
        if (angle < 0) {
            angle += (2 * Math.PI);
        }
        if (startAngle >= (2 * Math.PI)) {
            startAngle = startAngle - (2 * Math.PI);
        }
        // checks if point falls between the start and end of the wedge
        if ((angle >= startAngle && angle <= startAngle + sweepAngle) || (angle + (2 * Math.PI) >= startAngle && (angle + (2 * Math.PI)) <= startAngle + sweepAngle)) {
            // checks if point falls inside the radius of the wedge
            double dist = diffX * diffX + diffY * diffY;
            if (dist < outerRadius * outerRadius && dist > innerRadius * innerRadius) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否点击了中间按钮
     * @param touchX 点击的X坐标
     * @param touchY  点击的Y坐标
     * @param xPosition 圆形X坐标
     * @param yPosition 圆的Y坐标
     * @param radius 圆半径
     * @return
     */
    protected boolean isPutInCircle(double touchX, double touchY, double xPosition, double yPosition, double radius) {
        double diffX = xPosition - touchX;
        double diffY = yPosition - touchY;
        double dist = diffX * diffX + diffY * diffY;
        return dist < radius * radius;
    }



    /**
     * 添加环形菜单按钮
     * @param menuItems RadialMenuItem List
     * @return
     */
    public void addMenuItems(List<RingMenuItem> menuItems) {
        menuList.addAll(menuItems);
        determineWedges();
    }

    /**
     * 添加环形菜单按钮
     *
     * @param menuItem - RadialMenuItem.
     * @return
     */
    public void addMenuItem(RingMenuItem menuItem) {
        menuList.add(menuItem);
        determineWedges();
    }

    /**
     * 设置中间按钮
     * @param menuItem - center RadialMenuItem.
     * @return
     */
    public void setCenterCircle(RingMenuItem menuItem) {
        centerCircle = menuItem;
    }

    /**
     * 设置圆环大小
     * @param InnerRadius - 环形内圆半径
     * @param OuterRadius - 环形外圆半径
     */
    public void setRingRadius(int InnerRadius, int OuterRadius) {
        this.innerRadius = scalePX(InnerRadius);
        this.outerRadius = scalePX(OuterRadius);
        determineWedges();
    }


    /**
     * 设置中心圆形的大小
     * @param centerRadius - 中间圆形半径
     */
    public void setCenterCircleRadius(int centerRadius) {
        this.cRadius = scalePX(centerRadius);
        determineWedges();
    }

    /**
     * 设置文本大小
     * @param TextSize
     */
    public void setTextSize(int TextSize) {
        this.textSize = scalePX(TextSize);
    }


    /**
     * 设置环形按钮图标大小
     *
     * @param minIconSize - 环形按钮图标最小尺寸
     * @param maxIconSize - 环形按钮图标最大尺寸
     */
    public void setIconSize(int minIconSize, int maxIconSize) {
        this.minIconSize = scalePX(minIconSize);
        this.maxIconSize = scalePX(maxIconSize);
        determineWedges();
    }

    /**
     * 设置中心按钮图标大小
     *
     * @param minCenterIconSize - 中心按钮图标最小尺寸
     * @param maxCenterIconSize - 中心按钮图标最大尺寸
     */
    public void setCenterIconSize(int minCenterIconSize, int maxCenterIconSize) {
        this.minCenterIconSize = scalePX(minCenterIconSize);
        this.maxCenterIconSize = scalePX(maxCenterIconSize);
        determineWedges();
    }

    /**
     * 设置控件的中心坐标
     * @param xPosition dp
     * @param yPosition dp
     */
    public void setCenterLocation(int xPosition, int yPosition) {
        this.xPosition = scalePX(xPosition);
        this.yPosition = scalePX(yPosition);
        isAutoLocation = false;
        determineWedges();
    }


    /**设置环形按钮的间隙大小*/
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = scalePX(borderWidth);
        determineWedges();
    }

    /**
     * 设置按钮默认的颜色
     * @param color - Color value .
     * @param alpha - Alpha blend value.
     */
    public void setDefaultColor(int color, int alpha) {
        this.defaultColor = color;
        this.defaultAlpha = alpha;
    }

    /**
     * 设置变宽颜色以及控件的背景颜色
     * @param color - Color value .
     * @param alpha - Alpha blend value.
     */
    public void setBorderColor(int color, int alpha) {
        this.borderColor = color;
        this.borderAlpha = alpha;
    }

    /**
     * 设置按钮选中的颜色
     * @param color - Color value .
     * @param alpha - Alpha blend value.
     */
    public void setSelectedColor(int color, int alpha) {
        this.selectedColor = color;
        this.selectedAlpha = alpha;
    }

    /**
     * 设置按钮的禁用颜色
     *
     * @param color - Color value .
     * @param alpha - Alpha blend value.
     */
    public void setDisabledColor(int color, int alpha) {
        this.disabledColor = color;
        this.disabledAlpha = alpha;
    }

    /**
     * 设置文本颜色
     * @param color - Text color
     * @param alpha - Text alpha belnd value
     */
    public void setTextColor(int color, int alpha) {
        this.textColor = color;
        this.textAlpha = alpha;
    }

    /**刷新控件*/
    public void refresh(){
        invalidate();
    }

}