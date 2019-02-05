package com.popm.appark;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.popm.appark.Parquimetros.Parquimetro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Button iniciarS,registarse;
    EditText correo,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        iniciarS=(Button) findViewById(R.id.iniciarS);
        registarse=(Button) findViewById(R.id.registrarse);
        correo=(EditText) findViewById(R.id.iniciarCorreo);
        password=(EditText) findViewById(R.id.iniciarPass);


        iniciarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String correou = correo.getText().toString();
                String passu = password.getText().toString();

                if (passu == login(correou)){
                    Toast.makeText(getApplicationContext(),"Bienvenido",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"¿Estas seguro de tus datos?",Toast.LENGTH_LONG).show();
                }

            }
        });

        registarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ListSong = new Intent(getApplicationContext(), Registro.class);
                startActivity(ListSong);

            }
        });

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

    public String login (String Correo){

        String pass = "";
        try {

            Statement statement = conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT CORREO,PASS FROM USUARIO WHERE CORREO = '"+Correo+"';");

            while (resultSet.next()){

                    pass= resultSet.getString("PASS");
            }

        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return pass;
    }

}
