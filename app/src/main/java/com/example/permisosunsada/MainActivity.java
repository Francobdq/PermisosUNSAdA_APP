package com.example.permisosunsada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    //public static String url = "http://www.json-generator.com/api/json/get/cphlRqewGG?indent=2";

    ArrayList<HashMap<String, String>> nameList;

    // para el login
    EditText username;
    EditText password;

    // para el spinner
    Spinner edificiosSpinner;
    String[] edificios;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtengo los editText para verificar usuario y contraseña
        username = (EditText)findViewById(R.id.usuarioText);
        password = (EditText)findViewById(R.id.passwordText);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        // inicializo el spinner con los valores de los edificios
        nameList = new ArrayList<>();
        inicializarSpinner();
    }

    private void inicializarSpinner(){
        /*getNames a = new getNames();
        a.onPreExecute();
        a.doInBackground();*/

        edificiosSpinner = (Spinner)findViewById(R.id.spinner2);


        edificioASpinner();

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, edificios);
        //edificiosSpinner.setAdapter(adapter);
    }

    private boolean comprobarLogin(){
        return username.getText().toString().equals("admin") && password.getText().toString().equals("admin");
    }

    private int comprobarSpinner(){
        String seleccionado = edificiosSpinner.getSelectedItem().toString();

        if(edificios == null){
            Toast.makeText(getApplicationContext(), "No se pudo establecer internet. Intente de nuevo más tarde.", Toast.LENGTH_SHORT).show();
            return -1;
        }

        for(int i = 0; i < edificios.length;i++){
            if(edificios[i].equals(seleccionado)){
                return i;
            }

        }

        return  -1;
    }

    private void irAEscanerActivity(int idEdificio){
        Toast.makeText(getApplicationContext(), "contraseña correcta " + comprobarSpinner(), Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(MainActivity.this, escaner.class);
        myIntent.putExtra("idEdificio", idEdificio); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    public void login(View view) {
        if (comprobarLogin()) {
            int idEdificio = comprobarSpinner();
            if(comprobarSpinner() != -1){
                irAEscanerActivity(idEdificio+1);
            }

        } else {
            Toast.makeText(getApplicationContext(), "contraseña equivocada", Toast.LENGTH_SHORT).show();
        }

    }


    private void edificioASpinner(){
        String url = ConexionJSON.HOST + ConexionJSON.peticionEdificios;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("-------------------------------------------------------------------------------------------------------------------");
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            edificios = new String[jsonArray.length()];
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject edificiosJsonArray = jsonArray.getJSONObject(i);

                                String nombre = edificiosJsonArray.getString("nombre");
                                edificios[i] = nombre;
                                System.out.println("nombre:" + nombre);

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, edificios);
                            edificiosSpinner.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}