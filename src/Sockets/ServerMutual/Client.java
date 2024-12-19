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
    static Scanner sc = new Scanner(System.in);

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
            String line;
            while ((line = bfr.readLine()) != null) {
                ans += line + "\n"; // Acumulamos varias líneas
            }
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

    public String formMessage() {

        System.out.println("Escribe el texto a traducir: ");
        String mensaje = sc.nextLine();

        System.out.println("¿A qué idioma quieres traducir?");
        String idioma = sc.next();


        return mensaje+"~"+idioma;

    }
}
