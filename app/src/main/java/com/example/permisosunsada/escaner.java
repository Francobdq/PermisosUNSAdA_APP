package com.example.permisosunsada;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class escaner extends AppCompatActivity {


    //int idEdificio;
    RequestQueue requestQueue;


    // para el spinner
    Spinner edificiosSpinner;
    String[] edificios;
    int[] idEdificios;

    int idEdificio = -1;

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
        //System.out.println("---------------aaaaaaaaaaaaaaaaaaaaaaaa-----------------------");
        QRScaneado("04ce6aeba9634acea442d4ac67142e66");
        //new IntentIntegrator(this).initiateScan();
    }



    private void irAAnimacion(String qr, int tipoAni, String[] userData){
        Intent myIntent = new Intent(escaner.this, tick.class);
        myIntent.putExtra("qr", qr); //Optional parameters
        myIntent.putExtra("tipoAni", tipoAni); //Optional parameters
        myIntent.putExtra("userData", userData);
        escaner.this.startActivity(myIntent);
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


    private void updateSolicitud(String id_solicitud, String qr, String[] userData) {

        if(!PEDIR_URL.hayConexion(escaner.this))
            return;


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = PEDIR_URL.UpdateSolicitud(id_solicitud,idEdificio);// ConexionJSON.HOST + ConexionJSON.updateSolicitud + id_solicitud  + "/" + idEdificio;


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
                            //Toast.makeText(getApplicationContext(), "Intentanding ", Toast.LENGTH_LONG).show();
                            if(success){
                                // todo bien
                                System.out.println("PUEDE ACCEDER");
                                Toast.makeText(getApplicationContext(), "TODO OK", Toast.LENGTH_SHORT).show();
                                irAAnimacion(qr, tick.CORRECTO, userData);
                            }
                            else {
                                // edificio incorrecto
                                System.out.println("Edificio Incorrecto ");
                                Toast.makeText(getApplicationContext(), "EDIFICIO INCORRECTO ", Toast.LENGTH_SHORT).show();
                                irAAnimacion(qr, tick.EDIFICIO_INCORRECTO, userData);
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
                        error.printStackTrace();
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

    private String invertirFecha(String fecha){
        return fecha.substring(8,10) + fecha.substring(4,8) + fecha.substring(0,4);

    }

    private String toDayDate(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String fecha = sdf.format(date);
            return fecha;
        }

        return "";

    }

    private void QRScaneado(String qr){
        String url = PEDIR_URL.PeticionQR(qr);//ConexionJSON.HOST + ConexionJSON.peticionQR + qr;
        //Toast.makeText(getApplicationContext(), "v3", Toast.LENGTH_SHORT).show();

        if(!PEDIR_URL.hayConexion(escaner.this))
            return;


        idEdificio = comprobarSpinner();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("-------------------------------------------------------------------------------------------------------------------");
                        try {
                            if(response == null){
                                System.out.println("QR INEXISTENTE.");

                                Toast.makeText(getApplicationContext(), "QR inexistente", Toast.LENGTH_SHORT).show();
                                irAAnimacion(qr, tick.QR_INEXISTENTE, null);
                                return; // QR NO EXISTE.
                            }

                            JSONObject data = response.getJSONObject("data");
                            int presenteIngresante = data.getInt("presente");
                            String  id_solicitud = data.getString("idSolicitud");

                            Toast.makeText(getApplicationContext(), "b", Toast.LENGTH_SHORT).show();
                            String[] userData = {
                                    data.getString("nombre"),
                                    data.getString("nombreActividad"),
                                    data.getString("nombreCompleto"),
                                    invertirFecha(data.getString("fechaCarga").substring(0,10))
                            };



                            if(!userData[3].equals(toDayDate())){

                                irAAnimacion(qr, tick.FECHA_INCORRECTA, userData);
                                return; // FECHA INCORRECTA.
                            }

                            //Toast.makeText(getApplicationContext(), "c", Toast.LENGTH_SHORT).show();
                            if(presenteIngresante == 1){
                                //Toast.makeText(getApplicationContext(), "d", Toast.LENGTH_SHORT).show();
                                // usuario ya ingresado interfaz
                                //System.out.println("Usuario ya ingresado");
                                irAAnimacion(qr, tick.QR_REPETIDO, userData);
                                Toast.makeText(getApplicationContext(), "Usuario ya ingresado", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //Toast.makeText(getApplicationContext(), "e", Toast.LENGTH_SHORT).show();
                                updateSolicitud(id_solicitud, qr,userData);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "f " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
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

        if(!PEDIR_URL.hayConexion(escaner.this))
            return;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            edificios = new String[jsonArray.length()];
                            idEdificios = new int[edificios.length];
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject edificiosJsonArray = jsonArray.getJSONObject(i);

                                String nombre = edificiosJsonArray.getString("nombreConSede"); // edificiosJsonArray.getString("nombre_completo");
                                edificios[i] = nombre;
                                idEdificios[i] = edificiosJsonArray.getInt("idEdificio");
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

                        Toast.makeText(getApplicationContext(), "No se puede acceder al servidor. Reinicie la app. Si el problema continua contacte un administrador.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
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
                return idEdificios[i];
            }

        }

        return  -1;
    }
}