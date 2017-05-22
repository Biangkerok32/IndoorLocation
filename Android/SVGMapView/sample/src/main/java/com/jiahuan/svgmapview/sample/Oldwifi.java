package com.jiahuan.svgmapview.sample;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

/**
 * Created by andre on 21-05-2017.
 */

public class Oldwifi {


    public  String receivedstring;

    Oldwifi() {

    }

    Oldwifi(Date date){init();}

    private void init() {
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

                        receivedstring= new String(recv_packet.getData());

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
    public String getdata(){return this.receivedstring;}
}
