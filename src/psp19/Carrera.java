/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp19;

import java.util.Random;
import java.util.concurrent.TimeUnit;

class Corredor extends Thread {

    private String nombre;

    private Corredor siguiente; //Siguiente corredor
    private int time; //Tiempo 
    private boolean correr;

    public Corredor(String name, boolean run) {
        super();
        this.nombre = name;
        this.correr = run;
        start();
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!correr) { //Mientras correr no esté a true, que espere. Al principio solo el corredor 1 está a true.
                    wait();
                }
            }

            Random r = new Random();
            int segs = 9 + r.nextInt(4); //Simulo los segundos que ha tardado en correr 100m. 9+4
            int milis = r.nextInt(1000); //Simulo los milisegundos.

            System.out.println(nombre + "：" + segs + "." + milis + " segundos");  //Los muestro por pantalla
            if (this.siguiente != null) {
                System.out.println("Comienza a correr " + this.siguiente.nombre); //Muestro quién es el siguiente corredor.  

                Thread.sleep(2000); //Esperso 2 segundos para pasar el testigo
            }
            siguiente(siguiente, segs, milis); //Paso el testigo al siguiente corredor 
        } catch (InterruptedException e) {
        }

    }

    private  void siguiente(Corredor siguiente, int time, int milis) {

        if (siguiente != null) { //Mientras exista un siguiente corredor (corredores 1, 2 y 3).

            siguiente.setTime(this.time + time * 1000 + milis); //Coloco en time el tiempo del corredor anterior, para el total del final.
           synchronized (siguiente) { //Paso testigo poniendo correr del siguiente corredor a true.
                siguiente.setCorrer(true);
                siguiente.notify(); //Se lo notifico.
            }
        } else { //corredor 4.
          
            
            double total = (double) (this.time + time * 1000 + milis) / 1000;
            System.out.println("Tiempo Total：" + total + " segundos"); //Muestro el tiempo total.
        }
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public void setSiguiente(Corredor next) {
        this.siguiente = next;
    }

    public void setCorrer(boolean run) {
        this.correr = run;
    }

}

public class Carrera {

    public static void main(String[] args) throws InterruptedException {
        final Corredor[] players = new Corredor[4];

        System.out.println("Comienza a correr el corredor1");
        Thread.sleep(2000);
        /*
         * Creo los cuatro corredores
         * Les asigno un nombre y al primero le digo que correr=true.
         * 
         */
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                players[i] = new Corredor("Corredor" + (i + 1), true);
            } else {
                players[i] = new Corredor("Corredor" + (i + 1), false);
            }
        }
        /*
         * A los tres primeros corredores les digo quién es el siguiente corredor
         * para pasar el testigo.
         */
        for (int i = 0; i < 1; i++) {
            players[i].setSiguiente(players[i + 1]);
        }

    }

}
