package zeinhijazi.com.pmeas.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by zhijazi on 4/4/17.
 */

// TODO: Perform this at some start screen then making tcpSocket/streams public static so others can access them.
public class Bridge extends AsyncTask<Void, Void, String>{

    private DatagramSocket udpSocket;
    private Socket tcpSocket;

    private final int PORT = 10000;
    private final String ADDR = "255.255.255.255";

    public static DataOutputStream outputStream;
    public static BufferedReader inStream;

    public Bridge() throws IOException {
        udpSocket = new DatagramSocket();
        udpSocket.setBroadcast(true);
        udpSocket.setSoTimeout(5000);
    }

    @Override
    protected String doInBackground(Void... params) {

        String result = "FAIL";

        try {
            broadcastMessage();

            InetAddress tcpAddr = receiveMessage();
            if(tcpAddr == null) {
                System.out.println("Socket Timed Out");
                return result;
            }

            if( connectTCP(tcpAddr, 10001) ) {
                result = "Success";
            }
        } catch (SocketException e) {
            Log.e("EffectsActivity", "SocketException caught: " + e.getMessage());
        } catch (IOException e) {
            Log.e("EffectsActivity", "IOException caught: " + e.getMessage());
        }

        return result;
    }

    private void broadcastMessage() throws SocketException, IOException {

        byte[] broadcastMessage = "1".getBytes();
        InetAddress addr = InetAddress.getByName(ADDR);
        DatagramPacket sendPacket = new DatagramPacket(broadcastMessage, broadcastMessage.length, addr, PORT);
        udpSocket.send(sendPacket);
    }

    @Nullable
    private InetAddress receiveMessage() throws IOException{
        byte[] receivedMessage = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receivedMessage, receivedMessage.length);
        try {
            udpSocket.receive(receivePacket);
        } catch (SocketTimeoutException e) {
            System.out.println("SOCKET TIMED OUT!");
            return null;
        }
        String portNum = new String(receivePacket.getData());
        return receivePacket.getAddress();
    }

    private boolean connectTCP(InetAddress addr, int port) throws IOException {
        tcpSocket = new Socket(addr, port);
        outputStream = new DataOutputStream(tcpSocket.getOutputStream());
        inStream = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

        return (outputStream != null) && (inStream != null);
    }

}
