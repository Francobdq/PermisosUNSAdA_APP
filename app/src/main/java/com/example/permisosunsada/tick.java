package com.example.permisosunsada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class tick extends AppCompatActivity {

    public static int TICK = 0;
    public static int DANGER = 1;
    public static int WARNING = 2;


    Animacion animacionController;

    ImageView componente, fondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tick);
        Intent intent = getIntent();
        int ani = intent.getIntExtra("tipoAni",1);
        String warning_msg = intent.getStringExtra("warning_msg");

        animacionController = new Animacion();
        componente = (ImageView) findViewById(R.id.frente);
        TextView warningmsgTV = (TextView) findViewById(R.id.warningmsg);
        fondo = (ImageView) findViewById(R.id.fondo);

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

        animacionController.Animar(componente,fondo);
    }


    private void AnimacionTick(){



    }

}