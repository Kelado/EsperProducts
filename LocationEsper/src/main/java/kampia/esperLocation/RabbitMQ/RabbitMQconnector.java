package kampia.esperLocation.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.espertech.esper.runtime.client.EPRuntime;
import com.rabbitmq.client.*;
import kampia.esperLocation.EventTypes.Location;
import kampia.esperLocation.config.Configurations;
import kampia.esperLocation.config.CustomDeserializer;

import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class RabbitMQconnector {


    public static EPRuntime runtime;
    public static HashMap<Integer , ArrayList<Location>> ClientsLoc = new HashMap<>();

    private RabbitmqClient rabbitcli;
    public RabbitMQconnector(EPRuntime runtime){

        RabbitMQconnector.runtime =runtime;

        this.rabbitcli =new RabbitmqClient();
    }

    public void run() throws Exception {
       System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
       ObjectMapper mapper = new ObjectMapper();
       DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            Location loc = mapper.readValue(delivery.getBody(), Location.class);
            runtime.getEventService().sendEventBean(loc, "Location");
//            int clientId = l.getClientID();
//
//            if (ClientsLoc.containsKey(clientId)){
//                ClientsLoc.get(l.getClientID()).add(l);
//            }else {
//                ArrayList<Location> al = new ArrayList<>();
//                al.add(l);
//                ClientsLoc.put(clientId, al);
//            }
//
//           checkSizeAndSend(delivery);
       };

       this.rabbitcli.consume(Configurations.Reading_Queue_RabbitMQ, deliverCallback);
    }

    private void checkSizeAndSend(Delivery delivery) {
        ClientsLoc.forEach( (id, events) -> {
                    if (events.size()>=Configurations.No_Batch_Events) {
                       events.forEach( e -> {
                           runtime.getEventService().sendEventBean(e, delivery.getProperties().getContentType());
                       });
                       events.clear();
                    }
                });
    }

    public EPRuntime getRuntime() {
        return runtime;
    }
}
