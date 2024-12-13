package Sockets.ServerMutual;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Server implements Runnable {

    int port = 0;
    Scanner sc = new Scanner(System.in);
    private Socket client = null;
    private ServerSocket server = null;
    ArrayList<String> sends;
    private boolean awaitingMessage = true;
    private boolean messageReceived = false;
    private boolean awaitingOrigin = false;
    private boolean originReceived = false;
    private boolean awatingTarget = false;
    private boolean targetReceived = false;
    private String originT = "";
    private String targetT = "";

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = null;
            BufferedReader reader = null;
            PrintWriter pw = null;

            System.out.println("INFO: Server launching...");
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                System.out.println("ERROR: Unable to open socket on TCP " + port);
                return;
            }

            while (true) {

                client = server.accept();
                System.out.println("SERVER: Connection established!");

                pw = new PrintWriter(client.getOutputStream(), true);

                System.out.println("¿Qué quieres traducir?");
                //pw.println("¿Qué quieres traducir?");

                isr = new InputStreamReader(client.getInputStream());
                reader = new BufferedReader(isr);

                String all = reader.readLine();
                String clientMessage = all.substring(0, all.length()-2);
                targetT = all.substring(all.length()-2);

                //System.out.println("Idioma de origen: ");

                //System.out.println(clientMessage);

                System.out.println("Idioma de destino: ");

                /*while (awaitingOrigin){
                    pw.println("Elige idioma de tu texto: " +
                            "1. Español " +
                            "2. Inglés " +
                            "3. Rumano " +
                            "4. Alemán " +
                            "5. Francés " +
                            "6. Mandrarín "
                    );
                    String origin = reader.readLine();
                    awaitingOrigin = false;
                    originReceived = true;
                }

                do{
                    if(reader.readLine().contains("/m")){
                        awaitingMessage = false;
                        messageReceived = true;
                        awaitingOrigin = true;
                        clientMessage = clientMessage.substring(0, clientMessage.length()-2);
                        System.out.println(clientMessage);
                    }else{
                        System.out.println("No se ha enviado correctamente el mensaje");
                    }
                }while (awaitingMessage);

                do {
                    if(reader.readLine().contains("/o")){
                        awaitingOrigin = false;
                        originReceived = true;
                        awatingTarget = true;
                        originT = originT.substring(0, clientMessage.length()-2);
                        System.out.println(originT);
                    }else{
                        System.out.println("No se ha enviado correctamente el mensaje");
                    }
                }while (awaitingOrigin);

                do{
                    if(reader.readLine().contains("/d")){
                        awatingTarget = true;
                        targetReceived = true;
                        targetT = targetT.substring(0, clientMessage.length()-2);
                        System.out.println(targetT);
                    }else{
                        System.out.println("No se ha enviado correctamente el mensaje");
                    }
                }while (awatingTarget);*/

                if (clientMessage != null && targetT != null) {

                    System.out.println("SERVER: Message received: " + clientMessage);
                    String translatedMessage = getAnswer(clientMessage, originT, targetT);
                    System.out.println("Translated message: (" + targetT + ") " + translatedMessage);
                    //pw.println("Translated message: (" + targetT.toUpperCase() + " ) " + getAnswer(clientMessage, originT, targetT));
                    System.out.println("SERVER: Message sent.");
                    pw.close();

                } else {

                    System.out.println("ERROR: No message received from client.");

                }

                reader.close();
                isr.close();
                client.close();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Failed connecting to client.");
        }
    }

    private String getAnswer(String message, String origin, String destination) throws IOException {
        return translate(origin, destination, message);
    }

    private static String translate(String langFrom, String langTo, String text) throws IOException {

        String urlStr = "https://script.google.com/macros/s/AKfycbxq5SL1XmfVK05ph1l4uu9u4LXQeCno5fmNdpval10rraJ-YJOsSeSzl9HzRJGlI_gp/exec" +
                "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

}
