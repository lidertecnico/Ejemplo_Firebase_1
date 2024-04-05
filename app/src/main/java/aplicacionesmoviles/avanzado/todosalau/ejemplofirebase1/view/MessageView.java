package aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.view;

import java.util.List;

public interface MessageView {
    void clearMessageInput();
    void loadMessages();
    public void displayMessages(List<String> messages);
    void showMessageError();
}
