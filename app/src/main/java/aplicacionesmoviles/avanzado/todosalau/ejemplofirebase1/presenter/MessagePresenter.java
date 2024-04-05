package aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.presenter;

import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.model.MessageModel;
import aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.view.MessageView;

// Clase que actúa como intermediario entre la vista (UI) y el modelo de mensajes
public class MessagePresenter {
    private MessageModel model; // Referencia al modelo de mensajes
    private MessageView view; // Referencia a la vista de mensajes

    // Constructor que recibe la vista como parámetro
    public MessagePresenter(MessageView view) {
        this.view = view; // Asigna la vista proporcionada
        model = new MessageModel(); // Inicializa el modelo de mensajes
    }

    // Método para enviar un mensaje
    public void sendMessage(String message) {
        // Llama al método del modelo para enviar un mensaje, proporcionando un listener para manejar los eventos
        model.sendMessage(message, new MessageModel.OnSendMessageListener() {
            @Override
            public void onSuccess() {
                // En caso de éxito al enviar el mensaje, se limpia el campo de entrada de mensajes y se cargan los mensajes nuevamente
                view.clearMessageInput();
                view.loadMessages();
            }

            @Override
            public void onError() {
                // En caso de error al enviar el mensaje, se muestra un mensaje de error en la vista
                view.showMessageError();
            }
        });
    }

    // Método para cargar los mensajes
    public void loadMessages() {
        // Llama al método del modelo para cargar los mensajes, proporcionando un listener para manejar los eventos
        model.loadMessages(new MessageModel.OnLoadMessagesListener() {
            @Override
            public void onSuccess(List<String> messages) {
                // En caso de éxito al cargar los mensajes, se muestran en la vista
                view.displayMessages(messages);
            }

            @Override
            public void onError() {
                // En caso de error al cargar los mensajes, se muestra un mensaje de error en la vista
                view.showMessageError();
            }
        });
    }
}