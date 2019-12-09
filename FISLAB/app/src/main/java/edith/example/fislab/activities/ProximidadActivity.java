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
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edith.example.fislab.R;

public class ProximidadActivity extends AppCompatActivity implements SensorEventListener, ImageButton.OnClickListener {
    private ImageButton btnStart;
    private TextView txtVwDistancia;
    //Sensor
    private SensorManager sensorManager;
    private Sensor sProximity;
    //Cronómetro
    private Chronometer cronometro;
    private boolean correr;
    private long detenerse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximidad);
        //Vincular componentes
        cronometro = findViewById(R.id.cronometer);
        ImageButton btnRestart = findViewById(R.id.btnRestart);
        btnStart = findViewById(R.id.btnStart);
        ImageButton btnStop = findViewById(R.id.btnStop);
        txtVwDistancia = findViewById(R.id.txtVwDistancia);
        //Obtener servicio del sistema
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Verificar que el manager no esté vacío
        if (sensorManager != null) {
            sProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        //Escuchadores
        btnRestart.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Verificar que exista el sensor
        if (sProximity != null){
            sensorManager.registerListener(this, sProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Anular registro del escuchador
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        String sVal = "";
        if (sProximity != null) {
            //Obtener valor del sensor
            float proximityVal = sensorEvent.values[0];
            sVal = sProximity.getMaximumRange() + " cm";
            //Detener cronómetro y cambiar color de botón si el objeto se encuentra a una distancia menor a la máxima
            if (proximityVal < sProximity.getMaximumRange()) {
                btnStart.setBackgroundColor(Color.RED);
                stopChronometer();
            } else { btnStart.setBackgroundColor(Color.GREEN); }
        }
        //Mostrar distancia máxima
        txtVwDistancia.setText(sVal);
    }

    @Override public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override public void onClick(View v) {
        //Cambiar estado del cronómetro dependiendo del botón presionado
        switch (v.getId()) {
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
        //Iniciar cronómetro
        if (!correr){
            cronometro.setBase(SystemClock.elapsedRealtime() - detenerse);
            cronometro.start();
            correr = true;
        }
    }

    public void restartChronometer(){
        //Reiniciar cronómetro
        cronometro.setBase(SystemClock.elapsedRealtime());
        detenerse = 0;
    }

    private void stopChronometer() {
        //Detener cronómetro
        if (correr){
            cronometro.stop();
            detenerse = SystemClock.elapsedRealtime() - cronometro.getBase();
            correr = false;
        }
    }
}
