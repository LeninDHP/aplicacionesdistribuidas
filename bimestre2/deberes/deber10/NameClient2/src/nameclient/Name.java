/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nameclient;

import java.lang.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Name {

  BufferedReader din;
  PrintStream pout;

  public void getSocket() throws IOException {
        //crea un socket con el nombre del servidor y el nombre del puerto
    Socket server = new Socket(Symbols.nameServer, Symbols.serverPort);
    din = new BufferedReader(new InputStreamReader(server.getInputStream()));
    pout = new PrintStream(server.getOutputStream());

  }

  public int insertName(String name, String hname, int portnum) throws IOException {
    getSocket();
    //coge canl de salida y manda peticion 
    //ejm insert google google.com 5000
    pout.println("insert " + name + " " + hname + " " + portnum);
     //Espera la respuesta del servidor lo devuelve y lo imprime en pantalla
    pout.flush();
    return Integer.parseInt(din.readLine());
  }

  public PortAddr searchName(String name) throws IOException {
      //Crea un socket y manda la peticion 
    getSocket();
    pout.println("search " + name);
    pout.flush();
    String result = din.readLine();
    //Del vector escoje el puerto y el nombre
    StringTokenizer st = new StringTokenizer(result);
    int portnum = Integer.parseInt(st.nextToken());
    String hname = st.nextToken();
    //LÑa respuesta siempre va a ser el nombre y el puerto
    return new PortAddr(hname, portnum);
  }

  public static void main(String[] args) {

    Name myclient = new Name();
    try {
//        myclient.insertName("hello1", "oak.ece.utexas.edu", 1000);
//        PortAddr pa = myclient.searchName("hello1");
        
        //Inserta nombre del host
        myclient.insertName("yahoo", "yahoo.com", 2000);
        //busca el nombre del host
        //-----------------
        //Siempre debe inicializar el cliente
        //---------------------
        PortAddr pa = myclient.searchName("yahoo");
        System.out.println(pa.getHostName() + ":" + pa.getPort());

    } catch (Exception e) {
//      System.err.println("Server aborted  :" + e.toString());
      e.printStackTrace();
    }
  }
}
