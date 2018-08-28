package com.example.franck.mapchain.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.franck.mapchain.R;
import com.example.franck.mapchain.adapters.MyAdapter;
import com.example.franck.mapchain.etherium.EtheriumRunner;

import java.io.File;
import java.io.FileOutputStream;

public class AddQuest extends AppCompatActivity {
    private Button deploy;
    private TextInputEditText textInputEditText;
    private EtheriumRunner etheriumRunner;
    private  FileOutputStream outputStream;private  FileOutputStream outputStream1;
    private String filename;
    private String filename1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        etheriumRunner = new EtheriumRunner();

        filename = "UTC--2018-04-21T17-43-12_948279000Z--59ea0893ca2abe7bae02a5c2a8d564c5a2146ae2";
        String string = EtheriumRunner.jsonUTC;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        textInputEditText = findViewById(R.id.password_deploy);
        deploy = findViewById(R.id.deploy_button);
        deploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                textInputEditText.getText().toString();

                try{etheriumRunner.init(textInputEditText.getText().toString(), new File(getApplicationContext().getFilesDir(), filename),"creator");}
                catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Deploy", Toast.LENGTH_LONG);
            }
        });
    }
}
