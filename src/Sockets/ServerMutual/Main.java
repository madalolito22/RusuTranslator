package Sockets.ServerMutual;

public class Main {

    private static final int port = 1337;
    static Sockets.ServerMutual.Server server = new Server(port);

    public static void main(String[] args) {

        Thread serverThread = new Thread(server);
        serverThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}

