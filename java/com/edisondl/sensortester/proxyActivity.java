package com.edisondl.sensortester;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class proxyActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProxy;
    TextView sensor;
    TextView distance;
    LinearLayout background;
    private Vibrator vibrator;
    CheckBox vibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proxy);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mProxy = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensor = findViewById(R.id.sensor);
        distance = findViewById(R.id.distance);
        background = findViewById(R.id.background);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibration = findViewById(R.id.vibration);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProxy,
                SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            distance.setText(Float.toString(event.values[0])+" cm");
            if (Double.parseDouble(Float.toString(event.values[0])) <= 1) { //만약 인식되면
                sensor.setText("인식됨");
                if (vibration.isChecked()){
                    long[] pattern = {0,100};
                    vibrator.vibrate(pattern,0);
                }
                background.setBackgroundColor(0xff00ff00);
            }
            else {
                sensor.setText("떨어짐");
                background.setBackgroundColor(0xffffffff);
                vibrator.cancel();
            }
        }
    }
}