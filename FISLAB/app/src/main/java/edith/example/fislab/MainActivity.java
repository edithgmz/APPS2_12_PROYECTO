package edith.example.fislab;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btnNivel);
    }


    public void onClick(View view) {
        Toast.makeText(this, "copaa", Toast.LENGTH_SHORT).show();
        Intent inNivel = new Intent(MainActivity.this,Nivel.class);
        startActivity(inNivel);
    }
}
