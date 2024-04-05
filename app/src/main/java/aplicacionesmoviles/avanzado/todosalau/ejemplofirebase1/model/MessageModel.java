package aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.model;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageModel {
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // Constructor de la clase
    public MessageModel() {
        db = FirebaseFirestore.getInstance(); // Inicialización de Firebase Firestore
        auth = FirebaseAuth.getInstance(); // Inicialización de Firebase Auth
    }

    // Método para enviar un mensaje
    public void sendMessage(String message, OnSendMessageListener listener) {
        FirebaseUser currentUser = auth.getCurrentUser(); // Obtener el usuario actual
        if (currentUser != null) { // Verificar si hay un usuario logueado
            String userUID = currentUser.getUid(); // Obtener el UID del usuario actual

            // Obtener la fecha y hora actual
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = sdf.format(new Date());

            // Crear un nuevo mensaje con el contenido y la fecha
            Map<String, Object> newMessage = new HashMap<>();
            newMessage.put("message", message);
            newMessage.put("timestamp", dateTime); // Agregar la fecha y hora al mensaje

            // Agregar el mensaje a la colección del usuario en Firestore
            db.collection("users")
                    .document(userUID)
                    .collection("messages")
                    .add(newMessage)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Message added with ID: " + documentReference.getId()); // Éxito al agregar el mensaje
                        listener.onSuccess(); // Notificar al listener que el mensaje se envió correctamente
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding message", e); // Error al agregar el mensaje
                        listener.onError(); // Notificar al listener sobre el error
                    });
        } else {
            Log.w(TAG, "No user is currently logged in."); // No hay ningún usuario logueado
            listener.onError(); // Notificar al listener sobre el error
        }
    }

    // Método para cargar los mensajes del usuario
    public void loadMessages(OnLoadMessagesListener listener) {
        FirebaseUser currentUser = auth.getCurrentUser(); // Obtener el usuario actual
        if (currentUser != null) { // Verificar si hay un usuario logueado
            String userUID = currentUser.getUid(); // Obtener el UID del usuario actual

            // Obtener los mensajes del usuario desde Firestore
            db.collection("users")
                    .document(userUID)
                    .collection("messages")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) { // Verificar si la tarea se completó exitosamente
                            List<String> messages = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String message = document.getString("message");
                                String time = document.getString("timestamp");
                                String mensaje = "\nMensaje: "+ message + "\nFecha:"+time;
                                messages.add(mensaje); // Agregar cada mensaje a la lista
                            }
                            listener.onSuccess(messages); // Notificar al listener que se cargaron los mensajes exitosamente
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException()); // Error al obtener los documentos
                            listener.onError(); // Notificar al listener sobre el error
                        }
                    });
        } else {
            Log.w(TAG, "No user is currently logged in."); // No hay ningún usuario logueado
            listener.onError(); // Notificar al listener sobre el error
        }
    }

    // Interfaz para manejar el resultado del envío de mensajes
    public interface OnSendMessageListener {
        void onSuccess(); // Método llamado cuando el mensaje se envía correctamente
        void onError(); // Método llamado cuando hay un error al enviar el mensaje
    }

    // Interfaz para manejar el resultado de la carga de mensajes
    public interface OnLoadMessagesListener {
        void onSuccess(List<String> messages); // Método llamado cuando se cargan los mensajes exitosamente
        void onError(); // Método llamado cuando hay un error al cargar los mensajes
    }
}