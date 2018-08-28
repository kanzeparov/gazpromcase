package com.example.franck.sampleinterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv_sign_up;
    private Button button_sign_in;
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_sign_up = findViewById(R.id.tv_sign_up);
        button_sign_in = findViewById(R.id.btn_sign_in);
        switchCompat = findViewById(R.id.tsp_switch);

        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });

        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCompat.isChecked())
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                else
                    startActivity(new Intent(getApplicationContext(), ProfileTsp.class));
            }
        });
    }
}
