package server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ServerExecutor implements Runnable {

    private final Socket socket;


    public ServerExecutor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), false); BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            StringBuilder strInLine= new StringBuilder(in.readLine());
            Message recievedMessage = new Message(strInLine.toString());
            System.out.println("RECIEVED MESSAGE: " + recievedMessage);
            TimeUnit.SECONDS.sleep(1);
            if(recievedMessage.getText().length() == 0 || !recievedMessage.allDefined()) {
                out.println("0");
            }
            else {
                out.println("1");
                Message responseMessage =new Message(strInLine.toString());
                responseMessage.setText("Response generated on satellite");
                responseMessage.setDirection("DOWNLOAD");
                Date date =new java.sql.Date(System.currentTimeMillis());
                responseMessage.setCreationDate(LocalDateTime.now());
                out.println(responseMessage.getStringifiedJson());
                out.flush();
            }

        } catch (IOException ioe) {
            System.out.println("Error handling client.\n"+ ioe);
        } catch (ParseException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}