package com.example.flowlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private FlowLayout mFl;

    private String[] mValues = new String[]{"Hello", "Android Developer", "Welcome to Android Studio!", "Button", "Layout", "Hello", "Android",
            "Welcome", "Button", "Layout", "Hello", "Android", "Welcome", "Button", "Layout"};

    public void initData() {
        for (int i = 0; i < mValues.length; i++) {
            Button btn = new Button(this);

            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            btn.setText(mValues[i]);
            mFl.addView(btn, layoutParams);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        mFl = (FlowLayout) findViewById(R.id.fl_content);
    }
}
