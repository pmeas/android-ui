package zeinhijazi.com.pmeas.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

    Context context;
    Activity activity;

    // UDP socket used for the Service Discovery protocol.
    private DatagramSocket udpSocket;

    // TCP socket used in the service discovery protocol.
    private Socket tcpSocket;

    // PORT to listen on and the respective address to connect to (by default BROADCAST)
    private final int PORT = 10000;
    private final String ADDR = "255.255.255.255";

    // THe input and output streams used to communicate with the modulation application.
    public static DataOutputStream outputStream;
    public static BufferedReader inStream;

    public Bridge(Context context, Activity activity) throws IOException {
        this.context = context;
        this.activity = activity;
        udpSocket = new DatagramSocket();
        udpSocket.setBroadcast(true);
        udpSocket.setSoTimeout(5000);
    }

    @Override
    protected String doInBackground(Void... params) {

        String result = "FAIL";

        // Begin with the establishment of the connection protocol.
        try {
            // Send the UDP broadcast to retrieve the address to connect to.
            broadcastMessage();

            // Get the message the modulation application sent.
            InetAddress tcpAddr = receiveMessage();

            // Verify the message is not not and if it is, it is indicating a socket timeout.
            if(tcpAddr == null) {
                System.out.println("Socket Timed Out");
                return result;
            }

            // If the resulting communication is valid, connect tothe address through a TCP based communication.
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

    /**
     * Decides what happens based on the result of the doInBackground() method.
     *
     * @param result The result of the call from doInBackground().
     */
    @Override
    protected void onPostExecute(String result) {
        if(result.equals("FAIL")) {
            // If the sending was unsuccesful, print an error to the user.
//            Toast.makeText(context, "Connection could not be established.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Could not connect to server. Try again")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("Clicked retry button");
                            activity.recreate();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            // If the sending is unsuccesful, print a success message to the user.
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Broadcasts a UDP message to the modulation application to show intent to initiate the connection.
     *
     * @throws SocketException
     * @throws IOException
     */
    private void broadcastMessage() throws SocketException, IOException {

        byte[] broadcastMessage = "1".getBytes();
        InetAddress addr = InetAddress.getByName(ADDR);
        DatagramPacket sendPacket = new DatagramPacket(broadcastMessage, broadcastMessage.length, addr, PORT);
        udpSocket.send(sendPacket);
    }

    /**
     * Get the UDP message transmitted back by the modulation application. Use that information to
     * connect to the backend application.
     *
     * @return The address of the TCP socket to connect to.
     * @throws IOException
     */
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

    /**
     * Establish the TCP connection to the modulation application. With this connection, the user
     * can now send messages to and from the modulation application freely.
     *
     * @param addr The address of the TCP server to connect to.
     * @param port The port of the TCP server to connect to.
     * @return True if the message is successful.
     * @throws IOException
     */
    private boolean connectTCP(InetAddress addr, int port) throws IOException {
        tcpSocket = new Socket(addr, port);
        outputStream = new DataOutputStream(tcpSocket.getOutputStream());
        inStream = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

        return (outputStream != null) && (inStream != null);
    }

}
