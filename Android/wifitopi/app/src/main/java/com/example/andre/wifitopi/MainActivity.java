package com.example.andre.wifitopi;

import java.io.IOException;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.SocketException;



import android.app.Activity;
import android.os.Bundle;


import android.util.Log;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final TextView tx = (TextView) findViewById(R.id.textView);
        final TextView rc = (TextView) findViewById(R.id.textView2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            public void run() {
                try {
                    DatagramSocket clientsocket= new DatagramSocket(5005);

                    byte[] receivedata = new byte[5];
                    while(true) {
                        DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);
                       // Log.d("UDP", "S: Receiving...");
                        //rc.setText("C:Receiving.....");
                        clientsocket.receive(recv_packet);
                        String receivedstring = new String(recv_packet.getData());
                        Log.d("UDP", " Received String: " + receivedstring);
                        //rc.setText(" Received String: " + receivedstring);
                        InetAddress ipaddress = recv_packet.getAddress();
                        int port = recv_packet.getPort();
                        Log.d("UDP", "IPAddress : " + ipaddress.toString());
                        Log.d("UDP", "Port : " + Integer.toString(port));
                    }
                } catch (SocketException e) {
                    Log.e("UDP", "Socket Error", e);
                } catch (IOException e) {
                    Log.e("UDP", "IO Error", e);
                }
            }
        }).start();

    }
    }