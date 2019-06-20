/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.book.web;

import com.avbravo.jmoordbutils.JsfUtil;
import com.avbravo.jmoordbutils.email.ManagerEmail;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author avbravo
 */
@Named
@ViewScoped
public class Prueba implements Serializable {

    ManagerEmail managerEmail = new ManagerEmail();
  
    Integer contadoremail = 0;

  

    public void enviar() {
        try {
            long start = System.currentTimeMillis();
            JsfUtil.successMessage("voy a enviar " + start);

            Future<String> completableFuture = calculateAsync();
            System.out.println("... mientras se envia, ejecuta las acciones siguientes");

            /*
            Si se utiliza el esperara que termine.
            Cuando no se usa el completableFuture.get(); el programa sigue su curso
            y queda en un hilo el envio de emails
           
             */
            // String result = completableFuture.get();
            long end = System.currentTimeMillis();

            System.out.println("Tiempo estimado: " + (end - start));
        } catch (Exception e) {
            System.out.println("Error " + e.getLocalizedMessage());
        }
    }

    public Future<String> calculateAsync() throws InterruptedException {
        System.out.println("---->calculateAsync()");
        contadoremail++;
        CompletableFuture<String> completableFuture
                = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            System.out.println("........ Voy a enviar el email");
            Date date = new Date();
            managerEmail.sendOutlook("avbravo@gmail.com", "{Async}:" + contadoremail, "prueba a " + date.toString(), "aristides.villarreal@utp.ac.pa", "Controljav180den");
            System.out.println(".......... Email enviado");
//              Thread.sleep(2500);
            completableFuture.complete("Hello"); 

            return null;
        });

        return completableFuture;
    }
}
