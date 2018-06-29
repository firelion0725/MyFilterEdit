package com.leo.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.leo.widget.MyFilterEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyFilterEditText myFilterEditText = findViewById(R.id.edit_text);
    }
}
