package com.distanteam.exprezzocurrencydevice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityConexion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion);
    }

    public void errorconexion(View view ){

        Intent i = new Intent( this, MainActivity.class);
        startActivity(i);


    }
}
