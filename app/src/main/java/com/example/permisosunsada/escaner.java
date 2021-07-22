package com.example.permisosunsada;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class escaner extends AppCompatActivity {


    //int idEdificio;
    RequestQueue requestQueue;


    // para el spinner
    Spinner edificiosSpinner;
    String[] edificios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // deshabilita la rotación de pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        //idEdificio = intent.getIntExtra("idEdificio",-1); //if it's a string you stored.
        requestQueue = Volley.newRequestQueue(escaner.this);

        //Toast.makeText(getApplicationContext(), "idEdificio: " + idEdificio, Toast.LENGTH_SHORT).show();
        inicializarSpinner();
    }


    public void escanear(View view){
        new IntentIntegrator(this).initiateScan();
    }



    private void irAAnimacion(int tipoAni, String warning_msg){
        Intent myIntent = new Intent(escaner.this, tick.class);
        myIntent.putExtra("tipoAni", tipoAni); //Optional parameters
        myIntent.putExtra("warning_msg", warning_msg); //Optional parameters
        escaner.this.startActivity(myIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String valorDelQR = result.getContents();

        if (valorDelQR != null) {
            //Toast.makeText(getApplicationContext(), "QR: "+ valorDelQR, Toast.LENGTH_SHORT).show();
            QRScaneado(valorDelQR);
        }
        else{
            Toast.makeText(getApplicationContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
        }
    }

    /*private void PutRequest(String id_solicitud){
        String url =  ConexionJSON.HOST + ConexionJSON.updateSolicitud + id_solicitud  + "/" + idEdificio;

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //user = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    try {

                        boolean success = jsonObject.getBoolean("success");
                        Toast.makeText(getApplicationContext(), "Intentanding ", Toast.LENGTH_LONG).show();
                        if(success){
                            // todo bien
                            System.out.println("PUEDE ACCEDER");
                            Toast.makeText(getApplicationContext(), "TODO OK", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // edificio incorrecto
                            System.out.println("Edificio Incorrecto ");
                            Toast.makeText(getApplicationContext(), "EDIFICIO INCORRECTO " + jsonObject.getString("data") + " " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley error", error.toString());


            }
        }) {
            @Override
            public String getBodyContentType() { //override
//  the content type because the request is string not JSON
//   request note that if you used JSON request it will don't work with rest

                return "application/json";
            }
            //override getParams so that the server can receive the parameters otherwise
//  parameters will be null at server
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("presente", "1");
                return parameters;
            }
        };

        requestQueue.add(request);
    }*/


    private void updateSolicitud(String id_solicitud) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = PEDIR_URL.UpdateSolicitud(id_solicitud,comprobarSpinner());// ConexionJSON.HOST + ConexionJSON.updateSolicitud + id_solicitud  + "/" + idEdificio;

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("presente", 1);
        } catch (JSONException e) {
            // handle exception
        }

        //Toast.makeText(getApplicationContext(), "Intentanding A", Toast.LENGTH_SHORT).show();
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        System.out.println("-----------------------------------------------------------UPDATE SOLICITUD--------------------------------------------------------");

                        try {

                            boolean success = response.getBoolean("success");
                            Toast.makeText(getApplicationContext(), "Intentanding ", Toast.LENGTH_LONG).show();
                            if(success){
                                // todo bien
                                System.out.println("PUEDE ACCEDER");
                                //Toast.makeText(getApplicationContext(), "TODO OK", Toast.LENGTH_SHORT).show();
                                irAAnimacion(tick.TICK, "");
                            }
                            else {
                                // edificio incorrecto
                                System.out.println("Edificio Incorrecto ");
                                //Toast.makeText(getApplicationContext(), "EDIFICIO INCORRECTO " + response.getString("data") + " " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                irAAnimacion(tick.DANGER, "Edificio incorrecto.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "Error response en update solicitud", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {

                try {
                    Log.i("json", jsonObject.toString());
                    return jsonObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }


        };

        queue.add(putRequest);
    }

    private void QRScaneado(String qr){
        String url = PEDIR_URL.PeticionQR(qr);//ConexionJSON.HOST + ConexionJSON.peticionQR + qr;
        //Toast.makeText(getApplicationContext(), "v3", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("-------------------------------------------------------------------------------------------------------------------");
                        try {
                            JSONObject data = response.getJSONObject("data");
                            if(data == null){
                                System.out.println("QR INEXISTENTE.");

                                irAAnimacion(tick.DANGER, "EL QR NO ES CORRECTO.");
                                //Toast.makeText(getApplicationContext(), "QR inexistente", Toast.LENGTH_SHORT).show();
                                return; // QR NO EXISTE.
                            }
                            int presenteIngresante = data.getInt("presente");
                            String  id_solicitud = data.getString("id_solicitud");

                            if(presenteIngresante == 1){
                                // usuario ya ingresado interfaz
                                //System.out.println("Usuario ya ingresado");
                                irAAnimacion(tick.WARNING, "USUARIO YA INGRESADO.");
                                //Toast.makeText(getApplicationContext(), "Usuario ya ingresado", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                updateSolicitud(id_solicitud);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getApplicationContext(), "Error response en qrscaneado", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
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

    private void edificioASpinner(){
        String url = PEDIR_URL.PeticionEdificios();//ConexionJSON.HOST + ConexionJSON.peticionEdificios;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            edificios = new String[jsonArray.length()];
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject edificiosJsonArray = jsonArray.getJSONObject(i);

                                String nombre = edificiosJsonArray.getString("nombre");
                                edificios[i] = nombre;
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(escaner.this, android.R.layout.simple_spinner_dropdown_item, edificios);
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
}