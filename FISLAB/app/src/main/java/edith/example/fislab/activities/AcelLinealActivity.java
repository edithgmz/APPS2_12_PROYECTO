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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import edith.example.fislab.R;

public class AcelLinealActivity extends AppCompatActivity implements SensorEventListener, Button.OnClickListener {
    private LineChart graficaAcelerometro;
    private TextView axisX, axisY, axisZ;
    //Sensores
    private SensorManager sensorManager;
    private Sensor sAcel;
    private float xAct = 0;
    private float yAct = 0;
    private float zAct = 0;
    private float xCal, yCal, zCal;
    private Thread hiloX, hiloY, hiloZ;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acel_lineal);
        //Vincular componentes
        Button btnDatosAcel = findViewById(R.id.btnDatosAcel);
        Button btnCalibrarAcel = findViewById(R.id.btnCalibrarAcel);
        Button btnLimpiarAcel = findViewById(R.id.btnLimpiarAcel);
        graficaAcelerometro = findViewById(R.id.graficaAcelerometro);
        axisX = findViewById(R.id.txtXAcel);
        axisY = findViewById(R.id.txtYAcel);
        axisZ = findViewById(R.id.txtZAcel);
        //Obtener servicio del sistema
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Verificar que el manager no esté vacío
        if (sensorManager != null) {
            sAcel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        }
        //Establecer descripción de la gráfica
        graficaAcelerometro.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("1 dato capturado cada 250 milisegundos");
        graficaAcelerometro.setDescription(description);
        //Habilitar gestos
        graficaAcelerometro.setTouchEnabled(true);
        graficaAcelerometro.setDragEnabled(true);
        graficaAcelerometro.setScaleEnabled(true);
        graficaAcelerometro.setDrawGridBackground(false);
        graficaAcelerometro.setPinchZoom(true);
        //Establecer características de la leyenda
        Legend legend = graficaAcelerometro.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setDrawInside(false);
        legend.setXOffset(5f);
        legend.setYEntrySpace(2f);
        //Deshabilitar eje derecho
        graficaAcelerometro.getAxisRight().setEnabled(false);
        //Establecer características del eje Y
        YAxis ejeY = graficaAcelerometro.getAxisLeft();
        ejeY.setDrawGridLines(true);
        //Agregar datos vacíos
        LineData datos = new LineData();
        datos.setValueTextColor(Color.BLACK);
        graficaAcelerometro.setData(datos);
        //Escuchadores
        btnDatosAcel.setOnClickListener(this);
        btnCalibrarAcel.setOnClickListener(this);
        btnLimpiarAcel.setOnClickListener(this);
    }

    @Override protected void onResume() {
        super.onResume();
        //Verificar que exista el sensor
        if (sAcel != null) {
            sensorManager.registerListener(this, sAcel, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override protected void onPause() {
        super.onPause();
        //Anular registro del escuchador
        sensorManager.unregisterListener(this);
        //Interrumpir hilos
        if (hiloX != null) {
            hiloX.interrupt();
        }
        if (hiloY != null) {
            hiloY.interrupt();
        }
        if (hiloZ != null) {
            hiloZ.interrupt();
        }
    }

    @Override public void onSensorChanged(SensorEvent event) {
        if ((sAcel != null)) {
            if (sAcel.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                xAct = event.values[0];
                yAct = event.values[1];
                zAct = event.values[2];
                //Establecer en 0 cuando los valores son muy pequeños
                if (xAct < .01) { xAct = 0; }
                if (yAct < .01) { yAct = 0; }
                if (zAct < .01) { zAct = 0; }
            }
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDatosAcel:
                //Agregar valores a la gráfica
                datosEjeX();
                datosEjeY();
                datosEjeZ();
                break;
            case R.id.btnCalibrarAcel:
                //Actualizar valores de calibración
                xCal = xAct;
                yCal = yAct;
                zCal = zAct;
                break;
            case R.id.btnLimpiarAcel:
                //Borrar valores graficados
                graficaAcelerometro.clearValues();
                break;
        }
    }

    private void datosEjeX() {
        //Interrumpir hilo
        if (hiloX != null) {
            hiloX.interrupt();
        }
        //Definir runnable para añadir datos a la gráfica
        final Runnable runnable = new Runnable() {
            @Override public void run() {
                datoEjeX();
            }
        };
        //Definir la cantidad de veces que se ejecutará el hilo
        hiloX = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 100; i++) {
                    //No generar runnables basura en el hilo
                    runOnUiThread(runnable);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Iniciar hilo
        hiloX.start();
    }

    private void datosEjeY() {
        //Interrumpir hilo
        if (hiloY != null) {
            hiloY.interrupt();
        }
        //Definir runnable para añadir datos a la gráfica
        final Runnable runnable = new Runnable() {
            @Override public void run() {
                datoEjeY();
            }
        };
        //Definir la cantidad de veces que se ejecutará el hilo
        hiloY = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 100; i++) {
                    //No generar runnables basura en el hilo
                    runOnUiThread(runnable);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Iniciar hilo
        hiloY.start();
    }

    private void datosEjeZ() {
        //Interrumpir hilo
        if (hiloZ != null) {
            hiloZ.interrupt();
        }
        //Definir runnable para añadir datos a la gráfica
        final Runnable runnable = new Runnable() {
            @Override public void run() {
                datoEjeZ();
            }
        };
        //Definir la cantidad de veces que se ejecutará el hilo
        hiloZ = new Thread(new Runnable() {
            @Override public void run() {
                for (int i = 0; i < 100; i++) {
                    //No generar runnables basura en el hilo
                    runOnUiThread(runnable);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Iniciar hilo
        hiloZ.start();
    }

    private void datoEjeX() {
        //Obtener datos de la gráfica
        LineData datosEjeX = graficaAcelerometro.getLineData();
        //Establecer valor del eje
        float x = xAct - xCal;
        String X = "X: " + x + "\nm/s^2";
        //Definir set de datos para el eje X
        ILineDataSet setEjeX = datosEjeX.getDataSetByLabel("X", false);
        if (setEjeX == null) {
            setEjeX = setEjeX();
            datosEjeX.addDataSet(setEjeX);
        }
        datosEjeX.addEntry(new Entry(setEjeX.getEntryCount(), xAct - 1), 0);
        datosEjeX.notifyDataChanged();
        graficaAcelerometro.notifyDataSetChanged();
        //Mostrar valor en la etiqueta
        axisX.setText(X);
        //Mover vista hacia el último valor
        graficaAcelerometro.moveViewToX(datosEjeX.getEntryCount());
    }

    private void datoEjeY() {
        //Obtener datos de la gráfica
        LineData datosEjeY = graficaAcelerometro.getLineData();
        //Establecer valor del eje
        float y = yAct - yCal;
        String Y = "Y: " + y + "\nm/s^2";
        //Definir set de datos para el eje Y
        ILineDataSet setEjeY = datosEjeY.getDataSetByLabel("Y", false);
        if (setEjeY == null) {
            setEjeY = setEjeY();
            datosEjeY.addDataSet(setEjeY);
        }
        datosEjeY.addEntry(new Entry(setEjeY.getEntryCount(), yAct), 1);
        datosEjeY.notifyDataChanged();
        graficaAcelerometro.notifyDataSetChanged();
        //Mostrar valor en la etiqueta
        axisY.setText(Y);
        //Mover vista hacia el último valor
        graficaAcelerometro.moveViewToX(datosEjeY.getEntryCount());
    }

    private void datoEjeZ() {
        //Obtener datos de la gráfica
        LineData datosEjeZ = graficaAcelerometro.getLineData();
        //Establecer valor del eje
        float z = zAct - zCal;
        String Z = "Z: " + z + "\nm/s^2";
        //Definir set de datos para el eje Z
        ILineDataSet setEjeZ = datosEjeZ.getDataSetByLabel("Z", false);
        if (setEjeZ == null) {
            setEjeZ = setEjeZ();
            datosEjeZ.addDataSet(setEjeZ);
        }
        datosEjeZ.addEntry(new Entry(setEjeZ.getEntryCount(), zAct + 1), 2);
        datosEjeZ.notifyDataChanged();
        graficaAcelerometro.notifyDataSetChanged();
        //Mostrar valor en la etiqueta
        axisZ.setText(Z);
        //Mover vista hacia el último valor
        graficaAcelerometro.moveViewToX(datosEjeZ.getEntryCount());
    }

    private LineDataSet setEjeX() {
        //Generar nuevo set de datos para el eje X
        LineDataSet setEjeX = new LineDataSet(null, "X");
        //Establacer características del set
        setEjeX.setAxisDependency(YAxis.AxisDependency.LEFT);
        setEjeX.setColor(ColorTemplate.MATERIAL_COLORS[0]);
        setEjeX.setCircleColor(ColorTemplate.MATERIAL_COLORS[0]);
        setEjeX.setLineWidth(2f);
        setEjeX.setCircleRadius(2f);
        setEjeX.setFillAlpha(50);
        setEjeX.setFillColor(ColorTemplate.MATERIAL_COLORS[0]);
        setEjeX.setHighLightColor(Color.rgb(46, 204, 113));
        setEjeX.setDrawValues(false);
        //Devolver set de datos
        return setEjeX;
    }

    private LineDataSet setEjeY() {
        //Generar nuevo set de datos para el eje Y
        LineDataSet setEjeY = new LineDataSet(null, "Y");
        //Establacer características del set
        setEjeY.setAxisDependency(YAxis.AxisDependency.LEFT);
        setEjeY.setColor(ColorTemplate.MATERIAL_COLORS[3]);
        setEjeY.setCircleColor(ColorTemplate.MATERIAL_COLORS[3]);
        setEjeY.setLineWidth(2f);
        setEjeY.setCircleRadius(2f);
        setEjeY.setFillAlpha(50);
        setEjeY.setFillColor(ColorTemplate.MATERIAL_COLORS[3]);
        setEjeY.setHighLightColor(Color.rgb(52, 152, 219));
        setEjeY.setDrawValues(false);
        //Devolver set de datos
        return setEjeY;
    }

    private LineDataSet setEjeZ() {
        //Generar nuevo set de datos para el eje Z
        LineDataSet setEjeZ = new LineDataSet(null, "Z");
        //Establacer características del set
        setEjeZ.setAxisDependency(YAxis.AxisDependency.LEFT);
        setEjeZ.setColor(ColorTemplate.MATERIAL_COLORS[2]);
        setEjeZ.setCircleColor(ColorTemplate.MATERIAL_COLORS[2]);
        setEjeZ.setLineWidth(2f);
        setEjeZ.setCircleRadius(2f);
        setEjeZ.setFillAlpha(50);
        setEjeZ.setFillColor(ColorTemplate.MATERIAL_COLORS[2]);
        setEjeZ.setHighLightColor(Color.rgb(231, 76, 60));
        setEjeZ.setDrawValues(false);
        //Devolver set de datos
        return setEjeZ;
    }
}
