package com.caijiapeng.ringmenuwidget;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.caijiapeng.library.ringmenu.RingMenuItem;
import com.caijiapeng.library.ringmenu.RingMenuWidget;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RingMenuWidget mRingMenuWidget;

    public RingMenuItem mMenu1, mMenu2, mMenu3, mMenu4, mMenu5;
    public RingMenuItem mCenterMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//        mRingMenuWidget.setIconSize(15, 30);
        mRingMenuWidget.setCenterIconSize(50, 80);
        mRingMenuWidget.setRingRadius(64, 116);
        mRingMenuWidget.setCenterCircleRadius(56);
        mRingMenuWidget.setBorderWidth(8);
        mRingMenuWidget.setTextSize(13);
        mRingMenuWidget.setBorderColor(Color.WHITE, 225);
        mRingMenuWidget.setDefaultColor(Color.parseColor("#00846D"), 255);
        mRingMenuWidget.setSelectedColor(Color.parseColor("#09473A"), 255);
        mRingMenuWidget.setDisabledColor(Color.parseColor("#969696"), 255);
        mRingMenuWidget.setTextColor(Color.WHITE, 255);

//        mRingMenuWidget.setCenterLocation(150, 150);

        mRingMenuWidget.setCenterCircle(mCenterMenu);
        mRingMenuWidget.addMenuItems(new ArrayList<RingMenuItem>() {
            {
                add(mMenu1);
                add(mMenu2);
                add(mMenu3);
                add(mMenu4);
                add(mMenu5);
            }
        });
    }
}
