package edith.example.fislab.activities;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import edith.example.fislab.R;

public class NivelActivity extends AppCompatActivity implements SensorEventListener, Button.OnClickListener {
    private ScatterChart graficaNivel;
    private TextView axisX, axisY, axisZ;
    //Sensores
    private SensorManager sensorManager;
    private Sensor sRotVec, sGameRotVec;
    private float xAct = 0;
    private float yAct = 0;
    private float zAct = 0;
    private float xCal, yCal, zCal;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel);
        //Vincular componentes
        Button btnDatosNivel = findViewById(R.id.btnDatosNivel);
        Button btnCalibrarNivel = findViewById(R.id.btnCalibrarNivel);
        graficaNivel = findViewById(R.id.graficaNivel);
        axisX = findViewById(R.id.txtXNivel);
        axisY = findViewById(R.id.txtYNivel);
        axisZ = findViewById(R.id.txtZNivel);
        //Obtener servicio del sistema
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Verificar que el manager no esté vacío
        if (sensorManager != null) {
            sRotVec = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            sGameRotVec = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        }
        //Establecer color y texto cuando no hay datos
        graficaNivel.setNoDataTextColor(Color.BLACK);
        graficaNivel.setNoDataText("Toca el botón para ver datos.");
        //Deshabilitar descripción de la gráfica
        graficaNivel.getDescription().setEnabled(false);
        //Habilitar gestos
        graficaNivel.setTouchEnabled(true);
        graficaNivel.setDragEnabled(true);
        graficaNivel.setScaleEnabled(true);
        graficaNivel.setDrawGridBackground(false);
        graficaNivel.setPinchZoom(true);
        //Establecer características de la leyenda
        Legend legend = graficaNivel.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setDrawInside(false);
        legend.setXOffset(5f);
        legend.setYEntrySpace(2f);
        //Deshabilitar eje derecho
        graficaNivel.getAxisRight().setEnabled(false);
        //Escuchadores
        btnDatosNivel.setOnClickListener(this);
        btnCalibrarNivel.setOnClickListener(this);
    }

    @Override protected void onResume() {
        super.onResume();
        //Verificar que exista el sensor
        if (sRotVec != null) {
            sensorManager.registerListener(this, sRotVec, SensorManager.SENSOR_DELAY_NORMAL);
        }
        //Verificar que exista el sensor
        if (sGameRotVec != null) {
            sensorManager.registerListener(this, sGameRotVec, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override protected void onPause() {
        super.onPause();
        //Anular registro del escuchador
        sensorManager.unregisterListener(this);
    }

    @Override public void onSensorChanged(SensorEvent event) {
        //Utilizar Rotation Vector si Game Rotation Vector es nulo
        if ((sRotVec != null) && (sGameRotVec == null)) {
            //Convertir valores crudos obtenidos
            if (sRotVec.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                xAct = (float) Math.toDegrees(2 * Math.asin(event.values[0]));
                yAct = (float) Math.toDegrees(2 * Math.asin(event.values[1]));
                zAct = (float) Math.toDegrees(2 * Math.asin(event.values[2]));
                //Establecer en 0 cuando los valores son muy pequeños
                if (xAct < .001) { xAct = 0; }
                if (yAct < .001) { yAct = 0; }
                if (zAct < .001) { zAct = 0; }
            }
        }
        //Utilizar Game Rotation Vector si Rotation Vector es nulo
        if ((sGameRotVec != null) && (sRotVec == null)) {
            //Convertir valores crudos obtenidos
            if (sGameRotVec.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
                xAct = (float) Math.toDegrees(2 * Math.asin(event.values[0]));
                yAct = (float) Math.toDegrees(2 * Math.asin(event.values[1]));
                zAct = (float) Math.toDegrees(2 * Math.asin(event.values[2]));
                //Establecer en 0 cuando los valores son muy pequeños
                if (xAct < .001) { xAct = 0; }
                if (yAct < .001) { yAct = 0; }
                if (zAct < .001) { zAct = 0; }
            }
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override public void onClick(View view) {
        //Establecer valores de los ejes
        float x = xAct - xCal;
        float y = yAct - yCal;
        float z = zAct - zCal;
        String X = "X: " + x + "°";
        String Y = "Y: " + y + "°";
        String Z = "Z: " + z + "°";
        switch (view.getId()) {
            case R.id.btnDatosNivel:
                //Mostrar valores en las etiquetas
                axisX.setText(X);
                axisY.setText(Y);
                axisZ.setText(Z);
                //Mostrar valores en la gráfica
                datosGrafica(x, y, z);
                break;
            case R.id.btnCalibrarNivel:
                //Actualizar valores de calibración
                xCal = xAct;
                yCal = yAct;
                zCal = zAct;
                break;
        }
    }

    private void datosGrafica(float x, float y, float z) {
        //Crear array list para cada eje
        ArrayList<Entry> alEjeX = new ArrayList<>();
        ArrayList<Entry> alEjeY = new ArrayList<>();
        ArrayList<Entry> alEjeZ = new ArrayList<>();
        //Añadir entradas a los array list
        alEjeX.add(new Entry(-1f, x));
        alEjeY.add(new Entry(0f, y));
        alEjeZ.add(new Entry(1f, z));
        //Establecer sets de datos por cada eje
        ScatterDataSet setEjeX = new ScatterDataSet(alEjeX, "X");
        setEjeX.setColors(ColorTemplate.MATERIAL_COLORS[0]);
        setEjeX.setValueTextColor(Color.BLACK);
        setEjeX.setValueTextSize(12f);
        setEjeX.setHighLightColor(Color.rgb(46, 204, 113));
        ScatterDataSet setEjeY = new ScatterDataSet(alEjeY, "Y");
        setEjeY.setColors(ColorTemplate.MATERIAL_COLORS[3]);
        setEjeY.setValueTextColor(Color.BLACK);
        setEjeY.setValueTextSize(12f);
        setEjeY.setHighLightColor(Color.rgb(52, 152, 219));
        ScatterDataSet setEjeZ = new ScatterDataSet(alEjeZ, "Z");
        setEjeZ.setColors(ColorTemplate.MATERIAL_COLORS[2]);
        setEjeZ.setValueTextColor(Color.BLACK);
        setEjeZ.setValueTextSize(12f);
        setEjeZ.setHighLightColor(Color.rgb(231, 76, 60));
        //Crear array list de sets
        ArrayList<IScatterDataSet> setEjes = new ArrayList<>();
        //Añadir sets de datos
        setEjes.add(setEjeX);
        setEjes.add(setEjeY);
        setEjes.add(setEjeZ);
        //Crear datos de la gráfica con los ejes
        ScatterData datosGrafica = new ScatterData(setEjes);
        //Establecer datos en la gráfica
        graficaNivel.setData(datosGrafica);
        //Permite mostrar los datos en cuanto el botón es presionado
        graficaNivel.invalidate();
    }
}
