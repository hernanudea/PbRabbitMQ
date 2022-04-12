package dev.velasquez.rabbitmq.events;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventos {

    private static final String EVENTOS = "eventos";


    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        // Abrir conexión
        Connection connection = connectionFactory.newConnection();

        // Establecer canal
        Channel channel = connection.createChannel();

        // Declarar exchange "eventos"
        channel.exchangeDeclare(EVENTOS, BuiltinExchangeType.FANOUT);

        //Crear cola y asocial al exchange "eventos"
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EVENTOS, "");

        // Crear subscripción a una cola asociada al exchange "eventos"
        channel.basicConsume(queueName,
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje Recibido: " + messageBody);
//                    System.out.println("Exchange: " + message.getEnvelope().getExchange());
//                    System.out.println("Routing key: " + message.getEnvelope().getRoutingKey());
//                    System.out.println("Delivery tag: " + message.getEnvelope().getDeliveryTag());
                },
                (consumerTag) -> {
                    System.out.println("Consumidor" + consumerTag + " cancelado.");
                });

    }
}
