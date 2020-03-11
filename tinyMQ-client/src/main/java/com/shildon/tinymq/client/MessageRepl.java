package com.shildon.tinymq.client;

import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;

import java.util.Scanner;

/**
 * @author shildon
 */
public final class MessageRepl {

    public static void main(String[] args) {
        Serializer serializer = new ProtostuffSerializer();
        MessageClient messageClient = MessageClient.getInstance();
        Publisher<String> publisher = new Publisher<>(serializer);
        Subscriber<String> subscriber = Subscriber.create(serializer);
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (!"exit".equals(line)) {
            try {
                line = scanner.nextLine();
                if ("publish".equals(line)) {
                    publisher.publish("test", "melody");
                } else if ("subscribe".equals(line)) {
                    subscriber.subscribe("test", message -> {
                        System.out.println("---------> " + message);
                    });
                } else if ("close".equals(line)) {
                    messageClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
