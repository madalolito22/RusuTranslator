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

    static Scanner sc = new Scanner(System.in);

    //private static final String host = "192.168.67.181";
    private static final String host = "localhost";
    private static final int port = 1347;

    public static void main(String[] args) {

        Sockets.ServerMutual.Server s = new Server(port);

        Thread serverThread = new Thread(s);
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

        c.send(sc.nextLine() + "\n");

        c.send(sc.next() + "\n");

        String ans = c.receive();

        System.out.println(ans);

    }



}

