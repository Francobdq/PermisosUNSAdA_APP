package com.example.permisosunsada;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConexionJSON {

    public static String HOST = "http://areco.gob.ar:9528/";

    public static String peticionEdificios = "api/edificio/all";
    public static String peticionQR = "api/solicitud/find/uuid/";
    public static String updateSolicitud = "/api/solicitud/update/";

    public ConexionJSON(){
    }


}
