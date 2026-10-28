package br.org.edu.ifrn.LojaCarro.mensageria;

import br.org.edu.ifrn.LojaCarro.dto.CarroMensagemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Enviar {

    // Instância do Jackson para converter o Objeto em JSON
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void enviarMensagem(CarroMensagemDTO carroMensagem, String queue) {
        ConnectionFactory factory = new ConnectionFactory();

        try {
            // Configuração explícita de IP e Porta
            factory.setHost("163.176.90.156");
            factory.setPort(5672);

            // Credenciais corrigidas de "quest" para "guest"
            factory.setUsername("guest");
            factory.setPassword("guest");
        } catch (Exception e) {
            System.out.println("Erro na configuração: " + e.getMessage());
            e.printStackTrace();
        }

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(queue, true, false, false, null);

            // Converte o DTO para uma String JSON
            String jsonMensagem = objectMapper.writeValueAsString(carroMensagem);

            channel.basicPublish("", queue, null, jsonMensagem.getBytes(StandardCharsets.UTF_8));
            System.out.println("MENSAGEM ENVIADA = '" + jsonMensagem + "'");
        } catch (Exception e) {
            System.out.println("Erro ao enviar mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}