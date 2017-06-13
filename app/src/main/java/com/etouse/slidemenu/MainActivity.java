package com.etouse.slidemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivMenu;
    private SlidingMenu slidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(this);
        slidingMenu = (SlidingMenu) findViewById(R.id.slidingmenu);
    }

    @Override
    public void onClick(View view) {
        if (slidingMenu.isExpand()) {
            slidingMenu.close();
        } else {
            slidingMenu.expand();
        }
    }
}
