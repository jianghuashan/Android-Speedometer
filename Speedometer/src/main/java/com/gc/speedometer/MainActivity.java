package com.gc.speedometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gc.speedometer.view.SpeedView;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private SpeedView speedView;
    private Button button1,button2,button3,button4,button5,button6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speedView = (SpeedView) findViewById(R.id.speed_view);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1: //加速减速
                speedView.gotoRotate();
                break;
            case R.id.button2: //30km/h
                speedView.setSpeedValue(30.0);
                break;
            case R.id.button3: //90km/h
                speedView.setSpeedValue(90.0);
                break;
            case R.id.button4: //急刹车
                speedView.setSpeedValue(0);
                break;
            case R.id.button5: //更新总里程
                speedView.setTotalMileage(1000.0);
                break;
            case R.id.button6: //获取当前速度值
                double speedValue = speedView.getSpeedValue();
                Toast.makeText(getApplicationContext(),"当前速度值:"+speedValue+"km/h",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
