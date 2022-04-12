package dev.velasquez.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class ProductorEventosDeportivos {

    private static final String EXCHANGE = "eventos-deportivos";


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {


        ConnectionFactory connectionFactory = new ConnectionFactory();

        // Abrirconexión AMQ y establecer canal
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            // Crear topic exchange "eventos-deportivos"
            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.TOPIC); //crea un exchange no durable

            // pais:es, fr, us, co
            List<String> countries = Arrays.asList("FR", "ES", "CO", "EU");

            //Deporte: Futbol, Tenis, Voleibol
            List<String> sports = Arrays.asList("Futbol", "Tenis", "Voleibol");

            //tipo evento: envivo, noticia
            List<String> eventTypes = Arrays.asList("envivo", "noticia");

            long count = 0;
            // Enviar mensaje al topic exchange "eventos-deportivos"
            while (true) {
                shuffles(countries, sports, eventTypes);
                String country = countries.get(0);
                String sport = sports.get(0);
                String eventType = eventTypes.get(0);
                // routingKey => country.sport.eventType
                String routingKey = String.valueOf(country + "." + sport + "." + eventType);

                String message = String.format("Evento " + ++count);
                System.out.println("Produciendo mensaje: " + String.valueOf(country + "." + sport + "." + eventType) + message);

                channel.basicPublish(EXCHANGE, routingKey, null, message.getBytes()); //routingKey es vaio para el tipo Fanout
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private static void shuffles(List<String> countries, List<String> sports, List<String> eventTypes) {
        Collections.shuffle(countries);
        Collections.shuffle(sports);
        Collections.shuffle(eventTypes);
    }
}
