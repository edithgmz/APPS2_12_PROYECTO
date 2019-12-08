package edith.example.fislab.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import edith.example.fislab.R;
import edith.example.fislab.fragments.VerifSensoresFragment;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {
    private Intent inAcel, inNivel, inProx, inResValor;
    private FrameLayout flSensores;
    private ImageView imgVwPortada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Vincular componentes
        Button btnAcelerometro = findViewById(R.id.btnAcelerometro);
        Button btnNivel = findViewById(R.id.btnNivel);
        Button btnProximidad = findViewById(R.id.btnProximidad);
        Button btnResValor = findViewById(R.id.btnResValor);
        Button btnSensores = findViewById(R.id.btnSensores);
        flSensores = findViewById(R.id.flSensores);
        imgVwPortada = findViewById(R.id.imgVwPortada);
        //Intentos a otras actividades
        inAcel = new Intent(this, AcelLinealActivity.class);
        inNivel = new Intent(this, NivelActivity.class);
        inProx = new Intent(this, ProximidadActivity.class);
        inResValor = new Intent(this, ResColorActivity.class);
        //Escuchadores
        btnAcelerometro.setOnClickListener(this);
        btnNivel.setOnClickListener(this);
        btnProximidad.setOnClickListener(this);
        btnResValor.setOnClickListener(this);
        btnSensores.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAcelerometro:
                startActivity(inAcel);
                break;
            case R.id.btnNivel:
                startActivity(inNivel);
                break;
            case R.id.btnProximidad:
                startActivity(inProx);
                break;
            case R.id.btnResValor:
                startActivity(inResValor);
                break;
            case R.id.btnSensores:
                flSensores.setVisibility(View.VISIBLE);
                imgVwPortada.setVisibility(View.GONE);
                cargarFragment(new VerifSensoresFragment());
                break;
        }
    }

    private void cargarFragment(Fragment fragment) {
        //Muestra el fragmento
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flSensores, fragment).commit();
        }
    }
}
