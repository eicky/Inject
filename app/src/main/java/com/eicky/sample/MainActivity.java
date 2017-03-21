package com.eicky.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.eicky.ContentView;
import com.eicky.InjectUtils;
import com.eicky.OnClick;
import com.eicky.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.text)
    TextView mTextView;
    @OnClick({R.id.text, R.id.btn})
    void click(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mTextView.setText(simpleDateFormat.format(new Date()));
        Toast.makeText(this, "注入成功了!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this);
    }
}
