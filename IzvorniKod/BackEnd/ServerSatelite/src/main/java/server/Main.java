package server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        //ObjectMapper objectMapper = new ObjectMapper();

        try(ServerSocket ssc = new ServerSocket(5050,10, InetAddress.getByName("127.0.0.1"))) {
            ExecutorService pool = Executors.newFixedThreadPool(10);//limit of simultaneous calls to server
            while (true) {
                Socket scClient = ssc.accept();
                System.out.println("Received a message on satellite!");
                // citanje input streama (poslane poruke):
                ServerExecutor st = new ServerExecutor(scClient/*, objectMapper*/);
                pool.execute(st);
            }
        } catch (IOException e) {
            System.out.println("Error starting server.\n"+e);
        }
    }
}