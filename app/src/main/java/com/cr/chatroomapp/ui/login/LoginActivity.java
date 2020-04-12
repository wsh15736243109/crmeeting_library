package com.cr.chatroomapp.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cr.chatroomapp.R;
import com.cr.pn.beanBase.BeanBase;
import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;
import com.cr.pn.eventBus.improve.EventBusRealizeRANDS;
import com.cr.pn.netWork.NetWorkStatusData;
import com.cr.pn.websocket.WebSocketManager;
import com.cr.pn.websocket.callback.MyWebSocketListener;
import com.cr.pn.websocket.config.WebSocketConfig;
import com.manager.client.ClientRunnable;

import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends AppCompatActivity implements BaseReceiveEventBus<BeanBase> {

    private LoginViewModel loginViewModel;
    private ClientRunnable nettyClientForChat;
    private NetWorkStatusData netWorkStatusDataForChat = new NetWorkStatusData();
    private EventBusRealizeRANDS<BeanBase> mainEventForChat;
    //tcp连接状态.
    private volatile boolean tcpStatusForChat = false;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        initPro();
        mainEventForChat = new EventBusRealizeRANDS<>(ThreadMode.MAIN, this);
        mainEventForChat.setClass(BeanBase.class);
        final EditText usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });
        loginButton.setOnClickListener(v -> {
            WebSocketConfig webSocketConfig = new WebSocketConfig.Builder()
                    .setAPP_VERSION("1.0.0")
                    .setIP("192.168.0.100")
                    .setPORT(5209)
                    .setMODEL("Android")
                    .setPERSON_MEETING_ID("2434tsd")
                    .setPLATFORM_TYPES(4)
                    .setMyWebSocketListener(new MyWebSocketListener() {
                        @Override
                        public void conenctSuccess(Object data) {

                        }

                        @Override
                        public void connnectFail(String msg) {

                        }

                        @Override
                        public void resonponseData(Object data) {

                        }

                        @Override
                        public void sendFail(String msg) {

                        }
                    })
                    .build();
            WebSocketManager.getInstance().setWebSocketConfig(webSocketConfig);
            WebSocketManager.getInstance().connect();
//                createNettyForChat();
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
        });

//        WebSocketManager.getInstance().sendHeart();
    }

    private void initPro() {
        WebSocketConfig webSocketConfig = new WebSocketConfig.Builder()
                .setAPP_VERSION("1.0.0")
                .setIP("192.168.0.100")
                .setPORT(5209)
                .setMODEL("Android")
                .setPERSON_MEETING_ID("2434tsd")
                .setPLATFORM_TYPES(4)
                .build();
        WebSocketManager.getInstance().setWebSocketConfig(webSocketConfig);
    }

    public void sendData(View view) {
        WebSocketManager.getInstance().sendHeart();
    }


    @Override
    public void onReceiveEvent(BeanBase beanBase) {

    }

    @Override
    public void onReceiveStickyEvent(BeanBase beanBase) {

    }
}
