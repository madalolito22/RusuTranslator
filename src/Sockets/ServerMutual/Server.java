package Sockets.ServerMutual;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class Server implements Runnable {

    private ServerSocket server = null;

    private int port = 0;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {

            System.out.println("INFO: SERVER LAUNCHING...");
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                System.out.println("ERROR: Unable to open socket on TCP " + port);
                return;
            }

            while (true) {

                System.out.println("INFO: WAITING FOR CLIENT");
                Socket conexion = server.accept();
                System.out.println("SERVER: CONNECTED!");
                Peticion p = new Peticion(conexion);
                Thread hilo = new Thread(p);
                hilo.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

