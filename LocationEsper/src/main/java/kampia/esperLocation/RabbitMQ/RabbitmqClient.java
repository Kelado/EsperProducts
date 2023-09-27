package kampia.esperLocation.RabbitMQ;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import kampia.esperLocation.EventTypes.NotifObject;
import kampia.esperLocation.config.Configurations;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class RabbitmqClient {
    private Channel channel= null;
    private Set<String> queues=new HashSet<String>();

    public RabbitmqClient(){
        this.channel = this.getChannel();
        this.declareQueues();
    }

    public void publish_out(NotifObject outMsg){
        String name = Configurations.Output_Queue_RabbitMQ;
        AMQP.BasicProperties messageProperties = new AMQP.BasicProperties.Builder()
                .contentType("NotObject")
                .build();

        try {
            this.channel.basicPublish("", name, messageProperties, outMsg.serialize(outMsg));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void consume(String name, DeliverCallback callback){
        try {
            this.channel.basicConsume(Configurations.Reading_Queue_RabbitMQ, true, callback, consumerTag -> { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Channel getChannel(){

        if(this.channel!=null) return this.channel;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Configurations.RabbitMQHost);
        factory.setVirtualHost(Configurations.RabbitMQVirtualHost);
        factory.setUsername(Configurations.RabbitMQUser);
        factory.setPassword(Configurations.RabbitMQPass);

        try {
            return factory.newConnection().createChannel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public void declareQueues(){
//        try {
//            this.channel.queueDeclare(Configurations.Reading_Queue_RabbitMQ,false,false,false,null);
//            this.channel.queueDeclare(Configurations.Output_Queue_RabbitMQ,false,false,false,null);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
