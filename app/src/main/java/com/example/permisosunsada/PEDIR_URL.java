package com.example.permisosunsada;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

import static android.content.Context.MODE_PRIVATE;

public class PEDIR_URL {

    private static final String SAVE_FILE_NAME = "config.txt";

    private static String HOST = "http://areco.gob.ar:9528";
    private static String URL = "areco.gob.ar:9528";
    private static Boolean CIFRADO  = false;

    private static String peticionEdificios = "/api/edificio/all";
    private static String peticionQR = "/api/solicitud/find/uuid/";
    private static String updateSolicitud = "/api/solicitud/update/"; // agregar otra al inciio para probar
    private static String peticionLogin = "/api/login/ingreso";

    private static void saveFile(String textoASalvar, Context ctx){
        FileOutputStream fileOutputStream = null;
        Toast.makeText(ctx, "Guardando...", Toast.LENGTH_SHORT).show();
        try {
            fileOutputStream =  ctx.openFileOutput(SAVE_FILE_NAME, MODE_PRIVATE);
            fileOutputStream.write(textoASalvar.getBytes());
            Toast.makeText(ctx, "Fichero Salvado en: " + ctx.getFilesDir() + "/" + SAVE_FILE_NAME, Toast.LENGTH_SHORT).show();
            //Log.d("TAG1", "Fichero Salvado en: " + ctx.getFilesDir() + "/" + SAVE_FILE_NAME);
        }catch (Exception e){
            Toast.makeText(ctx, "Nop: " + e.getMessage() , Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }finally {
            if(fileOutputStream != null){
                try{
                    fileOutputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    private static void readFile(Context ctx){
        FileInputStream fileInputStream = null;
        try{
            Toast.makeText(ctx, "Buscando en " + ctx.getFilesDir() + "/" + SAVE_FILE_NAME, Toast.LENGTH_SHORT).show();
            fileInputStream = ctx.openFileInput(SAVE_FILE_NAME); // puede dar una excepcion si el archivo no existe
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineaTexto = bufferedReader.readLine();

            Toast.makeText(ctx, "texto:" + lineaTexto, Toast.LENGTH_SHORT).show();
            // si el archivo está vacio es como si no existiera
            if(lineaTexto == null)
                throw new FileNotFoundException();

            HOST = lineaTexto;

        } catch (FileNotFoundException e) {
            saveFile(HOST, ctx); // si el archivo no existe guardo los datos de las variables que son los generales.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream !=null){
                try {
                    fileInputStream.close();
                }catch (Exception e){

                }
            }
        }
    }




    public static String UpdateSolicitud(String id_solicitud, int idEdificio){
        return HOST + updateSolicitud + id_solicitud + "/" + idEdificio;
    }

    public static String PeticionQR(String qr){
        return HOST+peticionQR+qr;
    }

    public static String PeticionEdificios(){
        return HOST+peticionEdificios;
    }

    public static String PeticionLogin(){
        return HOST+peticionLogin;
    }

    /*public static void setCIFRADO(String nuevo_cifrado){
        CIFRADO = nuevo_cifrado;
    }*/

    public static void setHOST(String cifrado, String nuevo_host){
        HOST = cifrado + nuevo_host;
        URL = nuevo_host;
    }

    public static void Inicializar(Context ctx){
        readFile(ctx);
        CIFRADO = (HOST.charAt(4) == 's'); // pregunta si es https o http --- http:// https://
        URL = HOST.substring((CIFRADO) ? 8 : 7);

    }


    // la pide desdse el config menu
    public static String getURL(){
        return URL;
    }

    // la pide desdse el config menu
    public static boolean Cifrado(){
        return CIFRADO;
    }

    public static void ActualizarGuardarCambios(String cifrado, String host, Context ctx){
        //setCIFRADO(cifrado);
        setHOST(cifrado,host);
        saveFile(cifrado+host,ctx);


    }







    public static boolean hayConexion(Context ctx){
        if(hayConexionServer(ctx))
            return true;

        if(hayConexionInternet(ctx)){
            Toast.makeText(ctx, "No se puede acceder al servidor. Espere un momento, si el problema continua Consulte a un técnico", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(ctx, "No hay conexión a internet. Intente de nuevo más tarde", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private static boolean hayConexionInternet(Context ctx){
        ConnectivityManager cm =
                (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    // Test the conexion to the server
    private static boolean hayConexionServer(Context context) {
        return true;
    }


    //public static String getSaveFileName(){
    //    return SAVE_FILE_NAME;
    //}
}
