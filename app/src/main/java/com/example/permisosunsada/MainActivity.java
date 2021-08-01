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


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

    private void irAEscanerActivity(){
        //Toast.makeText(getApplicationContext(), "contraseña correcta " + comprobarSpinner(), Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(MainActivity.this, escaner.class);
        //myIntent.putExtra("idEdificio", idEdificio); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    public void login(View view) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String postUrl = PEDIR_URL.PeticionLogin();

        if(user.equals("") || pass.equals("")){
            Toast.makeText(getApplicationContext(), "Faltan datos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!PEDIR_URL.hayConexion(MainActivity.this))
            return;


        HashMap<String, Object> postData = new HashMap<>();
        postData.put("username", user);
        postData.put("password", pass);

        StringRequest request = new StringRequest(Request.Method.POST, postUrl,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
                        irAEscanerActivity();
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña equivocado " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            public byte[] getBody() {
                return new JSONObject(postData).toString().getBytes();
            }
            public String getBodyContentType() {
                return "application/json";
            }
        };

        Volley.newRequestQueue(this).add(request);

        
    }


}