package zeinhijazi.com.pmeas.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
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

/**
 * Created by zhijazi on 4/4/17.
 */

// TODO: Perform this at some start screen then making tcpSocket/streams public static so others can access them.
public class Bridge extends Thread{

    private DatagramSocket udpSocket;
    private Socket tcpSocket;

    private final int PORT = 10000;
    private final String ADDR = "255.255.255.255";

    public static DataOutputStream outputStream;
    public static BufferedReader inStream;

    public Bridge() throws IOException {
        udpSocket = new DatagramSocket();
        udpSocket.setBroadcast(true);
    }

    @Override
    public void run() {

        try {
            broadcastMessage(udpSocket);
            InetAddress tcpAddr = receiveMessage(udpSocket);
            connectTCP(tcpAddr, 10001);
        } catch (SocketException e) {
            Log.e("EffectsActivity", "SocketException caught: " + e.getMessage());
        } catch (IOException e) {
            Log.e("EffectsActivity", "IOException caught: " + e.getMessage());
        }

    }

    private void broadcastMessage(DatagramSocket udpSocket) throws SocketException, IOException {

        byte[] broadcastMessage = "1".getBytes();
        InetAddress addr = InetAddress.getByName(ADDR);
        DatagramPacket sendPacket = new DatagramPacket(broadcastMessage, broadcastMessage.length, addr, PORT);
        udpSocket.send(sendPacket);
    }

    private InetAddress receiveMessage(DatagramSocket udpSocket) throws IOException{
        byte[] receivedMessage = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receivedMessage, receivedMessage.length);
        udpSocket.receive(receivePacket);
        String portNum = new String(receivePacket.getData());
        return receivePacket.getAddress();
    }

    private void connectTCP(InetAddress addr, int port) throws IOException {
        tcpSocket = new Socket(addr, port);
        outputStream = new DataOutputStream(tcpSocket.getOutputStream());
        inStream = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
    }

    // TODO: Move this to the effects activity to be able to change UI elements.
    public static class BridgeAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String message = params[0];
            String result = null;

            try {
                if(!(outputStream == null) && !(inStream == null)) {
                    Bridge.outputStream.writeBytes(message);
                    result = Bridge.inStream.readLine();
                }
            } catch(IOException e) {
                Log.e("BRIDGE", "Caught IOException on doInBg: " + e.getMessage());
            }

            return result;
        }
    }

}
