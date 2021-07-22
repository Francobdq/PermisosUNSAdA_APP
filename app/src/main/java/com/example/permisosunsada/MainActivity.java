package com.example.permisosunsada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    //public static String url = "http://www.json-generator.com/api/json/get/cphlRqewGG?indent=2";

    ArrayList<HashMap<String, String>> nameList;

    // para el login
    EditText username;
    EditText password;



    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // deshabilita la rotación de pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        PEDIR_URL.Inicializar(MainActivity.this);

        // obtengo los editText para verificar usuario y contraseña
        username = (EditText)findViewById(R.id.usuarioText);
        password = (EditText)findViewById(R.id.passwordText);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        // inicializo el spinner con los valores de los edificios
        nameList = new ArrayList<>();


        ImageView img = (ImageView) findViewById(R.id.configButton);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your code here
                irAConfig();
            }
        });

    }

    public void irAConfig(){
        Toast.makeText(getApplicationContext(), "v5", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, configMenu.class);
        startActivity(intent);
    }


    private boolean comprobarLogin(){
        return true;//username.getText().toString().equals("admin") && password.getText().toString().equals("admin");
    }



    private void irAEscanerActivity(){
        //Toast.makeText(getApplicationContext(), "contraseña correcta " + comprobarSpinner(), Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(MainActivity.this, escaner.class);
        //myIntent.putExtra("idEdificio", idEdificio); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    public void login(View view) {
        if (comprobarLogin())
            irAEscanerActivity();
        else
            Toast.makeText(getApplicationContext(), "contraseña equivocada", Toast.LENGTH_SHORT).show();

    }


}