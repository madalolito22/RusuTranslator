package Sockets.ServerMutual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static Sockets.ServerMutual.Funcionalidades.*;

public class Peticion implements Runnable {

    private final String originT = "";
    private final String targetT = "";
    int port = 0;
    Scanner sc = new Scanner(System.in);
    ArrayList<String> sends;

    InputStreamReader isr = null;
    BufferedReader reader = null;
    PrintWriter pw = null;
    Socket socket;

    public Peticion(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            pw = new PrintWriter(socket.getOutputStream(), true);

            //Rellenamos el hashmap de idiomas
            hashmapIdiomas();

            //Rellenamos el hashmap de morse
            startMorseHashmap();

            //Le enviamos al cliente los idiomas disponibles
            printPossibleLanguages(pw);

            //Recogemos el flujo de datos del cliente
            isr = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(isr);

            //Cogemos la cadena mixta del cliente
            String all = reader.readLine();
            System.out.println("DEBUG: FULL STRING BY CLIENT: " + all);

            //Hago el split de la cadena usando la algoritmia
            clientLanguageNum = doSplit(all);

            System.out.println("DEBUG: NUMBER INTRODUCED BY THE CLIENT: " + clientLanguageNum);

            //Compruebo si es morse
            if (clientLanguageNum == 68) {

                System.out.println("DEBUG: MORSE!");
                //Convierto a morse
                doMorse(clientMessage, pw);

                //Compruebo si es nulo (No se ha introducido un número correcto)
            } else if (clientLanguage == null) {

                //Envío el primer número
                clientLanguage = idiomasDisponibles.get(Integer.parseInt(String.valueOf(clientLanguageNum.toString().charAt(0))));

                System.out.println("DEBUG: INCORRECT NUMBER ENTERED, FIRST NUMBER WILL BE USED!");

                //Si no es nulo
            } else if (clientMessage != null) {

                System.out.println("SERVER: Message received: " + clientMessage);

                //Guardo el mensaje traducido con el return de getAnswer
                String translatedMessage = getAnswer(clientMessage, clientLanguage);

                //Envío el mensaje traducido al cliente
                pw.println("Translated message: (" + clientLanguage.toUpperCase() + ") " + translatedMessage);

                //Corto el flujo de datos entre cliente y servidor
                pw.println("transReceived");

                System.out.println("SERVER: Message sent.");

            } else {
                System.out.println("ERROR: No message received from client OR language received not found.");
            }

            //Cierro readers y conexión
            reader.close();
            isr.close();
            socket.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



