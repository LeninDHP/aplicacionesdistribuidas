/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deber6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author David
 */
public class sumaArregloSecuencial {
    
    public static void main(String[] args){
        Scanner archivo = new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo: ");
        
        String num = archivo.nextLine();
        try{
            FileReader  r = new FileReader(num);
            BufferedReader reader = new BufferedReader(r);
            
            String contenido = reader.readLine();
                 String []vector = contenido.split(",");
                 int suma =0;
                System.out.println("Vector:" + Arrays.toString(vector));
                long tinicio=System.currentTimeMillis();
                for(int i = 0; i<vector.length;i++){         
                    suma+= Integer.parseInt(vector[i]);
                }
                
                long tfinal=System.currentTimeMillis();
                
                System.out.println("Suma = "+suma+ " tiempo: "+(tfinal-tinicio)); 
          
                
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
