package com.cr.chatroomapp.ui.chat;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author wsh
 * @version 1.0
 * @date 2020/4/11 15:25
 */
public class ChatClient {
    private final OnDataChangeLisenter onDataChangeLisenter;
    Socket s = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    private boolean bConnected = false;
    String TAG = "ChatClient";
    Thread tRecv;

    public ChatClient(OnDataChangeLisenter onDataChangeLisenter) {
        this.onDataChangeLisenter = onDataChangeLisenter;
    }

    public void connectServer(int port) {
        connect(port);
        tRecv = new Thread(new RecvThread());
        tRecv.start();
    }

    public void connect(int port) {
        new Thread(() -> {
            try {
                s = new Socket("192.168.0.100", port);
                dos = new DataOutputStream(s.getOutputStream());
                dis = new DataInputStream(s.getInputStream());
                Log.e(TAG, "~~~~~~~~连接成功~~~~~~~~!");
                bConnected = true;
            } catch (UnknownHostException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }).start();


    }

    public void disconnect() {
        try {
            dos.close();
            dis.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sedData(String conetent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dos.writeUTF(conetent);
                    dos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();

    }

    private class RecvThread implements Runnable {

        @Override
        public void run() {
            try {
                while (bConnected) {
                    String str = dis.readUTF();

                    Log.e(TAG, "接收到了数据：" + str);
                    onDataChangeLisenter.resonpase(str);
//                    taContent.setText(taContent.getText() + str + '\n');
                }
            } catch (SocketException e) {
                Log.e(TAG, "退出了，bye!");
            } catch (EOFException e) {
                Log.e(TAG, "退出了，bye!");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
