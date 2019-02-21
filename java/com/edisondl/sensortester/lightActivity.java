package com.edisondl.sensortester;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class lightActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mLight;
    TextView sensor;
    TextView lux;
    TextView level;
    LinearLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor = findViewById(R.id.sensor);
        lux = findViewById(R.id.lux);
        level = findViewById(R.id.level);
        background = findViewById(R.id.background);
        if(!(mLight != null)){
            Toast.makeText(this,"빛(조도) 센서가 없습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLight,
                SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lux.setText(Float.toString(event.values[0]));

            if (20 >= Double.parseDouble(Float.toString(event.values[0]))) { //만약 인식되면
                level.setText("LEVEL1");
                background.setBackgroundColor(0xffff0000);
            }
            else if (21 <= Double.parseDouble(Float.toString(event.values[0]))&& Double.parseDouble(Float.toString(event.values[0])) <=150){
                level.setText("LEVEL2");
                background.setBackgroundColor(0xffff8C00);
            }
            else if (151 <= Double.parseDouble(Float.toString(event.values[0]))&& Double.parseDouble(Float.toString(event.values[0])) <=1500){
                level.setText("LEVEL3");
                background.setBackgroundColor(0xffffD700);
            }
            else if (1501 <= Double.parseDouble(Float.toString(event.values[0]))&& Double.parseDouble(Float.toString(event.values[0])) <=15000){
                level.setText("LEVEL4");
                background.setBackgroundColor(0xff00ff00);
            }
            else if (15001 <= Double.parseDouble(Float.toString(event.values[0]))&& Double.parseDouble(Float.toString(event.values[0])) <=150000){
                level.setText("LEVEL5");
                background.setBackgroundColor(0xff0000ff);
            }
            else if (Double.parseDouble(Float.toString(event.values[0])) <= 150001) {
                level.setText("LEVEL6");
                background.setBackgroundColor(0xffff00ff);
            }
        }
    }
}