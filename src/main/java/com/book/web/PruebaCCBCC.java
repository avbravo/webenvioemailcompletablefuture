/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.book.web;

import com.avbravo.jmoordbutils.JsfUtil;
import com.avbravo.jmoordbutils.email.ManagerEmail;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Envia correos con copias (cc, bcc y varios destinatarios)
 *
 * @author avbravo
 */
@Named
@ViewScoped
public class PruebaCCBCC implements Serializable {

    String emailemisor = "";
    String passwordemailemisor = "";
    String[] to = {"unoo@gmail.com", "dos@gmail.com"}; // list of recipient email addresses
    String[] cc = {"tres@gmail.com"};
    String[] bcc = {"cuatro@gmail.com", "cinco@gmail.com"};

    public String getEmailemisor() {
        return emailemisor;
    }

    public void setEmailemisor(String emailemisor) {
        this.emailemisor = emailemisor;
    }

    public String getPasswordemailemisor() {
        return passwordemailemisor;
    }

    public void setPasswordemailemisor(String passwordemailemisor) {
        this.passwordemailemisor = passwordemailemisor;
    }

    public Integer getContadoremail() {
        return contadoremail;
    }

    public void setContadoremail(Integer contadoremail) {
        this.contadoremail = contadoremail;
    }

    
    
    
    
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

        Executors.newCachedThreadPool().submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("........ Voy a enviar el email");
                Date date = new Date();

                managerEmail.sendOutlook(to, cc, bcc, "{Async}:" + contadoremail, "prueba a " + date.toString(), emailemisor, passwordemailemisor);
                System.out.println(".......... Emails enviados con copias: " + Arrays.toString(to) + " cc " + Arrays.toString(cc) + " bcc " + Arrays.toString(bcc));
//              Thread.sleep(2500);
                completableFuture.complete("Hello");

                return null;
            }
        });

        return completableFuture;
    }
}
