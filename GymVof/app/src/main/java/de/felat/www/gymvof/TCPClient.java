package de.felat.www.gymvof;

import android.os.AsyncTask;
import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
public class TCPClient {
    private String serverMessage;
    public static final String SERVERIP = "Sammarei.servebeer.com"; //your computer IP address
    public static final int SERVERPORT = 6800;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    PrintWriter out;
    BufferedReader in;
    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            Log.e("TCP Client","Mesage Sending Start");
            out.write(message);

            out.println(message);
            Log.e("TCP Client","printedLine");
            out.flush();
            Log.e("TCP Client","Mesage Sended"+message);
            Log.e("TCP Client","Message succesfully sended");

        }
        else {
            Log.e("TCP Client","No message sneded");
        }
    }
    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;
        try {
//here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            Log.e("TCP Client", "C: Connecting...");
//create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);
            Log.e("TCP Client", "Succesfully connected ");
            try {
//send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                Log.e("TCP Client", "C: Sent.");
                Log.e("TCP Client", "C: Done.");
//receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.e("TCP Client", "C: Reader Builded");
//in this while the client listens for the messages sent by the server

                while (mRun==true) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener!=null) {
//call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                        Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                        Log.e("RESPONSE FROM SERVER", "Calling method ");
                    }
                    serverMessage = null;
                }
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
            } catch (Exception e) {
                Log.e("TCP", "S: Error1", e);
            } finally {
//the socket must be closed. It is not possible to reconnect to this socket
// after it is closed, which means a new socket instance has to be created.
                socket.close();
                Log.e("TCP Client ", "Socket Closed ");
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error2", e);
        }
    }
    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
//class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}

/*
10-23 18:21:45.764 31531-31531/de.felat.www.gymvof E/AndroidRuntime: FATAL EXCEPTION: main
    Process: de.felat.www.gymvof, PID: 31531
    android.os.NetworkOnMainThreadException
        at android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork(StrictMode.java:1457)
        at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:108)
        at java.net.SocketOutputStream.write(SocketOutputStream.java:153)
        at sun.nio.cs.StreamEncoder.writeBytes(StreamEncoder.java:221)
        at sun.nio.cs.StreamEncoder.implFlushBuffer(StreamEncoder.java:291)
        at sun.nio.cs.StreamEncoder.implFlush(StreamEncoder.java:295)
        at sun.nio.cs.StreamEncoder.flush(StreamEncoder.java:141)
        at java.io.OutputStreamWriter.flush(OutputStreamWriter.java:229)
        at java.io.BufferedWriter.flush(BufferedWriter.java:254)
        at java.io.PrintWriter.newLine(PrintWriter.java:482)
        at java.io.PrintWriter.println(PrintWriter.java:629)
        at java.io.PrintWriter.println(PrintWriter.java:740)
        at de.felat.www.gymvof.TCPClient.sendMessage(TCPClient.java:30)
        at de.felat.www.gymvof.MainActivity$1.onClick(MainActivity.java:51)
        at android.view.View.performClick(View.java:6259)
        at android.view.View$PerformClick.run(View.java:24732)
        at android.os.Handler.handleCallback(Handler.java:789)
        at android.os.Handler.dispatchMessage(Handler.java:98)
        at android.os.Looper.loop(Looper.java:164)
        at android.app.ActivityThread.main(ActivityThread.java:6592)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.Zygote$MethodAndArgsCaller.run(Zygote.java:240)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:769)
 */
