package edith.example.fislab;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Nivel extends AppCompatActivity {
    Sensor miSen;
    SensorManager miSenMan;
    SensorEventListener miSenLis;//evento de sensor se puede reimplementar como cualquier otro evento
    TextView txtX, txtY, txtZ;
    float x,y,z,xCal,yCal,zCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtX = findViewById(R.id.txtX);
        txtY = findViewById(R.id.txtY);
        txtZ = findViewById(R.id.txtZ);

        miSenMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        miSen = miSenMan.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (miSen==null){
            finish();
        }
        miSenLis = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                Sensor mySensor = sensorEvent.sensor;
                if (mySensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                    //los angulos funcionan value= sin(x/2) donde x es el angulo
                    //asi que para sacar el angulo es x = 2 * arcsin(value)
                    //pero lo devuelve en radianes y sin calibrar
                    x = (float) Math.toDegrees(2 * Math.asin(sensorEvent.values[0]));
                    y = (float) Math.toDegrees(2 * Math.asin(sensorEvent.values[1]));
                    z = (float) Math.toDegrees(2 * Math.asin(sensorEvent.values[2]));

                     /*
                System.out.println(" x: "+x);
                    System.out.println(" Y: "+y);
                    System.out.println(" Z: "+z);
                    */

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    private void start(){
        miSenMan.registerListener(miSenLis, miSen, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stop(){
        miSenMan.unregisterListener(miSenLis);
    }


    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnAct:
                txtX.setText(x+"");
                System.out.println("x: ");
                txtY.setText(y+"");
                txtZ.setText(z+"");
                break;
           /* case R.id.btnCal:
                xCal = x;
                yCal = y;
                zCal = z;
                break;*/
        }
    }
}
