/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

/**
 *
 * @author David
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author David
 */
public class ChatClient implements Runnable{
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    byte[] sendData = new byte[65508];
    byte[] receiveData = new byte[65508];
     DatagramPacket receivePacket = new DatagramPacket(receiveData, 256);
    
  
    public void run() {
        
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
  
            while (true) {
                
                //envia mensaje
               System.out.print("Yo: ");
               String clientSentence = inFromUser.readLine();
               sendData = clientSentence.getBytes();
               DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
               clientSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.getMessage();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
       byte[] sendData = new byte[65508];
       byte[] receiveData = new byte[65508];
       DatagramPacket receivePacket = new DatagramPacket(receiveData, 256);
        try(DatagramSocket clientSocket = new DatagramSocket()){
            InetAddress IPAddress = InetAddress.getByName("localhost");
            
            //ingresar el nombre por teclado
            System.out.println("Ingrese un nombre de usuario: ");
            String clientUsername = inFromUser.readLine();
            //toma el nombre del usuario
            String clientSentence = clientUsername;
            sendData = clientSentence.getBytes();
            
            
            //envia el nombre del usuario
            
            DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,IPAddress,9876);
            clientSocket.send(sendPacket);
            //recibe el nombre del usuario
            clientSocket.receive(receivePacket);
            
            String serverSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
            //imprime el nombre del usuario
            System.out.println("Esta conversando con : "+serverSentence);
            System.out.println("Puede empezar a enviar mensajes a: "+serverSentence);
        } catch (Exception e) {
        }
        
        ChatClient hilo = new ChatClient();
        Thread t1 = new Thread(hilo);
        t1.start();
    }
}
