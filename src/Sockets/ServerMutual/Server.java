package Sockets.ServerMutual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server implements Runnable {


    private ServerSocket server = null;
    PrintWriter pw;

    private int port = 0;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {

            System.out.println("INFO: Server launching...");
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                System.out.println("ERROR: Unable to open socket on TCP " + port);
                return;
            }

            while (true) {

                System.out.println("Esperando a cliente");
                Socket conexion = server.accept();
                System.out.println("SERVER: Connection established!");
                Peticion p = new Peticion(conexion);
                Thread hilo = new Thread(p);
                hilo.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

