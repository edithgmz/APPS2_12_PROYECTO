package edith.example.fislab.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import edith.example.fislab.R;
import edith.example.fislab.interfaces.ResColor;

public class ResColorActivity extends AppCompatActivity implements Button.OnClickListener, ImageView.OnTouchListener {
    private static final int GALERIA = 1200;
    private ImageView imgVwResistor;
    private RadioButton rdBtnColor1, rdBtnColor2, rdBtnColor3;
    private Spinner spTolerancia;
    private TextView txtVwColor1, txtVwColor2, txtVwColor3, txtVwMensaje, txtVwValor;
    //Variables para guardar los valores obtenidos de la imagen
    private int b1;
    private int b2;
    private int b3;

    @SuppressLint("ClickableViewAccessibility") @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_color);
        //Vincular componentes
        Button btnAbrirImg = findViewById(R.id.btnAbrirImg);
        Button btnCalcular = findViewById(R.id.btnCalcular);
        imgVwResistor = findViewById(R.id.imgVwResistor);
        rdBtnColor1 = findViewById(R.id.rdBtnColor1);
        rdBtnColor2 = findViewById(R.id.rdBtnColor2);
        rdBtnColor3 = findViewById(R.id.rdBtnColor3);
        spTolerancia = findViewById(R.id.spTolerancia);
        txtVwColor1 = findViewById(R.id.txtVwColor1);
        txtVwColor2 = findViewById(R.id.txtVwColor2);
        txtVwColor3 = findViewById(R.id.txtVwColor3);
        txtVwMensaje = findViewById(R.id.txtVwMensaje);
        txtVwValor = findViewById(R.id.txtVwValor);
        //Escuchadores
        btnAbrirImg.setOnClickListener(this);
        btnCalcular.setOnClickListener(this);
        imgVwResistor.setOnTouchListener(this);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERIA) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //Obtener ruta de la imagen
                Uri uri = data.getData();
                try {
                    //Resolver la ruta de la imagen para obtenerla
                    InputStream inputStream = null;
                    if (uri != null) {
                        inputStream = getContentResolver().openInputStream(uri);
                    }
                    //Crear bitmap a partir de la imagen obtenida
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    //Mostrar imagen seleccionada
                    imgVwResistor.setVisibility(View.VISIBLE);
                    imgVwResistor.setImageBitmap(bitmap);
                    //Mostrar mensaje
                    txtVwMensaje.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAbrirImg:
                //Acceder a la galería
                Intent inGaleria = new Intent(Intent.ACTION_PICK);
                inGaleria.setType("image/*");
                startActivityForResult(inGaleria, GALERIA);
                break;
            case R.id.btnCalcular:
                //Calcular valor del resistor y mostrarlo
                String tol = spTolerancia.getSelectedItem().toString();
                txtVwValor.setVisibility(View.VISIBLE);
                txtVwValor.setText(ResColor.calculaRes(b1, b2, b3, tol));
                break;
        }
    }


    @SuppressLint("ClickableViewAccessibility") @RequiresApi(api = Build.VERSION_CODES.O) @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Crear bitmap con el cache de la imagen seleccionada
        imgVwResistor.setDrawingCacheEnabled(true);
        Bitmap imgbmp = Bitmap.createBitmap(imgVwResistor.getDrawingCache());
        //Obtener pixel de las coordenadas que han sido presionadas
        int pixel = imgbmp.getPixel((int) event.getX(), (int) event.getY());
        //Obtener valores rojo, azul y verde del pixel
        int redValue = Color.red(pixel);
        int blueValue = Color.blue(pixel);
        int greenValue = Color.green(pixel);
        //Establecer valor de la banda 1 y mostrar color
        if (rdBtnColor1.isChecked()) {
            b1 = ResColor.getDigit(pixel);
            txtVwColor1.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        }
        //Establecer valor de la banda 2 y mostrar color
        if (rdBtnColor2.isChecked()) {
            b2 = ResColor.getDigit(pixel);
            txtVwColor2.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        }
        //Establecer valor de la banda 3 y mostrar color
        if (rdBtnColor3.isChecked()) {
            b3 = ResColor.getDigit(pixel);
            txtVwColor3.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        }
        //Devolver estado del evento
        return true;
    }
}
