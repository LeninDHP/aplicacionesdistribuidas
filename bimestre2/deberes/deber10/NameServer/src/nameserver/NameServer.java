/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nameserver;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NameServer {

  NameTable table;

  public NameServer() {
    table = new NameTable();
  }

  

  public static void main(String[] args) throws InterruptedException {
    
   
    System.out.println("Nameserver started  :");
    try {
      ServerSocket listener = new ServerSocket(Symbols.serverPort);
      while (true) {
        Socket aClient = listener.accept();
        serverhilo hilo = new serverhilo(aClient);
        
       
        hilo.start();
        
      }
    } catch (IOException e) {
      System.err.println("Server aborted  :" + e);
    }
  }
}
