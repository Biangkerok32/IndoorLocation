package com.example.andre.indoorlocation;

import android.content.res.Resources;
import android.sax.RootElement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv1 = (TextView)findViewById(R.id.textView2);
        tv1.setText("Hello, welcome to Indoor Location");

    }
}
