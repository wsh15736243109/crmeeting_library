package com.cr.chatroomapp.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cr.chatroomapp.R;

public class ChatActivity extends AppCompatActivity implements OnDataChangeLisenter {


    Button btn_send, btn_connect;
    EditText et_send, et_receive;
    private ChatClient chatClient = new ChatClient(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        btn_send = findViewById(R.id.btn_send);
        btn_connect = findViewById(R.id.btn_connect);
        et_send = findViewById(R.id.et_send);
        et_receive = findViewById(R.id.et_receive);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接
//                chatClient.connectServer(8888);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //发送数据
                chatClient.sedData(et_send.getText().toString());
            }
        });
    }

    @Override
    public void resonpase(final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et_receive.setText(data + "");
            }
        });

    }
}
