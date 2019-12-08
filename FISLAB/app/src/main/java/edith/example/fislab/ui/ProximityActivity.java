package edith.example.fislab.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edith.example.fislab.R;

public class ProximityActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sProximity;
    private float xAct = 0;
    private float yAct = 0;
    private float zAct = 0;
    private float proximityVal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

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
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
