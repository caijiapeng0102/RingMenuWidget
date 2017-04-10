# RingMenuWidget
android  ring menu 环形菜单控件

## 效果 
![](https://github.com/caijiapeng0102/RingMenuWidget/blob/master/screenshots/view.png)
 
## 引用
1、直接下载项目 将ringMenulibrary 导入项目中<br>
2、gradle(暂未审核)
```java
  compile 'com.caijiapeng.library.ringmenu:RingMenuLibrary:0.0.1'
 ```
 
 ## 使用
 ```xml
 <com.caijiapeng.library.ringmenu.RingMenuWidget
       android:id="@+id/ring_menu"
       android:layout_width="300dip"
       android:layout_height="300dip"/>
 ```
 
```java
    public RingMenuItem mMenu1, mMenu2, mMenu3, mMenu4, mMenu5;
    public RingMenuItem mCenterMenu;
    
    mRingMenuWidget = (RingMenuWidget) findViewById(R.id.ring_menu);
    mMenu1 = new RingMenuItem("menu1");
    mMenu1.setOnMenuItemPressed(new RingMenuItem.RingMenuItemClickListener() {
        @Override
        public void execute() {
            Toast.makeText(MainActivity.this, "menu1 click", Toast.LENGTH_SHORT).show();
        }
    });
    mMenu2 = new RingMenuItem("menu2");
    mMenu2.setOnMenuItemPressed(new RingMenuItem.RingMenuItemClickListener() {
        @Override
        public void execute() {
            Toast.makeText(MainActivity.this, "menu2 click", Toast.LENGTH_SHORT).show();
        }
    });
    mMenu3 = new RingMenuItem("menu3");
    mMenu3.setOnMenuItemPressed(new RingMenuItem.RingMenuItemClickListener() {
        @Override
        public void execute() {
            Toast.makeText(MainActivity.this, "menu3 click", Toast.LENGTH_SHORT).show();
        }
    });
    mMenu4 = new RingMenuItem("menu4");
    mMenu4.setOnMenuItemPressed(new RingMenuItem.RingMenuItemClickListener() {
        @Override
        public void execute() {
            Toast.makeText(MainActivity.this, "menu4 click", Toast.LENGTH_SHORT).show();
        }
    });
    mMenu5 = new RingMenuItem("menu5");
    mMenu5.setOnMenuItemPressed(new RingMenuItem.RingMenuItemClickListener() {
        @Override
        public void execute() {
            Toast.makeText(MainActivity.this, "menu5 click", Toast.LENGTH_SHORT).show();
        }
    });
    mCenterMenu = new RingMenuItem(null);
    mCenterMenu.setDisplayIcon(R.drawable.icon_smail);
    mCenterMenu.setOnMenuItemPressed(new RingMenuItem.RingMenuItemClickListener() {
        @Override
        public void execute() {
            Toast.makeText(MainActivity.this, "center click", Toast.LENGTH_SHORT).show();
        }
    });
  //        mRingMenuWidget.setIconSize(15, 30); //设置环形图标大小
    mRingMenuWidget.setCenterIconSize(50, 80); //设置中心图标大小
    mRingMenuWidget.setRingRadius(64, 116);   //设置环形大小
    mRingMenuWidget.setCenterCircleRadius(56); //设置中心圆形的大小
    mRingMenuWidget.setBorderWidth(8); //设置环形按钮的间隙大小
    mRingMenuWidget.setTextSize(13); //设置文本大小
    mRingMenuWidget.setBorderColor(Color.WHITE, 225); //设置边宽颜色以及控件的背景颜色
    mRingMenuWidget.setDefaultColor(Color.parseColor("#00846D"), 255); //设置按钮默认的颜色
    mRingMenuWidget.setSelectedColor(Color.parseColor("#09473A"), 255); //设置按钮选中的颜色
    mRingMenuWidget.setDisabledColor(Color.parseColor("#969696"), 255); //设置按钮禁用的颜色
    mRingMenuWidget.setTextColor(Color.WHITE, 255); //设置文本颜色

  //        mRingMenuWidget.setCenterLocation(150, 150); //设置控件中心坐标，根据父控件相对定位
 
    mRingMenuWidget.setCenterCircle(mCenterMenu); //设置圆心按钮
    //添加外环按钮
    mRingMenuWidget.addMenuItems(new ArrayList<RingMenuItem>() {
        {
            add(mMenu1);
            add(mMenu2);
            add(mMenu3);
            add(mMenu4);
            add(mMenu5);
        }
    });
    
    
```
 
