package edith.example.fislab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    Button btnNivel;
    Intent inNivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Vincular componentes
        btnNivel = findViewById(R.id.btnNivel);
        //Intentos a otras actividades
        inNivel = new Intent(this, NivelActivity.class);
        //Escuchadores
        btnNivel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNivel:
                startActivity(inNivel);
                break;
        }
    }
}
