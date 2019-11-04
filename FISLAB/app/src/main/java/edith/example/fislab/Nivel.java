package edith.example.fislab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Nivel extends AppCompatActivity implements SensorEventListener{
    private Sensor miSen;
    private SensorManager miSenMan;
    private SensorEventListener miSenLis;//evento de sensor se puede reimplementar como cualquier otro evento
    TextView txtX, txtY, txtZ;
    Button btnA,btnC;
    private float x=0,y=0,z=0,xCal,yCal,zCal;

    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    TextView axisX, axisY, axisZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel);

        axisX = (TextView) findViewById(R.id.txtX);
        axisY = (TextView) findViewById(R.id.txtY);
        axisZ = (TextView) findViewById(R.id.txtZ);
        btnA = findViewById(R.id.btnAct);
        btnC = findViewById(R.id.btnCal);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if(mGyroscope==null)
            Toast.makeText(this, "no gir", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "si gir", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }@Override
    public void onSensorChanged(SensorEvent event) {
        if(mGyroscope.getType() == Sensor.TYPE_ROTATION_VECTOR){
            x = (float) Math.toDegrees(2* Math.asin(event.values[0]));
            y = (float) Math.toDegrees(2 * Math.asin(event.values[1]));
            z = (float) Math.toDegrees(2 * Math.asin(event.values[2]));

            if(x<.001)
                x=0;
            if(y<.001)
                y=0;

            // axisX.setText("x: "+x);
            //axisY.setText("y: "+y);


        }
        // axisY.setText("Y: "+y);
        // axisZ.setText("Z: "+z);
    }@Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }@Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAct:
                Toast.makeText(this, "act", Toast.LENGTH_SHORT).show();
                axisX.setText("x: "+(x-xCal));
                axisY.setText("y: "+(y-yCal));
                break;
            case R.id.btnCal:
                Toast.makeText(this, "cal", Toast.LENGTH_SHORT).show();
                xCal=x;
                yCal=y;
                break;
        }
    }
}
