package Sockets.ServerMutual;

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

        /*Sockets.ServerMutual.Client c = new Client(host, port);
        if (!c.connect()) {
            System.out.println("ERROR: Can't connect to the server.");
            return;
        }

        c.send(c.formMessage());

        String ans = c.receive();

        System.out.println(ans);*/

    }

}

