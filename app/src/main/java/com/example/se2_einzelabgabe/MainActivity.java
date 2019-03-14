package com.example.se2_einzelabgabe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText mnr;
    EditText antwort;
    String sentence;
    String modifiedSentence;
    String mnrt;
    clientSocket cSocket;
    String prim;
    String primFinal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        mnr = this.findViewById(R.id.MNrID);
        antwort = this.findViewById(R.id.antwortID);

        mnrt = mnr.getText().toString();
        Log.i("Matrikelnummereingabe", mnrt);

        cSocket = new clientSocket();
        new Thread(cSocket).start();
    }

    public void setAntwort(String s){
        antwort.setText(s);
    }

    public void calculate(View v){
        mnr = this.findViewById(R.id.MNrID);
        antwort = this.findViewById(R.id.antwortID);
        primFinal = "";

        prim = mnr.getText().toString();


        String[] arrayString = prim.split("");
        int[] arrayInt = new int[arrayString.length];



        for (int i = 1; i < arrayString.length; i++) {
            Log.i("Erstes Element", arrayString[i]);
            arrayInt[i] = Integer.valueOf(arrayString[i]);
        }



        for (int i = 0; i < arrayInt.length; i++) {
            boolean isPrimzahl = true;
            for (int j = 2; j < arrayInt[i]; j++) {
                if((arrayInt[i]%j) == 0){
                    isPrimzahl = false;
                }
            }
            if(isPrimzahl && arrayInt[i] != 0 && arrayInt[i] != 1){
                primFinal += arrayString[i];
            }
        }

        if(primFinal == ""){
            setAntwort("Es ist keine Primzahl enthalten");
        } else {
            setAntwort(primFinal);
        }



    }


    class clientSocket implements Runnable{

        Socket socket;
        @Override
        public void run() {

            try{
                socket = new Socket("se2-isys.aau.at", 53212);
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.i("Socketverbindung ", "Erfolgreich");

                sentence = mnrt;

                outToServer.writeBytes(sentence + '\n');

                modifiedSentence = inFromServer.readLine();

                Looper.prepare();
                setAntwort(modifiedSentence);

                Log.i("Rückgabe vom Server", modifiedSentence);

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
