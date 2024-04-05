package aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.presenter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.view.AccelerometerView;

public class AccelerometerPresenter implements SensorEventListener {
    private AccelerometerView view; // Referencia a la vista que muestra los datos del acelerómetro

    // Constructor que recibe la vista como parámetro e inicializa el presenter
    public AccelerometerPresenter(AccelerometerView view) {
        this.view = view;
    }

    // Método llamado cuando cambian los valores de un sensor, en este caso el acelerómetro
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Lógica para procesar los datos del acelerómetro
        // y luego actualizar la vista

        // Verifica si el evento proviene del sensor de aceleración
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Obtiene los valores de aceleración en los ejes X, Y y Z
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Llama al método de la vista para mostrar los datos del acelerómetro
            view.displayAccelerometerData(x, y, z);
        }
    }

    // Método llamado cuando cambia la precisión de un sensor, no se utiliza en este caso
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se utiliza
    }
}