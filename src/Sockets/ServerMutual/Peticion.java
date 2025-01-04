package Sockets.ServerMutual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Peticion implements Runnable {

    private final String originT = "";
    private final String targetT = "";
    int port = 0;
    Scanner sc = new Scanner(System.in);
    ArrayList<String> sends;
    String clientMessage = "";
    String clientLanguage = "";
    Integer clientLanguageNum = 0;
    LinkedHashMap<Integer, String> idiomasDisponibles = new LinkedHashMap<>();
    LinkedHashMap<String, String> idiomasAcortaciones = new LinkedHashMap<>();
    boolean gettingMessage = true;
    boolean gettingLng = false;

    InputStreamReader isr = null;
    BufferedReader reader = null;
    PrintWriter pw = null;
    Socket socket;

    private static String translate(String langFrom, String langTo, String text) throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycbxq5SL1XmfVK05ph1l4uu9u4LXQeCno5fmNdpval10rraJ-YJOsSeSzl9HzRJGlI_gp/exec" + "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) + "&target=" + langTo + "&source=" + langFrom;
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

    public Peticion(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            hashmapIdiomas();

            pw = new PrintWriter(socket.getOutputStream(), true);

            printPossibleLanguages(pw);

            isr = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(isr);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String all = reader.readLine();
            System.out.println(all);

            for (int i = 0; i < all.length(); i++) {
                Character charnow = all.charAt(i);

                if (!charnow.equals('~') && gettingMessage) {
                    clientMessage += charnow.toString();
                } else if (gettingLng) {
                    try{
                        clientLanguageNum = Integer.valueOf(clientLanguageNum.toString().concat(String.valueOf(Integer.parseInt(charnow.toString()))));
                    }catch (NumberFormatException e){
                        clientLanguageNum = Integer.valueOf(clientLanguageNum.toString().concat("1"));
                    }
                    try {
                        if (clientLanguage == null) {
                            clientLanguage = "er";
                        } else {
                            clientLanguage = idiomasDisponibles.get(clientLanguageNum);
                        }
                    } catch (Exception e) {
                        System.out.println("El cliente es bobo");
                        pw.flush();
                        pw.println("Idioma proporcionado no correcto... Cerrando conexión");
                        socket.close();
                    }
                    System.out.println(clientLanguageNum);
                    System.out.println(clientLanguage);
                    System.out.println(clientMessage);

                    if ((clientMessage.length() + clientLanguage.length()) - 1 == all.length() && idiomasDisponibles.containsKey(clientLanguageNum)) {
                        gettingLng = false;
                        break;
                    } else {
                        gettingLng = false;
                        System.out.println("Errorete");
                        break;
                    }

                } else if (charnow.equals('~')) {
                    gettingMessage = false;
                    gettingLng = true;
                }
            }

            if(clientLanguage.equals("er")){
                pw.println("Idioma no correcto, cerrando conexión...");
            }else if (clientMessage != null && clientLanguage != null && idiomasDisponibles.containsKey(clientLanguageNum)) {
                System.out.println("SERVER: Message received: " + clientMessage);
                String translatedMessage = getAnswer(clientMessage, clientLanguage);
                pw.println("Translated message: (" + clientLanguage.toUpperCase() + ") " + translatedMessage);
                pw.println("transReceived");
                System.out.println("SERVER: Message sent.");
            } else {
                System.out.println("ERROR: No message received from client OR language received not found.");
            }

            reader.close();
            isr.close();
            socket.close();

        } catch (
                IOException e) {
            System.out.println("ERROR: Failed connecting to client.");
        }

    }

    private String getAnswer(String message, String destination) throws IOException {
        return translate("", destination, message);
    }

    public void printPossibleLanguages(PrintWriter pw) {
        idiomasDisponibles.put(1, "af");
        idiomasDisponibles.put(2, "sq");
        idiomasDisponibles.put(3, "am");
        idiomasDisponibles.put(4, "ar");
        idiomasDisponibles.put(5, "eu");
        idiomasDisponibles.put(6, "bn");
        idiomasDisponibles.put(7, "bs");
        idiomasDisponibles.put(8, "bg");
        idiomasDisponibles.put(9, "zh-HK");
        idiomasDisponibles.put(10, "ca");
        idiomasDisponibles.put(11, "zh-CN");
        idiomasDisponibles.put(12, "zh-TW");
        idiomasDisponibles.put(13, "hr");
        idiomasDisponibles.put(14, "cs");
        idiomasDisponibles.put(15, "da");
        idiomasDisponibles.put(16, "nl");
        idiomasDisponibles.put(17, "en");
        idiomasDisponibles.put(18, "et");
        idiomasDisponibles.put(19, "tl");
        idiomasDisponibles.put(20, "fi");
        idiomasDisponibles.put(21, "fr");
        idiomasDisponibles.put(22, "gl");
        idiomasDisponibles.put(23, "de");
        idiomasDisponibles.put(24, "el");
        idiomasDisponibles.put(25, "gu");
        idiomasDisponibles.put(26, "ha");
        idiomasDisponibles.put(27, "he");
        idiomasDisponibles.put(28, "hi");
        idiomasDisponibles.put(29, "hu");
        idiomasDisponibles.put(30, "is");
        idiomasDisponibles.put(31, "id");
        idiomasDisponibles.put(32, "it");
        idiomasDisponibles.put(33, "ja");
        idiomasDisponibles.put(34, "jv");
        idiomasDisponibles.put(35, "kn");
        idiomasDisponibles.put(36, "km");
        idiomasDisponibles.put(37, "ko");
        idiomasDisponibles.put(38, "la");
        idiomasDisponibles.put(39, "lv");
        idiomasDisponibles.put(40, "lt");
        idiomasDisponibles.put(41, "ms");
        idiomasDisponibles.put(42, "ml");
        idiomasDisponibles.put(43, "mr");
        idiomasDisponibles.put(44, "my");
        idiomasDisponibles.put(45, "ne");
        idiomasDisponibles.put(46, "no");
        idiomasDisponibles.put(47, "pl");
        idiomasDisponibles.put(48, "pt-BR");
        idiomasDisponibles.put(49, "pt-PT");
        idiomasDisponibles.put(50, "pa");
        idiomasDisponibles.put(51, "ro");
        idiomasDisponibles.put(52, "ru");
        idiomasDisponibles.put(53, "sr");
        idiomasDisponibles.put(54, "si");
        idiomasDisponibles.put(55, "sk");
        idiomasDisponibles.put(56, "es");
        idiomasDisponibles.put(57, "su");
        idiomasDisponibles.put(58, "sw");
        idiomasDisponibles.put(59, "sv");
        idiomasDisponibles.put(60, "ta");
        idiomasDisponibles.put(61, "te");
        idiomasDisponibles.put(62, "th");
        idiomasDisponibles.put(63, "tr");
        idiomasDisponibles.put(64, "uk");
        idiomasDisponibles.put(65, "ur");
        idiomasDisponibles.put(66, "vi");
        idiomasDisponibles.put(67, "cy");

        int contador = 1;

        for (Map.Entry<String, String> entry : idiomasAcortaciones.entrySet()) {
            pw.println(entry.getKey() + " => " + contador);
            contador++;
            pw.flush();
        }

        System.out.println("Fin del bucle");

        pw.println("optionsReceived");

        pw.flush();

    }

    public LinkedHashMap<String, String> hashmapIdiomas() {
        idiomasAcortaciones.put("Afrikaans", "af");
        idiomasAcortaciones.put("Albanian", "sq");
        idiomasAcortaciones.put("Amharic", "am");
        idiomasAcortaciones.put("Arabic", "ar");
        idiomasAcortaciones.put("Basque", "eu");
        idiomasAcortaciones.put("Bengali", "bn");
        idiomasAcortaciones.put("Bosnian", "bs");
        idiomasAcortaciones.put("Bulgarian", "bg");
        idiomasAcortaciones.put("Cantonese", "yue");
        idiomasAcortaciones.put("Catalan", "ca");
        idiomasAcortaciones.put("Chinese (Simplified)", "zh-CN");
        idiomasAcortaciones.put("Chinese (Traditional)", "zh-TW");
        idiomasAcortaciones.put("Croatian", "hr");
        idiomasAcortaciones.put("Czech", "cs");
        idiomasAcortaciones.put("Danish", "da");
        idiomasAcortaciones.put("Dutch", "nl");
        idiomasAcortaciones.put("English", "en");
        idiomasAcortaciones.put("Estonian", "et");
        idiomasAcortaciones.put("Filipino", "fil");
        idiomasAcortaciones.put("Finnish", "fi");
        idiomasAcortaciones.put("French", "fr");
        idiomasAcortaciones.put("Galician", "gl");
        idiomasAcortaciones.put("German", "de");
        idiomasAcortaciones.put("Greek", "el");
        idiomasAcortaciones.put("Gujarati", "gu");
        idiomasAcortaciones.put("Hausa", "ha");
        idiomasAcortaciones.put("Hebrew", "he");
        idiomasAcortaciones.put("Hindi", "hi");
        idiomasAcortaciones.put("Hungarian", "hu");
        idiomasAcortaciones.put("Icelandic", "is");
        idiomasAcortaciones.put("Indonesian", "id");
        idiomasAcortaciones.put("Italian", "it");
        idiomasAcortaciones.put("Japanese", "ja");
        idiomasAcortaciones.put("Javanese", "jv");
        idiomasAcortaciones.put("Kannada", "kn");
        idiomasAcortaciones.put("Khmer", "km");
        idiomasAcortaciones.put("Korean", "ko");
        idiomasAcortaciones.put("Latin", "la");
        idiomasAcortaciones.put("Latvian", "lv");
        idiomasAcortaciones.put("Lithuanian", "lt");
        idiomasAcortaciones.put("Malay", "ms");
        idiomasAcortaciones.put("Malayalam", "ml");
        idiomasAcortaciones.put("Marathi", "mr");
        idiomasAcortaciones.put("Myanmar (Burmese)", "my");
        idiomasAcortaciones.put("Nepali", "ne");
        idiomasAcortaciones.put("Norwegian (Bokmål)", "no");
        idiomasAcortaciones.put("Polish", "pl");
        idiomasAcortaciones.put("Portuguese (Brazil)", "pt-BR");
        idiomasAcortaciones.put("Portuguese (Portugal)", "pt-PT");
        idiomasAcortaciones.put("Punjabi (Gurmukhi)", "pa");
        idiomasAcortaciones.put("Romanian", "ro");
        idiomasAcortaciones.put("Russian", "ru");
        idiomasAcortaciones.put("Serbian", "sr");
        idiomasAcortaciones.put("Sinhala", "si");
        idiomasAcortaciones.put("Slovak", "sk");
        idiomasAcortaciones.put("Spanish", "es");
        idiomasAcortaciones.put("Sundanese", "su");
        idiomasAcortaciones.put("Swahili", "sw");
        idiomasAcortaciones.put("Swedish", "sv");
        idiomasAcortaciones.put("Tamil", "ta");
        idiomasAcortaciones.put("Telugu", "te");
        idiomasAcortaciones.put("Thai", "th");
        idiomasAcortaciones.put("Turkish", "tr");
        idiomasAcortaciones.put("Ukrainian", "uk");
        idiomasAcortaciones.put("Urdu", "ur");
        idiomasAcortaciones.put("Vietnamese", "vi");
        idiomasAcortaciones.put("Welsh", "cy");
        return idiomasAcortaciones;
    }

}

