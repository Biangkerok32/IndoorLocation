package com.jiahuan.svgmapview.sample;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

/**
 * Class responsible for UPD communication with Rpi
 */
public class WifiActivity {

    private String receivedstring;
    private int data = 0;
    private JSONObject json;
    public boolean isconnected;

    WifiActivity() {
    }

    WifiActivity(Date date) {
        inicialize();
    }

    public void inicialize() {
        new Thread(new Runnable() {

            public void run() {

                try {

                    DatagramSocket clientsocket = new DatagramSocket(5005);
                    Log.d("UDP", String.valueOf(clientsocket.isConnected()));

                    byte[] receivedata = new byte[1024];

                    while (true) {

                        DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);
                        clientsocket.receive(recv_packet);
                        isconnected = true;

                        receivedstring = new String(recv_packet.getData(), recv_packet.getOffset(), recv_packet.getLength());
                        json = new JSONObject(receivedstring);
                        // data=json.getString("sender");
                        //Log.d("UDP", " Received String: " + Integer.parseInt(receivedstring));
                        data = json.getInt("sender");
                        Log.d("json", json.getString("sender"));

                        InetAddress ipaddress = recv_packet.getAddress();

                        int port = recv_packet.getPort();

                        Log.d("UDP", "IPAddress : " + ipaddress.toString());

                        Log.d("UDP", "Port : " + Integer.toString(port));

                    }

                } catch (SocketException e) {

                    Log.e("UDP", "Socket Error", e);

                } catch (IOException e) {

                    Log.e("UDP", "IO Error", e);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }).start();
        isconnected=false;

    }

    public String getReceivedstring() {

        return receivedstring;
    }

    public int getData() {
        return data;
    }

    public boolean getisConnected() {
        return isconnected;
    }

}
