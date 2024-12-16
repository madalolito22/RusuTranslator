package Sockets.ServerMutual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Server implements Runnable {

    private final String originT = "";
    private final String targetT = "";
    int port = 0;
    Scanner sc = new Scanner(System.in);
    ArrayList<String> sends;
    String clientMessage = "";
    String clientLanguage = "";
    HashMap<String, Integer> idiomasDisponibles = new HashMap<>();
    boolean gettingMessage = true;
    boolean gettingLng = false;
    private Socket client = null;
    private ServerSocket server = null;


    public Server(int port) {
        this.port = port;
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

    @Override
    public void run() {
        try {

            hashmapIdiomas();

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

                isr = new InputStreamReader(client.getInputStream());
                reader = new BufferedReader(isr);

                String all = reader.readLine();

                for (int i = 0; i < all.length(); i++) {

                    Character charnow = all.charAt(i);

                    if (!charnow.equals('~') && gettingMessage) {

                        clientMessage += charnow.toString();

                    } else if (gettingLng) {

                        clientLanguage = clientLanguage.concat(charnow.toString());

                        if ((clientMessage.length() + clientLanguage.length()) - 1 == all.length()) {
                            gettingLng = false;
                            break;
                        }

                    } else if (charnow.equals('~')) {

                        gettingMessage = false;
                        gettingLng = true;

                    }
                }

                if (clientMessage != null && clientLanguage != null) {

                    System.out.println("SERVER: Message received: " + clientMessage);
                    String translatedMessage = getAnswer(clientMessage, clientLanguage);
                    //System.out.println("Translated message: (" + clientLanguage.toUpperCase() + ") " + translatedMessage);
                    pw.println("Translated message: (" + clientLanguage.toUpperCase() + ") " + getAnswer(clientMessage, clientLanguage));
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

    private String getAnswer(String message, String destination) throws IOException {
        return translate("", destination, message);
    }

    public HashMap<String, Integer> hashmapIdiomas() {

        idiomasDisponibles.put("af", 1);
        idiomasDisponibles.put("sq", 2);
        idiomasDisponibles.put("am", 3);
        idiomasDisponibles.put("ar", 4);
        idiomasDisponibles.put("eu", 5);
        idiomasDisponibles.put("bn", 6);
        idiomasDisponibles.put("bs", 7);
        idiomasDisponibles.put("bg", 8);
        idiomasDisponibles.put("zh-HK", 9);
        idiomasDisponibles.put("ca", 10);
        idiomasDisponibles.put("zh-CN", 11);
        idiomasDisponibles.put("zh-TW", 12);
        idiomasDisponibles.put("hr", 13);
        idiomasDisponibles.put("cs", 14);
        idiomasDisponibles.put("da", 15);
        idiomasDisponibles.put("nl", 16);
        idiomasDisponibles.put("en", 17);
        idiomasDisponibles.put("et", 18);
        idiomasDisponibles.put("tl", 19);
        idiomasDisponibles.put("fi", 20);
        idiomasDisponibles.put("fr", 21);
        idiomasDisponibles.put("gl", 22);
        idiomasDisponibles.put("de", 23);
        idiomasDisponibles.put("el", 24);
        idiomasDisponibles.put("gu", 25);
        idiomasDisponibles.put("ha", 26);
        idiomasDisponibles.put("he", 27);
        idiomasDisponibles.put("hi", 28);
        idiomasDisponibles.put("hu", 29);
        idiomasDisponibles.put("is", 30);
        idiomasDisponibles.put("id", 31);
        idiomasDisponibles.put("it", 32);
        idiomasDisponibles.put("ja", 33);
        idiomasDisponibles.put("jv", 34);
        idiomasDisponibles.put("kn", 35);
        idiomasDisponibles.put("km", 36);
        idiomasDisponibles.put("ko", 37);
        idiomasDisponibles.put("la", 38);
        idiomasDisponibles.put("lv", 39);
        idiomasDisponibles.put("lt", 40);
        idiomasDisponibles.put("ms", 41);
        idiomasDisponibles.put("ml", 42);
        idiomasDisponibles.put("mr", 43);
        idiomasDisponibles.put("my", 44);
        idiomasDisponibles.put("ne", 45);
        idiomasDisponibles.put("no", 46);
        idiomasDisponibles.put("pl", 47);
        idiomasDisponibles.put("pt-BR", 48);
        idiomasDisponibles.put("pt-PT", 49);
        idiomasDisponibles.put("pa", 50);
        idiomasDisponibles.put("ro", 51);
        idiomasDisponibles.put("ru", 52);
        idiomasDisponibles.put("sr", 53);
        idiomasDisponibles.put("si", 54);
        idiomasDisponibles.put("sk", 55);
        idiomasDisponibles.put("es", 56);
        idiomasDisponibles.put("su", 57);
        idiomasDisponibles.put("sw", 58);
        idiomasDisponibles.put("sv", 59);
        idiomasDisponibles.put("ta", 60);
        idiomasDisponibles.put("te", 61);
        idiomasDisponibles.put("th", 62);
        idiomasDisponibles.put("tr", 63);
        idiomasDisponibles.put("uk", 64);
        idiomasDisponibles.put("ur", 65);
        idiomasDisponibles.put("vi", 66);
        idiomasDisponibles.put("cy", 67);

        return idiomasDisponibles;
    }

}
