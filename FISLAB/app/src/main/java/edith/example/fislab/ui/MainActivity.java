package edith.example.fislab.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edith.example.fislab.R;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    private Intent inNivel, inResValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Vincular componentes
        Button btnNivel = findViewById(R.id.btnNivel);
        Button btnResValor = findViewById(R.id.btnResValor);
        //Intentos a otras actividades
        inNivel = new Intent(this, NivelActivity.class);
        inResValor = new Intent(this, ResColorActivity.class);
        //Escuchadores
        btnNivel.setOnClickListener(this);
        btnResValor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNivel:
                startActivity(inNivel);
                break;
            case R.id.btnResValor:
                startActivity(inResValor);
                break;
        }
    }
}
