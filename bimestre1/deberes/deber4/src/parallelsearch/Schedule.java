/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelsearch;

/**
 *
 * @author David
 */
public class Schedule {
    
    static int x = 0;
static int y = 0;

public static int op1 (){x = 5; return y;}
public static int op2 (){y = 3; return 3*x;}

   public static void main(String[] args) {
       System.out.println(op1());
       System.out.println(op2());
   }
   
   //se puede notar que el resultado de la opcion 1 es 0  y de opcion 2 es 3
   //se aprecia que si cambiamos los valores solo la opcion2 se ve afectada

}
