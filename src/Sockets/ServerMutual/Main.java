package Sockets.ServerMutual;

import Sockets.ServerMutual.Client;
import Sockets.ServerMutual.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    //private static final String host = "192.168.67.181";
    private static final String host = "localhost";
    private static final int port = 1347;
    static Sockets.ServerMutual.Server server = new Server(port);

    public static void main(String[] args) {

        Thread serverThread = new Thread(server);
        serverThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Sockets.ServerMutual.Client c = new Client(host, port);
        if (!c.connect()) {
            System.out.println("ERROR: Can't connect to the server.");
            return;
        }

        c.send(c.formMessage());

        String ans = c.receive();

        System.out.println(ans);

    }

}

