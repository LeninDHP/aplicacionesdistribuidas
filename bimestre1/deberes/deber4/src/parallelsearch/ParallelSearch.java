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
import java.util.Arrays;
import java.util.Scanner;

public class ParallelSearch {
    private static int index = -1;

    public static int parallelSearch(int x, int[] A, int numThreads) {
        // partition
        int divisionTheads = A.length / numThreads;//segun la cantidad de arboles divide el vector
        Thread[] threads = new Thread[numThreads];
        int topeArbol = 0;
        int inicioArbol = 0;
        // search
          
        for (int i = 0; i < numThreads; i++) {//determina el tope de cada arbol
            topeArbol = inicioArbol + divisionTheads;
            if (i == numThreads - 1 || topeArbol > A.length) {
                topeArbol = A.length;
            }
            Search target = new Search(inicioArbol, topeArbol, A, x);
            System.out.println(target); //imprime el inicio, tope y el numero a buscar
            threads[i] = new Thread(target);
            threads[i].start();//invocan un nuevo hilo
            System.out.println(threads[i].getName());//imprime el numero del arbol
            inicioArbol = topeArbol;    
        } 
        return index;
    }
    
    public static void main(String[] args) {
        int v[];
        Scanner vector = new Scanner(System.in);
        System.out.println("Ingrese el tamaño del vector:");
        int num = vector.nextInt();
        v=new int[num];
        System.out.println("Ingrese los numeros del vector, despues de cada numero presionar enter");
        for(int i = 0; i<v.length;i++){
            v[i]=vector.nextInt();
        }
        System.out.println("Vector "+Arrays.toString(v));
        Scanner read = new Scanner(System.in);
        System.out.println("Ingrese el numero que desea buscar: ");
        int x =  Integer.parseInt(read.nextLine());
        System.out.println("Ingrese la cantidad de arboles: ");
        int y =  Integer.parseInt(read.nextLine());
        int parallelSearch = parallelSearch(x, v, y);
        if (-1 == parallelSearch) {
            System.out.println("Encontrado en el ultimo arbol");
        } else {
            System.out.println("Econtrado en el indice: [" + parallelSearch+"]");
        }
    }

    private static class Search implements Runnable {
        //Atributos
        int inicioArbol;
        int topeArbol;
        int[] A;
        int x;

        public Search(int _inicioArbol, int _finalArbol, int[] _A, int _x) {
            super();//invoca al contructor de la super clase
            if (_finalArbol < _inicioArbol) {
                throw new IllegalStateException();
            }
            this.inicioArbol = _inicioArbol;
            this.topeArbol = _finalArbol;
            this.A = _A;
            this.x = _x;
        }
        //aqui se ejecula el hilo
        public void run() {
            for (int i = inicioArbol; i < topeArbol; i++) {
                if (x == A[i]) {//realiza un busqueda caracter por caracter
                    index = i;
                     System.out.println("Encontrado en el Arbol ("+ Thread.currentThread().getName()+")");
                   
                }
            }
        }
        
        public String toString() {
            
            return "\nInicio arbol=[" + inicioArbol + "], Tope arbol=[" + topeArbol + "], x=" + x + " ";
        }

    }
}