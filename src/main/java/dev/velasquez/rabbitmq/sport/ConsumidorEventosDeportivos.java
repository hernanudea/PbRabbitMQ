package dev.velasquez.rabbitmq.sport;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ConsumidorEventosDeportivos {

    private static final String EXCHANGE = "eventos-deportivos";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        // Abrir conexión
        Connection connection = connectionFactory.newConnection();

        // Establecer canal
        Channel channel = connection.createChannel();

        // Declarar exchange "eventos-deportivos"
        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC);

        //Crear cola y asocial al exchange "eventos-deportivos"
        String queueName = channel.queueDeclare().getQueue();

        // Patron routingKey => country.sport.eventTypy
        // * => identifica una palabra
        // # => identifica varias palabras delimitadas por .
        //Example: Tenis de cualquier pais y tipo: *.Tenis.*
        // Todos de un pais              : CO.*.*, CO.#
        // Todos los eventos        #, *.*.*

        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduzca el Routing Key");
        String routingKey =scanner.nextLine();
        channel.queueBind(queueName, EXCHANGE, routingKey);

        // Crear subscripción a una cola asociada al exchange "eventos-deportivos"
        channel.basicConsume(queueName,
                true,
                (consumerTag, message) -> {
                    String messageBody = new String(message.getBody(), Charset.defaultCharset());
                    System.out.println("Mensaje Recibido: " + messageBody);
                    System.out.println("Routing Key: " + message.getEnvelope().getRoutingKey());
                },
                (consumerTag) -> {
                    System.out.println("Consumidor" + consumerTag + " cancelado.");
                });

    }
}
