package com.example.permisosunsada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class configMenu extends AppCompatActivity{

    Spinner spinnerDeCifrado;
    //TextView tv_host;
    EditText et_host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // deshabilita la rotación de pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_menu);
        getSupportActionBar().hide();

        spinnerDeCifrado = (Spinner) findViewById(R.id.spinner_host);
        //tv_host = (TextView) findViewById(R.id.tv_host);
        et_host = (EditText) findViewById(R.id.et_host);
        et_host.setText(PEDIR_URL.getURL());
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.array_tipo_cifrado, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDeCifrado.setAdapter(adapter);
        // selecciono el http o el https según corresponda
        spinnerDeCifrado.setSelection(((PEDIR_URL.Cifrado()) ? 1 : 0));
    }



    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }




    public void Guardar(View view){
        String cifrado = spinnerDeCifrado.getSelectedItem().toString();
        String host = et_host.getText().toString();//tv_host.getText().toString();
        PEDIR_URL.ActualizarGuardarCambios(cifrado, host,configMenu.this);

    }
}