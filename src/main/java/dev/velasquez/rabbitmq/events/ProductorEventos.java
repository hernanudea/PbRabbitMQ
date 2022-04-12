package dev.velasquez.rabbitmq.events;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProductorEventos {

    private static final String EVENTO = "eventos-deportivos";


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {


        ConnectionFactory connectionFactory = new ConnectionFactory();

        // Abrirconexión AMQ y establecer canal
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            // Crear topic exchange "eventos-deportivos"
            channel.exchangeDeclare(EVENTO, BuiltinExchangeType.TOPIC); //crea un exchange no durable



            long count = 0;
            // Enviar mensaje al topic exchange "eventos-deportivos"
            while (true) {
                String message = String.format("Evento " + ++count);
                System.out.println("Produciendo mensaje: " + message);
                channel.basicPublish(EVENTO, "", null, message.getBytes()); //routingKey es vaio para el tipo Fanout
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
