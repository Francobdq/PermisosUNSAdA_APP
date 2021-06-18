package com.example.permisosunsada;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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


    int idEdificio;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner);
        Intent intent = getIntent();
        idEdificio = intent.getIntExtra("idEdificio",-1); //if it's a string you stored.
        requestQueue = Volley.newRequestQueue(escaner.this);
        //Toast.makeText(getApplicationContext(), "idEdificio: " + idEdificio, Toast.LENGTH_SHORT).show();
    }


    public void escanear(View view){
        new IntentIntegrator(this).initiateScan();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        String valorDelQR = result.getContents();

        if (valorDelQR != null) {
            Toast.makeText(getApplicationContext(), "QR: "+ valorDelQR, Toast.LENGTH_SHORT).show();
            QRScaneado(valorDelQR);
        }
        else{
            Toast.makeText(getApplicationContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSolicitud(String id_solicitud) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConexionJSON.HOST + ConexionJSON.updateSolicitud + id_solicitud  + "/" + idEdificio;

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
                                Toast.makeText(getApplicationContext(), "TODO OK", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // edificio incorrecto
                                System.out.println("Edificio Incorrecto ");
                                Toast.makeText(getApplicationContext(), "EDIFICIO INCORRECTO " + response.getString("data") + " " + response.getString("message"), Toast.LENGTH_SHORT).show();
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

    /*private void updateSolicitud(String id_solicitud){
        String url = ConexionJSON.HOST + ConexionJSON.updateSolicitud + id_solicitud  + "/" + idEdificio;

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("presente", 1);
        } catch (JSONException e) {
            // handle exception
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        requestQueue.add(jsonObjectRequest);
    }*/

    private void QRScaneado(String qr){
        String url = ConexionJSON.HOST + ConexionJSON.peticionQR + qr;
        Toast.makeText(getApplicationContext(), "v2", Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("-------------------------------------------------------------------------------------------------------------------");
                        try {
                            JSONObject data = response.getJSONObject("data");
                            if(data == null){
                                System.out.println("QR INEXISTENTE.");
                                Toast.makeText(getApplicationContext(), "QR inexistente", Toast.LENGTH_SHORT).show();
                                return; // QR NO EXISTE.
                            }
                            int presenteIngresante = data.getInt("presente");
                            String  id_solicitud = data.getString("id_solicitud");

                            if(presenteIngresante == 1){
                                // usuario ya ingresado interfaz
                                System.out.println("Usuario ya ingresado");
                                Toast.makeText(getApplicationContext(), "Usuario ya ingresado", Toast.LENGTH_SHORT).show();
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
}