package Sockets;

import java.io.*;
import java.net.Socket;

public class ejercicio2socket {

    public static void main(String[] args) {

        String host = "www.google.com";

        int port = 80;

        try {

            //Socket
            Socket socket = new Socket(host, port);

            //Flujos de bytes
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            //Leer todos los bytes sin codificar
            //byte[] bArray = is.readAllBytes();

            //Flujos que manejan caracteres
            InputStreamReader isr = new InputStreamReader(is);
            OutputStreamWriter osr = new OutputStreamWriter(os);

            //Leer un CHAR del stream codificado ASCII
            //int charInt = isr.read();

            //Flujos de líneas
            BufferedReader br = new BufferedReader(isr);
            PrintWriter pwr = new PrintWriter(osr);

            //Leo TODOS LOS CARACTERES DEL STRING HASTA EL \n
            String line = br.readLine();

            while (line != null){
                System.out.println(line);
                line = br.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

}
