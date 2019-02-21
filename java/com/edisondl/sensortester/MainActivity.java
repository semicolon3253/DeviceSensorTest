package com.edisondl.sensortester;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAcc;
    private Sensor mGyro;
    private Sensor mProxy;
    private Sensor mLight;
    private Sensor mMag;
    private Sensor mOrientation;
    private Sensor mPress;
    private Sensor mTemp;
    private static final int VERSION_DIALOG = 1;
    private static final int NOTICE_DIALOG=2;
    private static final int INFO_DIALOG=3;
    private String version = "";
    private String notice = "";
    private String download = "";
    FirebaseDatabase database;
    DatabaseReference versioncheck;
    DatabaseReference noticeview;
    DatabaseReference downloadurl;

    TextView acc;
    TextView gyro;
    TextView proxy;
    TextView light;
    TextView mag;
    TextView orientation;
    TextView press;
    TextView temp;


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case VERSION_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("업데이트가 필요 합니다")
                        .setMessage("현재 버전: 1.0.0 \n최신 버전: " + version +"\n\n업데이트 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){
                                Intent intent = null;
                                intent = new Intent(intent.ACTION_VIEW, Uri.parse(download));
                                startActivity(intent);
                                }
                        })
                        .setNegativeButton("아니요",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                return alert;
        }
        switch (id) {
            case NOTICE_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("공지사항")
                        .setMessage(notice)
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton){
                                    }
                                });
                AlertDialog alert = builder.create();
                return alert;
        }
        switch (id) {
            case INFO_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("앱 정보")
                        .setMessage("제작 시작: 2018년01월20일\n제작 종료: 2018년03월10일\n마지막 업데이트: 2018년03월10일\n\n제작자:edisondl\n© 2018 Edisondl ALL RIGHTS RESERVED")
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton){
                                    }
                                });
                AlertDialog alert = builder.create();
                return alert;
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mProxy = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mPress = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mTemp = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        acc = findViewById(R.id.acc);
        gyro = findViewById(R.id.gyro);
        proxy = findViewById(R.id.proxy);
        light = findViewById(R.id.light);
        mag = findViewById(R.id.mag);
        orientation = findViewById(R.id.orientation);
        press = findViewById(R.id.press);
        temp = findViewById(R.id.temp);
        database = FirebaseDatabase.getInstance();
        versioncheck = database.getReference("/applist/SensorTester/versioncheck");
        noticeview = database.getReference("/applist/SensorTester/notice");
        downloadurl = database.getReference("/applist/SensorTester/downloadurl");

        downloadurl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                download = value;
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
            }
        });


        versioncheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                version = value;
                if(!version.equals("1.0.0")) {
                    versioncheck.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            version = value;
                            showDialog(VERSION_DIALOG);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {}
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(),"에러",Toast.LENGTH_SHORT).show();
            }
        });

        noticeview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                notice = value;
                if(notice != null) {
                    showDialog(NOTICE_DIALOG);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(),"에러",Toast.LENGTH_SHORT).show();
            }
        });


        if(!(mAcc != null)){
            acc.setText("사용 불가능");
            acc.setTextColor(0xFFFF0000);
        }
        else {
            acc.setText("사용 가능");
            acc.setTextColor(0xFF0000FF);
        }
        if(!(mGyro != null)){
            gyro.setText("사용 불가능");
            gyro.setTextColor(0xFFFF0000);
        }
        else {
            gyro.setText("사용 가능");
            gyro.setTextColor(0xFF0000FF);
        }

        if(!(mProxy != null)){
            proxy.setText("사용 불가능");
            proxy.setTextColor(0xFFFF0000);
        }
        else {
            proxy.setText("사용 가능");
            proxy.setTextColor(0xFF0000FF);
        }

        if(!(mLight != null)){
            light.setText("사용 불가능");
            light.setTextColor(0xFFFF0000);
        }
        else {
            light.setText("사용 가능");
            light.setTextColor(0xFF0000FF);
        }

        if(!(mMag != null)){
            mag.setText("사용 불가능");
            mag.setTextColor(0xFFFF0000);
        }
        else {
            mag.setText("사용 가능");
            mag.setTextColor(0xFF0000FF);
        }

        if(!(mOrientation != null)){
            orientation.setText("사용 불가능");
            orientation.setTextColor(0xFFFF0000);
        }
        else {
            orientation.setText("사용 가능");
            orientation.setTextColor(0xFF0000FF);
        }

        if(!(mPress != null)){
            press.setText("사용 불가능");
            press.setTextColor(0xFFFF0000);
        }
        else {
            press.setText("사용 가능");
            press.setTextColor(0xFF0000FF);
        }

        if(!(mTemp != null)){
            temp.setText("사용 불가능");
            temp.setTextColor(0xFFFF0000);
        }
        else {
            temp.setText("사용 가능");
            temp.setTextColor(0xFF0000FF);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                Intent email = new Intent(Intent.ACTION_SEND);
                String[] address = {"ji64922@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "[기기 센서 테스트]");
                email.setType("message/rfc855");
                startActivity(Intent.createChooser(email, "이메일 보낼 앱 선택: "));
                return true;
            case R.id.support:
                showDialog(INFO_DIALOG);
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAcc,
                SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }

    public void onSensorChanged(SensorEvent event) {

        }





    public void acc (View v){
            Intent intent = new Intent(MainActivity.this,
                    accActivity.class);
            startActivity(intent);
    }
    public void gyro (View v){
        Intent intent = new Intent(MainActivity.this,
                gyroActivity.class);
        startActivity(intent);
    }
    public void light (View v){
        Intent intent = new Intent(MainActivity.this,
                lightActivity.class);
        startActivity(intent);
    }
    public void mag (View v){
        Intent intent = new Intent(MainActivity.this,
                magActivity.class);
        startActivity(intent);
    }
    public void orientation (View v){
        Intent intent = new Intent(MainActivity.this,
                orientationActivity.class);
        startActivity(intent);
    }
    public void press (View v){
        Intent intent = new Intent(MainActivity.this,
                pressActivity.class);
        startActivity(intent);
    }
    public void proxy (View v){
        Intent intent = new Intent(MainActivity.this,
                proxyActivity.class);
        startActivity(intent);
    }
    public void temp (View v){
        Intent intent = new Intent(MainActivity.this,
                tempActivity.class);
        startActivity(intent);
    }


    }