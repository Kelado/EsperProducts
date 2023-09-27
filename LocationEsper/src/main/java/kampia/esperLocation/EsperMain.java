package kampia.esperLocation;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPDeployment;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPStatement;
import com.rabbitmq.client.Channel;
import kampia.esperLocation.Data.CMSApiConnector;
import kampia.esperLocation.EventTypes.ClientInterested;
import kampia.esperLocation.EventTypes.ClientInterestedCat;
import kampia.esperLocation.EventTypes.Location;
import kampia.esperLocation.EventTypes.NotifObject;
import kampia.esperLocation.RabbitMQ.RabbitMQconnector;
import kampia.esperLocation.RabbitMQ.RabbitmqClient;
import kampia.esperLocation.Subscribers.*;
import kampia.esperLocation.config.EsperConfig;
import kampia.esperLocation.utils.EPLUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class EsperMain {

    public static  ArrayList<ClientInterested> ClientsInterests= new  ArrayList();
    public static ArrayList<ClientInterestedCat> ClientsInterestsCat= new ArrayList<>();

    public static CMSApiConnector connector = null;

    public static EPCompiled compiled;
    public static Configuration configuration;
    public static EPRuntime runtime;
    public static EPDeployment deployment;

    public void run() throws Exception {
        CMSApiConnector.InitCMSApiConnector();

        configuration = EsperConfig.getConfiguration();

        compiled = EPLUtil.loadQueries(configuration);

        runtime = EPRuntimeProvider.getRuntime(EsperConfig.RUNTIME_URI, configuration);

        deployment = EPLUtil.deploy(runtime, compiled);

        listenToSampleStatement(runtime, deployment);


        RabbitMQconnector RabbitConnector = new RabbitMQconnector(runtime);
        RabbitConnector.run();


    }


    private void listenToSampleStatement(EPRuntime runtime, EPDeployment deployment) {
        RabbitmqClient rabbitcli = new RabbitmqClient();

        EPStatement statement = runtime.getDeploymentService().getStatement(deployment.getDeploymentId(), "TimeWindow");
        statement.addListener((newData, oldData, sta, run) -> {
            Map<Integer, List<Location>> events = new HashMap<>();
            // Take all events within <window size> seconds
            // and separate events for each client
            for (EventBean event : newData) {
                int clientID = (int)event.get("clientID");

                Location loc = new Location();
                loc.setClientID((int)event.get("clientID"));
                loc.setLatitude((String)event.get("latitude"));
                loc.setLongitude((String)event.get("longitude"));
                loc.setEventType((String)event.get("eventType"));
                loc.setBeacon_name((String)event.get("beacon_name"));

                if (!events.containsKey(clientID)) {
                    events.put(clientID, new ArrayList<>());
                }
                events.get(clientID).add(loc);
            }

            // For each client, see if there
            // is IN and OUT from the same beacon
            for(Map.Entry<Integer,List<Location>> entry : events.entrySet()){

                // Separate events based on beacon's coordinates
                Map<String, List<String>> beacons = new HashMap<>();
                for(Location loc : entry.getValue()){
//                    String key = loc.getLatitude()+"_"+loc.getLongitude();
                    String key = loc.getBeacon_name();

                    if (!beacons.containsKey(key)) {
                        beacons.put(key, new ArrayList<>());
                    }
                    beacons.get(key).add(loc.getEventType());
                }

                // If we have IN but not out of a beacon
                // We can assume that the client is interested
                for(Map.Entry<String,List<String>> beacon : beacons.entrySet()){
                    if(beacon.getValue().contains("IN") || beacon.getValue().contains("STILL IN") && !beacon.getValue().contains("OUT")){
//                        Get product locations from CMS -> ProductCategoryId, ProductID
//                        Get client preferences from CMS
//                        Find ProductCategoryId where client is interested
//                        Choose one ProductId from this category
                        ArrayList<Integer> product_ids = CMSApiConnector.fetchProductsNearBeacon(beacon.getKey());

                        if(Objects.isNull(product_ids) || product_ids.size()==0){
                            System.out.println("0 items in this beacon ["+beacon.getKey()+"]");
                            return;
                        }

//                      Do something more sophisticated, see wishlist and/or previous purchases
                        int product_id = product_ids.get(0);
                        System.out.println("# Client: "+entry.getKey()+ " is interested for "+product_id);
                        rabbitcli.publish_out(new NotifObject(entry.getKey(),product_id,-1,""));
                    }
                }
                System.out.println("Nothing found in this time window!");
                System.out.println(beacons.entrySet());
            }
        });
    }
}
