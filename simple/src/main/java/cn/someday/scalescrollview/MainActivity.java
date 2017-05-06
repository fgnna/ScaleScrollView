package cn.someday.scalescrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.someday.scalescrollview.core.ScaleScrollView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScaleScrollView scaleScrollView = (ScaleScrollView) findViewById(R.id.ScaleScrollView);
        scaleScrollView.setOption(10,200);
    }
}
