package edith.example.fislab.activities;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edith.example.fislab.R;

public class ProximidadActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sProximity;
    private float xAct = 0;
    private float yAct = 0;
    private float zAct = 0;
    private float proximityVal;
    private TextView txtVwTime;
    private long time_start;
    private long time_end;
    private Chronometer cronometro;
    private boolean correr;
    private long detenerse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximidad);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        cronometro = findViewById(R.id.cronometer);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sProximity != null){
            sensorManager.registerListener(this, sProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sProximity!=null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sProximity != null) {
            //Convertir valores crudos obtenidos
            proximityVal = sensorEvent.values[0];
            if(proximityVal < sProximity.getMaximumRange()){
                findViewById(R.id.btnStart).setBackgroundColor(Color.BLACK);
                stopChronometer();
            }
            else findViewById(R.id.btnStart).setBackgroundColor(Color.WHITE);
        }

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnStart:
                startChronometer();
                break;
            case R.id.btnRestart:
                restartChronometer();
                break;
            case R.id.btnStop:
                stopChronometer();
                break;
        }

    }

    private void startChronometer() {
        if (!correr){
            cronometro.setBase(SystemClock.elapsedRealtime()-detenerse);
            cronometro.start();
            correr=true;
        }
    }

    public void restartChronometer(){
        cronometro.setBase(SystemClock.elapsedRealtime());
        detenerse=0;
    }

    private void stopChronometer() {
        if (correr){
            cronometro.stop();
            detenerse = SystemClock.elapsedRealtime() - cronometro.getBase();
            correr = false;
        }
    }
}
