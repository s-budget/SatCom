package server;



//import com.fasterxml.jackson.databind.ObjectMapper;

//import hr.fer.progi.satcom.dto.MessageDto;

//import hr.fer.progi.satcom.models.Message;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.io.PrintWriter;

import java.net.InetAddress;

import java.net.Socket;

import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.util.Date;

import java.util.Objects;

public class TestComunicationWithSatelite {

    public static void main(String[] args) throws Exception {

        // u opcenitom slucaju, ovoj klasi je potrebno predati MessageDto objekt koji se zeli poslati na satelit.

        Message myMess = new Message("stat","sat", LocalDateTime.now(),"ovo je \nsadrzaj","UPLOAD",300000,"mode",1515);

        //System.out.println("Trying to send: \n"+myMess.getStringifiedJson());

        System.out.println(myMess);

        //System.out.println("-------");

        //MessageDto messageToSend = new MessageDto(myMess); // u messageDto objekt spremljeni samo potrebni atributi iz objekta message.

       // System.out.println("Trying to send messageDto: \n" + messageToSend);

        //System.out.println(messageToSend);

        System.out.println("-------");

        //TODO ODAVDE POCETI KOPIRANJE

        InetAddress address2 = InetAddress.getByName("localhost");

        Socket scEcho = new Socket(address2, 5050);

        PrintWriter pwOut = new PrintWriter(scEcho.getOutputStream(), false);

        BufferedReader brIn = new BufferedReader(new InputStreamReader(scEcho.getInputStream()));

        pwOut.println(myMess.getStringifiedJson());//sending Message myMess to server
        pwOut.flush();

        // pwOut.println(messageToSend.toJson());

        //System.out.println("STRINGIFIED MESSAGE DTO: " + messageToSend.toJson());

        // {"{": "haha"} - bernardov protuprimjer za ovo citanje odgovora od satelita

        // citanje input streama - odgovor od satelita:

        if(Objects.equals(brIn.readLine(), "1")) {

            StringBuilder strInLine = new StringBuilder(brIn.readLine());
            Message receivedMessage = new Message(strInLine.toString());

            System.out.println("Received from server: \n"+receivedMessage);//receive message from satelite

        }

        else {

            System.out.println("Communication with satellite failed");

        }

        /*

        status will be 1 if succesfull comunication with satelite, 0 otherwise

        recieved message will be relevant only for status 1

        */



        brIn.close();
        pwOut.close();
        scEcho.close();

        //TODO PA SVE DO TUD TREBA SKOPIRATI TAM DI SE KREIRA PORUKA KOJU PISE KORISNIK

    }

}

