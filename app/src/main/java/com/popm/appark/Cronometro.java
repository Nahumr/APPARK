package com.popm.appark;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Cronometro extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 00000;//Tiempo inicial del temporizador en milisegundos

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Spinner sp_horas;
    private Spinner sp_minutos;
    private int contador= 0;
    private TextView tiempo;
    private TextView cobro;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        tiempo = findViewById(R.id.tiempo);
        cobro = findViewById(R.id.cobro);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        sp_horas = findViewById(R.id.sp_hours);
        sp_minutos = findViewById(R.id.sp_minutes);

        tiempo.setVisibility(View.INVISIBLE);
        cobro.setVisibility(View.INVISIBLE);

        final String [] opciones_mm = {"00","15","30","45"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.spinner_item_op, opciones_mm);
        sp_minutos.setAdapter(adaptador);

        final String [] opciones_hh = {"00", "01", "02", "03", "04", "05", "06"};
        ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(this, R.layout.spinner_item_op, opciones_hh);
        sp_horas.setAdapter(adaptador2);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    mCountDownTimer.cancel();
                    mTimerRunning = false;
                    mTimeLeftInMillis = 1000;
                    int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
                    int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
                    String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

                    mTextViewCountDown.setText(timeLeftFormatted);
                    mButtonReset.setVisibility(View.INVISIBLE);
                    sp_horas.setVisibility(View.INVISIBLE);
                    sp_minutos.setVisibility(View.INVISIBLE);
                    Toast.makeText(Cronometro.this, "Su servicio ha finalizado", Toast.LENGTH_SHORT).show();

                } else {
                    if(mTimeLeftInMillis!=1000){
                        String selection = sp_horas.getSelectedItem().toString();
                        String selection2 = sp_minutos.getSelectedItem().toString();
                        int tiempoRentado_mm=0;
                        int tiempoRentado_hh=0;

                        if(selection.equals("00")){
                            tiempoRentado_hh = 000000;
                        } else if(selection.equals("01")){
                            tiempoRentado_hh = 60000;
                        } else if(selection.equals("02")) {
                            tiempoRentado_hh = 120000;
                        } else if(selection.equals("03")){
                            tiempoRentado_hh = 180000;
                        } else if(selection.equals("04")) {
                            tiempoRentado_hh = 240000;
                        } else if(selection.equals("05")){
                            tiempoRentado_hh = 300000;
                        } else if(selection.equals("06")) {
                            tiempoRentado_hh = 360000;
                        }

                        if(selection2.equals("00")){
                            tiempoRentado_mm = 000000;
                        } else if(selection2.equals("15")){
                            tiempoRentado_mm = 15000;
                        } else if(selection2.equals("30")) {
                            tiempoRentado_mm = 30000;
                        } else if(selection2.equals("45")){
                            tiempoRentado_mm = 45000;
                        }
                        if(selection.equals("00") && selection2.equals("00")){
                            Toast.makeText(Cronometro.this, "Especifique el tiempo que se desee rentar", Toast.LENGTH_SHORT).show();
                        }else{
                            int tiempoRentado = tiempoRentado_hh + tiempoRentado_mm;
                            contador = contador + tiempoRentado;
                            if(contador < 360001){
                                mTimeLeftInMillis=tiempoRentado;
                                startTimer();
                            } else{
                                Toast.makeText(Cronometro.this, "Sólo tiene permitido rentar 6 horas como máximo", Toast.LENGTH_SHORT).show();
                                contador = contador - tiempoRentado;
                            }

                        }
                    }
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = sp_horas.getSelectedItem().toString();
                String selection2 = sp_minutos.getSelectedItem().toString();
                int tiempoRentado_mm=0;
                int tiempoRentado_hh=0;
                int tiempoRentado = 0;

                if(selection.equals("00")){
                    tiempoRentado_hh = 000000;
                } else if(selection.equals("01")){
                    tiempoRentado_hh = 60000;
                } else if(selection.equals("02")) {
                    tiempoRentado_hh = 120000;
                } else if(selection.equals("03")){
                    tiempoRentado_hh = 180000;
                } else if(selection.equals("04")) {
                    tiempoRentado_hh = 240000;
                } else if(selection.equals("05")){
                    tiempoRentado_hh = 300000;
                } else if(selection.equals("06")) {
                    tiempoRentado_hh = 360000;
                }

                if(selection2.equals("00")){
                    tiempoRentado_mm = 000000;
                } else if(selection2.equals("15")){
                    tiempoRentado_mm = 15000;
                } else if(selection2.equals("30")) {
                    tiempoRentado_mm = 30000;
                } else if(selection2.equals("45")){
                    tiempoRentado_mm = 45000;
                }

                if(selection.equals("00") && selection2.equals("00")){
                    Toast.makeText(Cronometro.this, "Especifique el tiempo que se desea agregar", Toast.LENGTH_SHORT).show();
                }else{
                    pauseTimer();
                    tiempoRentado= tiempoRentado_hh + tiempoRentado_mm;
                    contador = contador + tiempoRentado;
                    if(contador < 360001){
                        mTimeLeftInMillis=mTimeLeftInMillis + tiempoRentado;
                        startTimer();
                    }else{
                        if(contador - tiempoRentado > 360001){
                            Toast.makeText(Cronometro.this, "Ha excedido el tiempo permitido para rentar", Toast.LENGTH_SHORT).show();
                            contador = contador - tiempoRentado;
                            startTimer();
                        } else {
                            Toast.makeText(Cronometro.this, "Sólo tiene permitido rentar en total 6 horas como máximo", Toast.LENGTH_SHORT).show();
                            contador = contador - tiempoRentado;
                            startTimer();
                        }
                    }
                }
            }
        });

        updateCountDownText();
    }

    public void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            //Cada cuántos milisegundos cuenta atrás nuestro timer
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                int min = (contador/1000)%60;
                int hor = (contador/1000)/60;
                String formato = String.format(Locale.getDefault(), "%02d:%02d", hor, min);
                tiempo.setText("Su tiempo rentado al momento es " +formato);
                tiempo.setVisibility(View.VISIBLE);
                int monto = (contador/15000)*2;
                cobro.setText("Monto cobrado al momento de $" +monto+".00");
                cobro.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();
                NotificationCompat.Builder builer = new NotificationCompat.Builder(Cronometro.this);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                PendingIntent pendingIntent = PendingIntent.getActivity(Cronometro.this, 01, intent, 0);
                builer.setContentIntent(pendingIntent);
                builer.setDefaults(Notification.DEFAULT_ALL);
                builer.setContentTitle("APPark");
                builer.setSmallIcon(R.mipmap.ic_launcher);
                builer.setContentText("Su tiempo ha expirado");
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(002,builer.build());
                mButtonReset.setVisibility(View.INVISIBLE);
                mButtonStartPause.setVisibility(View.INVISIBLE);
                Toast.makeText(Cronometro.this, "Se le ha generado una multa", Toast.LENGTH_SHORT).show();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
        if(minutes==0 && seconds==15){
            NotificationCompat.Builder builer2 = new NotificationCompat.Builder(Cronometro.this);
            Intent intent2 = new Intent(Intent.ACTION_VIEW);
            PendingIntent pendingIntent = PendingIntent.getActivity(Cronometro.this, 01, intent2, 0);
            builer2.setContentIntent(pendingIntent);
            builer2.setDefaults(Notification.DEFAULT_ALL);
            builer2.setContentTitle("APPark");
            builer2.setSmallIcon(R.mipmap.ic_launcher);
            builer2.setContentText("Su servicio está a punto de caducar");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(001, builer2.build());
        }
    }

    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Finalizar Servicio");
        } else {                                            //cambio de texto en el botón principal
            mButtonStartPause.setText("Iniciar Servicio");
            mButtonReset.setVisibility(View.INVISIBLE);

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", mEndTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTimeLeftInMillis = savedInstanceState.getLong("millisLeft");
        mTimerRunning = savedInstanceState.getBoolean("timerRunning");
        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTime = savedInstanceState.getLong("endTime");
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            startTimer();
        }
    }
}