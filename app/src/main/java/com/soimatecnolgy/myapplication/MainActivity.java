package com.soimatecnolgy.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.soimatecnolgy.myapplication.Post.PostData;

public class MainActivity extends AppCompatActivity {
    Button getData;
    Button sendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendData = (Button) findViewById(R.id.insertUser);
        getData = (Button) findViewById(R.id.viewUser);

        getData.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), UserList.class);
                startActivity(intent);
            }
        });

        sendData.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), PostData.class);
                startActivity(intent);
            }
        });

    }
}
