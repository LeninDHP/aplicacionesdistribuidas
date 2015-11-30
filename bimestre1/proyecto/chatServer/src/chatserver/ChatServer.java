/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

/**
 *
 * @author David
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 22B
 */
public class ChatServer implements Runnable{
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    
      byte[] receiveData = new byte[65508];
      byte[] sendData = new byte[65508];
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      
    
    public void run(){
        try {
            DatagramSocket serverSocket = new DatagramSocket(9876);
                 
//Ingresa el nombre del usuario
      System.out.println("Ingrese un nombre de usuario: ");
      //toma el nombre del usuario
      String serverUsername = inFromUser.readLine();
      //Envia el nombre del usuario
      String serverSentence = serverUsername;
      sendData = serverSentence.getBytes();
      serverSocket.receive(receivePacket);
      //recibe el puerto del cliente y direccion
      InetAddress IPAddress = receivePacket.getAddress();
      int port = receivePacket.getPort();
      //una vez q recibe lo envia al puerto y a la direccion del cliente el nombre de usuario
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
      serverSocket.send(sendPacket);
      
      String clientSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
      //imprime el nombre de usuario
      System.out.println("Esta conversando con: "+clientSentence);
      System.out.println("Puede empezar a enviar mensajes a: "+clientSentence);

     
          

      while (true) {
         
        
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        clientSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("\n"+clientSentence);
        port = receivePacket.getPort();

        IPAddress = receivePacket.getAddress();

        receivePacket.getPort();

        System.out.print("Yo: ");
        
        /*
        //envia mensaje
          
        serverSentence =  inFromUser.readLine();
        sendData = serverSentence.getBytes();
        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        serverSocket.send(sendPacket);
            */     
                   
      }
    } catch (IOException ex) {
      Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
   

    public static void main(String[] args) {
        
         
ChatServer hilo = new ChatServer();
        Thread hil = new Thread(hilo);
        hil.start(); 
    } 
}

