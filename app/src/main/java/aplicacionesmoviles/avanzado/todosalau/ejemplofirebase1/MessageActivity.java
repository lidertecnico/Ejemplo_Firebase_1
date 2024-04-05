package aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.presenter.MessagePresenter;
import aplicacionesmoviles.avanzado.todosalau.ejemplofirebase1.view.MessageView;

public class MessageActivity extends AppCompatActivity  implements MessageView {
    private EditText messageEditText;
    private Button sendButton;
    private Button returnButton;
    private ListView messageListView;
    private ArrayAdapter<String> adapter;
    private MessagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        returnButton = findViewById(R.id.returnButton);
        messageListView = findViewById(R.id.messageListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        messageListView.setAdapter(adapter);

        presenter = new MessagePresenter((MessageView) this);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ejemplo de redirección a otra actividad
                Intent intent = new Intent(MessageActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Cierra esta actividad para que el usuario no pueda volver atrás
            }
        });

        sendButton.setOnClickListener(view -> {
            String message = messageEditText.getText().toString();
            if (!message.isEmpty()) {
                presenter.sendMessage(message);
            } else {
                Toast.makeText(MessageActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        presenter.loadMessages();
    }

    @Override
    public void clearMessageInput() {
        messageEditText.setText("");
    }

    @Override
    public void loadMessages() {
        presenter.loadMessages();
    }


    @Override
    public void displayMessages(List<String> messages) {
        adapter.clear();
        for (String message : messages) {
            adapter.add(message);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showMessageError() {
        Toast.makeText(MessageActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
    }
}
