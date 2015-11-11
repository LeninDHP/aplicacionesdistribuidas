package datagramclient;

/**
* un EchoClient que se lee datos del usuario, lo envia al servidor.
* El servidor recibe los datagramas y regresa el mismo datagrama y el cliente lo
* imprime. Se conecta solo al localhost.
*
*/
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class  DatagramClient  {
  public static void  main(String  []  args)  {
    String hostname;
    int  port  =  2018;
    int  len  =  1024;
    DatagramPacket sPacket, rPacket;
    //Primera parte
    hostname = "####"; //colocar aqui la direccion IP de la maquina que deseamos que sea nuestro servidor.
    // el servidor funciona para cualquier cliente que solicite su servicio.
    try{
      InetAddress ia = InetAddress.getByName(hostname);
      DatagramSocket datasocket = new DatagramSocket();
      BufferedReader stdinp = new BufferedReader(
              new InputStreamReader(System.in ));
      String mensaje ="";
      
      
      //En caso de que nuestro cliente desee salir y no continuar.
      while (!mensaje.equals("si"))  {
        try  {
             
          System.out.println("Ingrese un mensaje:");
          String echoline  =  stdinp.readLine();
          
          byte [] buffer = new byte[echoline.length()];
          buffer = echoline.getBytes();
          sPacket = new DatagramPacket( buffer, buffer.length, ia, port);
          System.out.println("Enviando mensaje al servidor...");
          datasocket.send(sPacket);
          
          byte[] rbuffer = new byte[len] ;
          rPacket = new DatagramPacket(rbuffer, rbuffer.length);
          System.out.println("Esperando respuesta del servidor...");
          datasocket. receive (rPacket);
          
          String retstring = new String(rPacket.getData(), 0, rPacket.getLength());
          System.out.println("El servidor devolvió el siguiente mensaje:");
          System.out.println(retstring);
           Scanner read = new Scanner(System.in);
            System.out.println("Desea Salir?");
            mensaje = read.nextLine();
            
        }  catch  (IOException  e )  {
          System.err.println( e );
        }
      }  //  while
    } catch (UnknownHostException e ) {
      System.err.println( e );
    } catch ( SocketException se ) {
      System.err.println ( se ) ;
    }
  }//end main
}