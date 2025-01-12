package kampia.esperLocation.Subscribers;

import com.espertech.esper.common.client.EventBean;
import kampia.esperLocation.EsperMain;
import kampia.esperLocation.EventTypes.ClientInterested;
import kampia.esperLocation.EventTypes.NotifObject;
import kampia.esperLocation.RabbitMQ.RabbitMQconnector;
import kampia.esperLocation.RabbitMQ.RabbitmqClient;

public class RelProdSubscriber implements StatementSubscriber{
    private RabbitmqClient rabbitcli;
    public RelProdSubscriber(){
        this.rabbitcli = new RabbitmqClient();
    }

    @Override
    public String output(EventBean event){

        StringBuilder sb= new StringBuilder();

        Object[] tmp = (Object[]) event.get("assoc");
        ClientInterested intre = new ClientInterested((int)tmp[1],(int)tmp[0], (double) tmp[4],(int)tmp[2]);
        EsperMain.ClientsInterests.add(intre);

        sb.append("------------****------------      ");
        sb.append("Client with sessionID "+tmp[1]+" is interested for product with id :"+tmp[0] + " relevance -> "+tmp[3]  );
        sb.append("       ------------****------------");

        System.out.println(sb);
        RabbitMQconnector.runtime.getEventService().sendEventBean(intre, "ClientInterested");

        NotifObject not= new NotifObject((int)tmp[1],(int)tmp[0],(int)tmp[3],(String)tmp[4]);
        this.rabbitcli.publish_out(not);

        return "";
    }


}
