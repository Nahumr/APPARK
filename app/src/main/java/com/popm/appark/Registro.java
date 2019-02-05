package com.popm.appark;

import android.content.Intent;
import android.os.Bundle;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.popm.appark.Usuarios.Usuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Registro extends AppCompatActivity {
    String nombre,apellidoP,apellidoM,email,telefono,contraseña,fechaNac,tarjeta;
    int [] fechan = new int[3];
    EditText edTnombre,edTapellidoP,edTapellidoM,edTemail,edTtelefono,edTcontraseña,edTfnacDia,edTfnacMes,edTfnacAno,edTtarjeta1,edTtarjeta2,edTtarjeta3,edTtarjeta4;
    Usuario usuario;
    TextView kat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registro);

        edTnombre= (EditText) findViewById(R.id.rnombre);
        edTapellidoP=(EditText) findViewById(R.id.rapellidop);
        edTapellidoM=(EditText) findViewById(R.id.rapellidom);
        edTemail=(EditText) findViewById(R.id.remail);
        edTtelefono=(EditText) findViewById(R.id.rTel);
        edTcontraseña=(EditText) findViewById(R.id.rpass1);
        edTtarjeta1=(EditText) findViewById(R.id.rtpago1);
        edTtarjeta2=(EditText) findViewById(R.id.rtpago2);
        edTtarjeta3=(EditText) findViewById(R.id.rtpago3);
        edTtarjeta4=(EditText) findViewById(R.id.rtpago4);
        edTfnacDia=(EditText) findViewById(R.id.rfDia);
        edTfnacMes=(EditText) findViewById(R.id.rfMes);
        edTfnacAno=(EditText) findViewById(R.id.rfAno);
        kat= (TextView) findViewById(R.id.kat);

        ImageButton fab = (ImageButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre=valores(edTnombre);
                apellidoP=valores(edTapellidoP);
                apellidoM=valores(edTapellidoM);
                email=valores(edTemail);
                telefono=valores(edTtelefono);
                contraseña=valores(edTcontraseña);
                tarjeta=valores(edTtarjeta1)+valores(edTtarjeta2)+valores(edTtarjeta3)+valores(edTtarjeta4);
                fechan[0]= Integer.valueOf(valores(edTfnacDia));
                fechan[1]= Integer.valueOf(valores(edTfnacMes));
                fechan[2]= Integer.valueOf(valores(edTfnacAno));
                usuario= new Usuario(nombre,apellidoP,apellidoM,email,telefono,contraseña,fechaNac,tarjeta);
                registrarUsuario();

            }
        });
    }

    public static String valores (EditText texto){

        String valor= texto.getText().toString().trim();

        if (valor==""){
            return "default";
        }else{
            return valor;
        }
    }

    public Connection conexionBD(){
        Connection conexion = null;

        try{

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            conexion = DriverManager.getConnection(""
                    + "jdbc:jtds:sqlserver://192.168.100.5/APPARK;"
                    + "user=SA;password=Rodriguez1$;");

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG ).show();
        }


        return conexion;
    }


    public void registrarUsuario(){

        try{

            //PreparedStatement pst = conexionBD().prepareStatement("INSERT INTO USUARIO (nombre, apellido_p, apellido_m, fecha_nac, correo, sexo, saldo, rfc, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pst = conexionBD().prepareStatement("INSERT INTO USUARIO VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            pst.setString(1,email);
            pst.setString(2,nombre);
            pst.setString(3,apellidoP);
            pst.setDate(4,new Date(fechan[2],fechan[1],fechan[0]));
            pst.setString(5,contraseña);
            pst.setString(6,telefono);
            pst.setString(7,tarjeta);
            pst.executeUpdate();
            Toast.makeText(getApplicationContext(),"Usuario creado exitosamente",Toast.LENGTH_LONG).show();

        }catch (SQLException e) {

            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();
            Log.e("ERROR", e.getMessage());
        }

    }

}
