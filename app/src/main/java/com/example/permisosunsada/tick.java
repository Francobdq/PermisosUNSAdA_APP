package com.example.permisosunsada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class tick extends AppCompatActivity {

    public static int TICK = 0;
    public static int DANGER = 1;
    public static int WARNING = 2;

    public static int QR_INEXISTENTE = 0; // si el qr no existió nunca
    public static int QR_REPETIDO = 1; // Si el usuario está repetido
    public static int EDIFICIO_INCORRECTO = 2; // si el edificio es repetido
    public static int CORRECTO = 3; // todo bien
    public static int FECHA_INCORRECTA = 4;

    Animacion animacionController;

    ImageView componente, fondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // deshabilita la rotación de pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tick);
        getSupportActionBar().hide();

        componente = (ImageView) findViewById(R.id.frente);
        animacionController = new Animacion();
        fondo = (ImageView) findViewById(R.id.fondo);

        Inicializar();


        /*Intent intent = getIntent();
        int ani = intent.getIntExtra("tipoAni",1);
        String warning_msg = intent.getStringExtra("warning_msg");

        TextView warningmsgTV = (TextView) findViewById(R.id.warningmsg);


        if(ani == TICK){
            // animacion tick
            componente.setImageResource(R.drawable.avd_tick);
            fondo.setImageResource(R.drawable.avd_green_circle);
            warningmsgTV.setText("");
        }else if(ani == DANGER){
            // ani wrong
            componente.setImageResource(R.drawable.avd_danger);
            fondo.setImageResource(R.drawable.avd_red_circle);
            warningmsgTV.setText(warning_msg);
        }else if(ani == WARNING){
            // ANI DANGER
            componente.setImageResource(R.drawable.avd_danger);
            fondo.setImageResource(R.drawable.avd_warning_circle);
            warningmsgTV.setText(warning_msg);
        }else{
            // error cast
        }

        animacionController.Animar(componente,fondo);*/
    }

    private void danger(){
        componente.setImageResource(R.drawable.avd_danger);
        fondo.setImageResource(R.drawable.avd_red_circle);
    }

    private void tick(){
        componente.setImageResource(R.drawable.avd_tick);
        fondo.setImageResource(R.drawable.avd_green_circle);
    }

    private void warning(){
        componente.setImageResource(R.drawable.avd_warning);
        fondo.setImageResource(R.drawable.avd_warning_circle);
    }

    private void ShowQRData(String qr, String[] userData){
        TextView tv_nombreyapellido = (TextView) findViewById(R.id.tv_nombreyapellido);
        TextView tv_actividad = (TextView) findViewById(R.id.tv_actividad);
        TextView tv_edificio = (TextView) findViewById(R.id.tv_edificio);
        TextView tv_dia = (TextView) findViewById(R.id.tv_dia);

        if(userData == null)
            return;

        tv_nombreyapellido.setText("Nombre: " + userData[0]);//data.getString("nombre"));
        tv_actividad.setText("Actividad: " + userData[1]);//data.getString("actividad"));
        tv_edificio.setText("Edificio: " + userData[2]);//data.getString("edificio"));
        tv_dia.setText("Fecha de permiso: " + userData[3]);//data.getString("dia"));
    }


    private void Inicializar() {
        Intent intent = getIntent();
        int tipo = intent.getIntExtra("tipoAni",1);
        String qr = intent.getStringExtra("qr");
        String[] userData = intent.getStringArrayExtra("userData");
        TextView warningmsgTV = (TextView) findViewById(R.id.warningmsg);

        if(tipo == QR_INEXISTENTE){
            danger();
            warningmsgTV.setText("NO EXISTE SOLICITUD PARA ESE QR.");
        }else if(tipo == QR_REPETIDO){
            danger();
            warningmsgTV.setText("EL QR YA FUE UTILIZADO.");
            ShowQRData(qr, userData);
        }else if(tipo == EDIFICIO_INCORRECTO){
            warning();
            warningmsgTV.setText("EL EDIFICIO NO ES EL CORRECTO.");
            ShowQRData(qr, userData);
        }else if(tipo == FECHA_INCORRECTA) {
            warning();
            warningmsgTV.setText("EL PERMISO NO ES PARA HOY.");
            ShowQRData(qr, userData);
        }else if(tipo == CORRECTO){
            tick();
            warningmsgTV.setText("");
            ShowQRData(qr, userData);
        }
        else return;

        animacionController.Animar(componente,fondo);
    }

}