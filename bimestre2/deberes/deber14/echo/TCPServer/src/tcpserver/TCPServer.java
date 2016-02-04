/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.net.*;
import java.io.*;
public class TCPServer {
 public static void main (String args[]) {
  try{
   int serverPort = 7899; // the server port
   ServerSocket listenSocket = new ServerSocket(serverPort);
   int i = 0;
   while(true) {
    System.out.println("Server listening for a connection");
    Socket clientSocket = listenSocket.accept();
    i++;
    System.out.println("Received connection " + i );
    Connection c = new Connection(clientSocket);
   }
  } catch(IOException e){
    System.out.println("Listen socket:"+e.getMessage());
  }
 }
}     