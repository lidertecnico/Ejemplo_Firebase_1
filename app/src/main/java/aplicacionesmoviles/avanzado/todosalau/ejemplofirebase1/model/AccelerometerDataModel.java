package aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.model;

import java.util.Date;

public class AccelerometerDataModel {
    // Define los campos necesarios para almacenar los datos del acelerómetro
    private float x; // Valor de la aceleración en el eje X
    private float y; // Valor de la aceleración en el eje Y
    private float z; // Valor de la aceleración en el eje Z
    private Date timestamp; // Marca de tiempo de la lectura del acelerómetro

    // Constructor
    // Constructor sin argumentos necesario para Firebase
    public AccelerometerDataModel() {
    }

    // Constructor con argumentos para inicializar los valores del modelo
    public AccelerometerDataModel(float x, float y, float z, Date timestamp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.timestamp = timestamp;
    }

    // Métodos para obtener los valores de los campos privados

    public float getX() {
        return x; // Devuelve el valor de la aceleración en el eje X
    }

    public float getY() {
        return y; // Devuelve el valor de la aceleración en el eje Y
    }

    public float getZ() {
        return z; // Devuelve el valor de la aceleración en el eje Z
    }

    public Date getTimestamp() {
        return timestamp; // Devuelve la marca de tiempo de la lectura del acelerómetro
    }
}