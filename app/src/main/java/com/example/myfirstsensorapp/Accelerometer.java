package com.example.myfirstsensorapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "Accelerometer";

    private SensorManager sensorManager;
    Sensor accelerometer;

    TextView xValue, yValue, zValue, rotation;
    MediaPlayer sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        Log.d(TAG, "onCreate");
        sound = MediaPlayer.create(this, R.raw.whip);


        xValue = (TextView)findViewById(R.id.xValue);
        yValue = (TextView)findViewById(R.id.yValue);
        zValue = (TextView)findViewById(R.id.zValue);
        rotation = (TextView)findViewById(R.id.rotation);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Accelerometer.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        double x = sensorEvent.values[0];
        double y = sensorEvent.values[1];
        double z = sensorEvent.values[2];

        String.format("%.2f", sensorEvent.values[0]);

        xValue.setText("X: " + String.format("%.2f",sensorEvent.values[0]));
        yValue.setText("Y: " + String.format("%.2f",sensorEvent.values[1]));
        zValue.setText("Z: " + String.format("%.2f",sensorEvent.values[2]));

        String text = "";

        if(x < 1.0 && x > -1.0){
            text += " Står upp\n";
        }else if(x >= 1.0){
            text += " Lutar åt vänster\n";
        }else{
            text += " Lutar åt höger\n";
        }

        if(y > 0.0){
            text += " Mobilen är rättvänd\n";
        }else{
            text += " Mobilen är uppochner\n";
            sound.start();
        }

        if(z < 0.0){
            text += " Skärmen pekar nedåt\n";
            vibrate();
        }else{
            text += " Skärmen pekar uppåt\n";
        }

        rotation.setText(text);

    }

    public void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void stop() {
        sensorManager.unregisterListener(this,accelerometer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
