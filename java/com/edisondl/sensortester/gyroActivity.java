package com.edisondl.sensortester;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class gyroActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mGyro;
    TextView gyro_x;
    TextView gyro_y;
    TextView gyro_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gyro);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        gyro_x = findViewById(R.id.gyro_x);
        gyro_y = findViewById(R.id.gyro_y);
        gyro_z = findViewById(R.id.gyro_z);

        if(!(mGyro != null)){
            Toast.makeText(this,"자이로스코프 센서가 없습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGyro,
                SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int gyrouracy){
    }

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyro_x.setText(Float.toString(event.values[0]));
            gyro_y.setText(Float.toString(event.values[1]));
            gyro_z.setText(Float.toString(event.values[2]));
        }
    }
}
