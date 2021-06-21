package com.example.permisosunsada;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class Animacion {

    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawableCompat avdBackground;
    AnimatedVectorDrawable avd2;
    AnimatedVectorDrawable avd2Background;

    public Animacion(){

    }


    public void Animar(ImageView frente, ImageView fondo){
        Drawable drawable = frente.getDrawable();
        Drawable drawableBackground = fondo.getDrawable();

        //Toast.makeText(getApplicationContext(), "Animacion", Toast.LENGTH_SHORT).show();

        if(drawable instanceof AnimatedVectorDrawableCompat){
            avd  = (AnimatedVectorDrawableCompat) drawable;
            avdBackground = (AnimatedVectorDrawableCompat) drawableBackground;
            avd.start();
            avdBackground.start();

        }else if(drawable instanceof AnimatedVectorDrawable){
            avd2 = (AnimatedVectorDrawable) drawable;
            avd2Background = (AnimatedVectorDrawable) drawableBackground;
            avd2.start();
            avd2Background.start();
        }
    }
}
