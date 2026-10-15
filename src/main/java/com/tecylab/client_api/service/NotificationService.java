package com.tecylab.client_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Async
    public CompletableFuture<Void> sendEmail(String to, String body) {
        var hilo = Thread.currentThread();
        log.info("[{}] virtual={} Enviando correo a {} ...", hilo.getName(), hilo.isVirtual(), to);

        simulateSlowServer(2000); // aquí iría emailClient.send(to, body)

        log.info("[{}] virtual={} Correo enviado a {}: {}", hilo.getName(), hilo.isVirtual(), to, body);
        return CompletableFuture.completedFuture(null);
    }

    private void simulateSlowServer(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
