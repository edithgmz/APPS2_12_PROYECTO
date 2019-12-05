package edith.example.fislab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    Button btnNivel,btnRes;
    Intent inNivel,inRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Vincular componentes
        btnNivel = findViewById(R.id.btnNivel);
        btnRes = findViewById(R.id.btnResis);
        //Intentos a otras actividades
        inNivel = new Intent(this, NivelActivity.class);
        //Escuchadores
        btnNivel.setOnClickListener(this);
        btnRes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNivel:
                startActivity(inNivel);
                break;
            case R.id.btnResis:
                startActivity(inRes);
                Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
