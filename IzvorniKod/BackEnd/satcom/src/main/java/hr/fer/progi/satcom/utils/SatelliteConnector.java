package hr.fer.progi.satcom.utils;

import hr.fer.progi.satcom.models.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

public class SatelliteConnector {

    /**
     * @param message message to be sent
     * @return received message from satellite
     */
    public Message sendMessage(Message message) {

        try {
            Message receivedMessage;
            InetAddress address2 = InetAddress.getByName("localhost");
            Socket scEcho = new Socket(address2, 5050);
            PrintWriter pwOut = new PrintWriter(scEcho.getOutputStream(), false);
            BufferedReader brIn = new BufferedReader(new InputStreamReader(scEcho.getInputStream()));
            pwOut.println(message.getStringifiedJson());//sending Message to server
            pwOut.flush();
            if (Objects.equals(brIn.readLine(), "1")) {
                StringBuilder strInLine = new StringBuilder(brIn.readLine());
                receivedMessage = new Message(strInLine.toString());
                System.out.println("Received from server: \n" + receivedMessage);//receive message from satelite
            }
            else {
                System.out.println("Communication with satellite failed");
                return null;
            }
            /*
            status will be 1 if successful communication with satellite, 0 otherwise
            received message will be relevant only for status 1
            */
            pwOut.close();
            brIn.close();
            scEcho.close();
            return receivedMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
