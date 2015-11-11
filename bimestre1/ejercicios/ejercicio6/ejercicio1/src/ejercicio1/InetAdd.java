/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejercicio1;

import java.net.*;

/**
 *
 * @author 22B
 */
import java.util.Scanner;
public class InetAdd{
      

    public static void main(String[] args) throws UnknownHostException {
        Scanner leer = new Scanner(System.in);
        InetAddress compu = null;
        compu = InetAddress.getLocalHost();
        System.out.println("Local Host: "+compu);
         System.out.println("Direccion IP: "+compu.getHostAddress());
        System.out.println("Host: "+compu.getHostName());
       
        
        System.out.println("\nIngrese una Url");
        String name = leer.nextLine();
        
        InetAddress direc = InetAddress.getByName(name);
        System.out.println(direc);
        

        

    }   
}
