package kampia.esperLocation.Subscribers;

import com.espertech.esper.common.client.EventBean;
import kampia.esperLocation.EventTypes.ClientCloseEvent;
import kampia.esperLocation.EventTypes.Location;
import kampia.esperLocation.RabbitMQ.RabbitMQconnector;


public class CloseNtimesEventSubscriber  implements  StatementSubscriber{


    @Override
    public String output(EventBean event) {

        StringBuilder sb = new StringBuilder();

        Location loc1 = (Location) event.get("loc1");
        Long notimes = (Long) event.get("idcount");
        ClientCloseEvent closeEvent = new ClientCloseEvent(loc1,notimes);

        sb.append("------------****------------      ");

        sb.append("       ------------****------------");

        System.out.println(sb);
        RabbitMQconnector.runtime.getEventService().sendEventBean(closeEvent, "ClientCloseEvent");
        return sb.toString();
    }
}
