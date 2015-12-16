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

  void handleclient(Socket theClient) {
    try {
      BufferedReader din = new BufferedReader(new InputStreamReader(theClient.getInputStream()));
      PrintWriter pout = new PrintWriter(theClient.getOutputStream());
      String getline = din.readLine();
      System.out.println(getline);
      StringTokenizer st = new StringTokenizer(getline);
      String tag = st.nextToken();
      if (tag.equals("search")) {
        int index = table.search(st.nextToken());
        System.out.println(index);
        if (index == -1) //  not  found
        {
          pout.println(-1 + " " + "nullhost");
        } else {
          pout.println(table.getPort(index) + " " + table.getHostName(index));
        }
      } else if (tag.equals("insert")) {
        String name = st.nextToken();
        String hostName = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        int retValue = table.insert(name, hostName, port);
        System.out.println(retValue);
        pout.println(retValue);
      }
      pout.flush();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  public static void main(String[] args) throws InterruptedException {
     
    NameServer ns = new NameServer();
    System.out.println("Nameserver started  :");
    try {
      
      ServerSocket listener = new ServerSocket(Symbols.serverPort);
      while (true) {
        Thread.sleep(20000);
        Socket aClient = listener.accept();
        ns.handleclient(aClient);
        aClient.close();
      }
    } catch (IOException e) {
      System.err.println("Server aborted  :" + e);
    }
  }
}
