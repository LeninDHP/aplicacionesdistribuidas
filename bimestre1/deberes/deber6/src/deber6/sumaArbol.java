/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deber6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.Buffer;
import static java.sql.JDBCType.NULL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author David
 */
public class sumaArbol extends Thread{
   
    String v[];
    int inicio,fin; long result;
    
    public sumaArbol(String []v, int inicio, int fin){
    this.v = v;
    this.inicio =inicio;
    this.fin = fin;
    
}
    
    

    //Tarea que ejecutaran los threads
    public void run() {
        int sum = 0;
        
        for (int i=inicio; i<fin; i++){
                sum = sum + Integer.parseInt(v[i]);
                
        } 
        try {
           
        System.out.println("Suma = "+sum);
        if(fin==0){
        sumaArbol hilo3 = new sumaArbol(v, (v.length/2),v.length); 
        hilo3.start();
        
        sumaArbol hilo2 = new sumaArbol(v, 0,(v.length/2)); 
        hilo2.start();
        sumaArbol total = new sumaArbol(v, 0, v.length);
        total.start();
        total.sleep(1);
        }   
        
        } catch (Exception e) {
        }
  
         
          
    }  

   public long getResult(){
        return result;
    }
   
   public synchronized void sumaMatriz(long sum) {
        this.result = this.result + sum;

    }

     public static void main(String[] args) throws InterruptedException {
      
        int tamano =0;
        Scanner archivo = new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo: ");
        
        String num = archivo.nextLine();
        
       
        try{
            FileReader  r = new FileReader(num);
            BufferedReader reader = new BufferedReader(r);
            
                String contenido = reader.readLine();
                String []vector = contenido.split(",");
                
                
                System.out.println("Vector:" + Arrays.toString(vector));
                
                
  
                long tinicio=System.currentTimeMillis();
                sumaArbol hilo2 = new sumaArbol(vector, 0,0); 
                hilo2.start();
                long tfinal=System.currentTimeMillis();
                System.out.println("tiempo: "+(tfinal-tinicio));
               
        }catch(Exception e){
            System.out.println(e);
        }
    }
     
}
