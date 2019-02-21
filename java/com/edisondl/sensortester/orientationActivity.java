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

public class orientationActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mOrientation;
    TextView sensor;
    TextView azimuth;
    TextView pitch;
    TextView roll;
    ImageView compass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensor = findViewById(R.id.sensor);
        azimuth = findViewById(R.id.azimuth);
        pitch = findViewById(R.id.pitch);
        roll = findViewById(R.id.roll);
        compass = findViewById(R.id.compass);
        if(!(mOrientation != null)) {
            Toast.makeText(this, "가속도 센서나 자기장 센서가 없습니다.", Toast.LENGTH_SHORT).show();
        }
        }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mOrientation,
                SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            compass.setRotation(360 - (event.values[0]));
            azimuth.setText(Float.toString(event.values[0]));
            pitch.setText(Float.toString(event.values[1]));
            roll.setText(Float.toString(event.values[2]));
        }
    }
}