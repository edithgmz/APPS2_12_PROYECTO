package edith.example.fislab.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import edith.example.fislab.R;
import edith.example.fislab.data.ResColor;

public class ResColorActivity extends AppCompatActivity implements Button.OnClickListener, ImageView.OnTouchListener {
    private static final int GALERIA = 1200;
    private ImageView imgVwResistor;
    private RadioButton rdBtnColor1, rdBtnColor2, rdBtnColor3;
    private Spinner spTolerancia;
    private TextView txtVwColor1, txtVwColor2, txtVwColor3, txtVwMensaje, txtVwValor;
    //Variables para guardar los valores obtenidos de la imagen
    private int b1 = 0;
    private int b2 = 0;
    private int b3 = 0;
    private boolean tol = false;

    @SuppressLint("ClickableViewAccessibility") @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_color);

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
                txtVwValor.setVisibility(View.VISIBLE);
                txtVwValor.setText(ResColor.calculaRes(b1, b2, b3, tol));
                break;
        }
    }


    @SuppressLint("ClickableViewAccessibility") @RequiresApi(api = Build.VERSION_CODES.O) @Override
    public boolean onTouch(View v, MotionEvent event) {
        imgVwResistor.setDrawingCacheEnabled(true);
        //Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap imgbmp = Bitmap.createBitmap(imgVwResistor.getDrawingCache());
        int pixel = imgbmp.getPixel((int) event.getX(), (int) event.getY());
        int redValue = Color.red(pixel);
        int blueValue = Color.blue(pixel);
        int greenValue = Color.green(pixel);

        if (rdBtnColor1.isChecked()) {
            b1 = ResColor.getDigit(pixel);
            txtVwColor1.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        }

        if (rdBtnColor2.isChecked()) {
            b2 = ResColor.getDigit(pixel);
            txtVwColor2.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        }

        if (rdBtnColor3.isChecked()) {
            b3 = ResColor.getDigit(pixel);
            txtVwColor3.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        }

        if (spTolerancia.getSelectedItem().toString().equals("Dorado")) { tol = true; }
        if (spTolerancia.getSelectedItem().toString().equals("Plateado")) { tol = false; }

        Log.wtf("Color", redValue + ", " + greenValue + ", " + blueValue);
        Toast.makeText(this, "Color: " + redValue + ", " + greenValue + ", " + blueValue, Toast.LENGTH_LONG).show();

        return true;
    }


}
