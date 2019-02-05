package com.popm.appark;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.popm.appark.Parquimetros.Parquimetro;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LugaresE extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lon = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugares);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ////////////////////////////////////////////////////////////////////
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                Integer id_tienda= (Integer) marker.getTag();

                Toast.makeText(getApplicationContext(),id_tienda.toString(),Toast.LENGTH_LONG).show();
                return false;
            }
        });


    }


    private void agregarMarcador(double lat, double lon) {
        LatLng coordenadas = new LatLng(lat, lon);
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);

        if (marcador != null) {
            marcador.remove();
        }

        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Ubicacion actual")
                .snippet("Aqui estoy")
        );
        mMap.animateCamera(ubicacion);


    }

    private void agregarParking(){
        for (Parquimetro parquimetro:Parquimetros()){
            LatLng ubicacion = new LatLng(parquimetro.getLatitudX(),parquimetro.getLatitudY());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(ubicacion)
                    .title(String.valueOf(parquimetro.getSensorID()))

            );

            marker.setTag(parquimetro.getSensorID());
        }
    }

    private void actualizarUbicacion(Location location) {

        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            agregarMarcador(lat, lon);
        }
    }


    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        actualizarUbicacion(location);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locListener);
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


    public List<Parquimetro> Parquimetros (){
        List<Parquimetro> tiendas  = new ArrayList<>();

        try {

            Statement statement = conexionBD().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PARQUIMETRO WHERE PARQUIMETRO.ESTADO='LIBRE'");

            while (resultSet.next()){
                tiendas.add(new Parquimetro(

                        resultSet.getInt("ID"),
                        resultSet.getString("ESTADO"),
                        resultSet.getFloat("LATITUD_X"),
                        resultSet.getFloat("LATITUD_Y")
                ));

            }

        }catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        return tiendas;

    }


    public void cambiarEstado (int id){

        try{

            //PreparedStatement pst = conexionBD().prepareStatement("INSERT INTO USUARIO (nombre, apellido_p, apellido_m, fecha_nac, correo, sexo, saldo, rfc, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pst = conexionBD().prepareStatement("UPDATE PARQUIMETRO SET ESTADO=? WHERE ID="+String.valueOf(id)+";");

            pst.setString(1,"Ocupado");


        }catch (SQLException e) {

            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_LONG).show();
            Log.e("ERROR", e.getMessage());
        }


    }

}
