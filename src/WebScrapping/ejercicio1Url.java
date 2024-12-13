package WebScrapping;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ejercicio1Url {
    public static void main(String[] args) throws MalformedURLException {

        try {

            URL url = new URL("https://www.php.net/manual/es/tutorial.firstpage.php");

            InputStream is = url.openStream();

            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);

            BufferedReader br = new BufferedReader(isr);

            FileWriter fw = new FileWriter("fichero.php");

            String line = br.readLine();

            while (line != null) {
                fw.write(line);
                if(line.contains("div")){
                    System.out.println(line);
                }
                line = br.readLine();
            }

            fw.close();
            br.close();
            isr.close();
            is.close();


        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }
}