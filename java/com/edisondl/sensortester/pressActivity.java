package com.edisondl.sensortester;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class pressActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mPress;
    TextView sensor;
    TextView press;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.press);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mPress = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensor = findViewById(R.id.sensor);
        press = findViewById(R.id.press);

        if(!(mPress != null)) {
            Toast.makeText(this, "압력 센서가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mPress,
                SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            press.setText(Float.toString(event.values[0]));
        }
    }
}