package aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.model.AccelerometerDataModel;
import aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.presenter.AccelerometerPresenter;
import aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.view.AccelerometerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SensorActivity extends AppCompatActivity  implements AccelerometerView {
    // Variables para el manejo del acelerómetro
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private AccelerometerPresenter presenter;

    // Componentes de la interfaz de usuario
    private TextView accelerometerValuesTextView;
    private Button startButton;
    private Button uploadButton;
    private Button returnButtonSensor;
    private ListView accelerometerListView;
    private ArrayAdapter<String> adapter;
    private List<String> accelerometerDataList;

    // Variables para almacenar los valores finales del acelerómetro
    private float Xfinal;
    private float Yfinal;
    private float Zfinal;

    // Estado del monitoreo del acelerómetro
    private boolean isMonitoring = false;

    // Referencias a Firebase
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Inicialización de los componentes de la interfaz de usuario
        accelerometerValuesTextView = findViewById(R.id.accelerometerValuesTextView);
        startButton = findViewById(R.id.startButton);
        uploadButton = findViewById(R.id.uploadButton);
        returnButtonSensor = findViewById(R.id.returnButtonSensor);
        accelerometerListView = findViewById(R.id.listViewDatabaseRealTime);
        accelerometerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accelerometerDataList);
        accelerometerListView.setAdapter(adapter);

        // Configuración de Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("accelerometer_data");
        firebaseAuth = FirebaseAuth.getInstance();

        // Inicialización del sensor de acelerómetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            presenter = new AccelerometerPresenter(this);
        } else {
            // Manejo de la falta de soporte para el acelerómetro
        }

        // Configuración de los botones
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMonitoring) {
                    startMonitoring();
                    startButton.setText("Stop");
                    showToast("Captura de datos del acelerómetro iniciada");
                } else {
                    stopMonitoring();
                    startButton.setText("Start");
                    showToast("Captura de datos del acelerómetro detenida");
                }
                isMonitoring = !isMonitoring;
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToDatabase();
            }
        });

        returnButtonSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar a la actividad principal
                Intent intent = new Intent(SensorActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Método para iniciar la lectura del acelerómetro
    private void startMonitoring() {
        sensorManager.registerListener(presenter, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Método para detener la lectura del acelerómetro
    private void stopMonitoring() {
        sensorManager.unregisterListener(presenter);
    }

    // Método para mostrar los datos del acelerómetro en la interfaz de usuario
    @Override
    public void displayAccelerometerData(float x, float y, float z) {
        // Mostrar los valores del acelerómetro en el TextView
        Xfinal = x;
        Yfinal = y;
        Zfinal = z;
        String values = "X: " + x + ", Y: " + y + ", Z: " + z;
        accelerometerValuesTextView.setText(values);

        // Agregar los datos a la lista y actualizar el adaptador
        accelerometerDataList.add(values);
        adapter.notifyDataSetChanged();
    }

    // Método para subir los datos del acelerómetro a la base de datos Firebase
    private void uploadDataToDatabase() {
        // Tomar el valor de aceleración y subirlo a la base de datos
        float x = Xfinal; /* obtener valor de x */;
        float y = Yfinal; /* obtener valor de y */;
        float z = Zfinal; /* obtener valor de z */;

        // Guardar los valores en Firebase Realtime Database bajo el ID del usuario logeado
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            AccelerometerDataModel dataModel = new AccelerometerDataModel(x, y, z, new Date());
            // Generar un ID único para cada entrada de datos
            String dataId = databaseReference.child(userId).push().getKey();
            // Guardar los datos en la ubicación especificada en la base de datos bajo el ID del usuario
            databaseReference.child(userId).child(dataId).setValue(dataModel);
            showToast("Datos cargados correctamente en Firebase");
        }
    }

    // Método para mostrar mensajes de notificación
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}