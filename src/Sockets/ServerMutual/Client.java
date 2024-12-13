package Sockets.ServerMutual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final String errorMSG = "ERROR";
    private String host = "";
    private int port = 0;
    private Socket socket = null;
    private InputStreamReader isr = null;
    private BufferedReader bfr = null;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            System.out.println("CLIENT: Connected.");
            return true;
        } catch (IOException e) {
            System.out.println("CLIENT: Connection rejected.");
            return false;
        }
    }

    public String receive() {
        try {
            isr = new InputStreamReader(socket.getInputStream());
            bfr = new BufferedReader(isr);
            String ans = bfr.readLine();
            System.out.println("CLIENT: Message received.");
            bfr.close();
            isr.close();
            return ans;
        } catch (IOException e) {
            e.printStackTrace();
            return errorMSG;
        }
    }

    public boolean send(String message) {
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println(message);
            System.out.println("CLIENT: Message sent.");
            pw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
