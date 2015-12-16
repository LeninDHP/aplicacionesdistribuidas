/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 22B
 */
public class serverhilo extends Thread{
    private static NameTable table = new NameTable();
    Socket theClient;
    public serverhilo(Socket aClient){
        theClient = aClient;
    }
    
    public void run(){
        try {
            Thread.sleep(20000);
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
    }   catch (InterruptedException ex) {
            Logger.getLogger(serverhilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
