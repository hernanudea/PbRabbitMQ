package dev.velasquez.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

public class ConsumerSimple {
    public static void main(String[] args) throws IOException, TimeoutException {

        // Abrir Conexión
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();

        // Establecer Canal
        Channel channel = connection.createChannel();

        // Declarar la cola "primera-cola"
        String queueName = "primera-cola";
        channel.queueDeclare(queueName, false, false, false, null);
        // si la cola no existe la crea
        // si la cola existe, no la puede modificar, por lo cual fallará

        // Crear Subscripción a la cola "primera-cola" usando el comando Basic.Consume
        channel.basicConsume(queueName,
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje: " + messageBody);
                    System.out.println("Exchange: " + message.getEnvelope().getExchange());
                    System.out.println("Routing key: " + message.getEnvelope().getRoutingKey());
                    System.out.println("Delivery tag: " + message.getEnvelope().getDeliveryTag());
                },
                (consumerTag) -> {
                    System.out.println("Consumidor" + consumerTag + " cancelado.");
                });

    }
}
