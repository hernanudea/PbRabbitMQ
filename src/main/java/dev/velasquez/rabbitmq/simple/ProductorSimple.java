package dev.velasquez.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProductorSimple {
    public static void main(String[] args) {

//        Channel channel = null;
        String message = "** Hola Mundo 4**";

        // Abrir conexión AMQ y Establecer canal
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // ConnectionFactory contiene una cantidad de metodos set y unos valores por defecto
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            // Channel es uno de los emtodos mas importantes del cliente,
            // aporta metodos para crear exchanges, colas, enviar y consumir mensajes

            // Crear Cola
            String queueName = "primera-cola";
            channel.queueDeclare(queueName, false, false, false, null);

            // Enviar Mensaje al Exchange
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
