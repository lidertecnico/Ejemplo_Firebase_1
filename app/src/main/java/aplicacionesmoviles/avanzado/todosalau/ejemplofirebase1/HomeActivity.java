package aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button btnFirestore;
    private Button btnRealDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnFirestore = findViewById(R.id.buttonFirestore);
        btnRealDatabase = findViewById(R.id.buttonRealtimeDatabase);
        btnFirestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ejemplo de redirección a otra actividad
                Intent intent = new Intent(HomeActivity.this, MessageActivity.class);
                startActivity(intent);
                finish(); // Cierra esta actividad para que el usuario no pueda volver atrás
            }
        });
        btnRealDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ejemplo de redirección a otra actividad
                Intent intent = new Intent(HomeActivity.this, SensorActivity.class);
                startActivity(intent);
                finish(); // Cierra esta actividad para que el usuario no pueda volver atrás
            }
        });

    }
}