package kampia.esperLocation.EventTypes;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientCloseEvent    {
    private int clientID;
    private String latitude;
    private String longitude;
    private String eventType;
    private Long ticks;


    public ClientCloseEvent(){ticks=0L;}

    public ClientCloseEvent(Location loc ,Long ticks){
        this.latitude=loc.getLatitude();
        this.longitude=loc.getLongitude();
        this.eventType=loc.getEventType();
        this.clientID=loc.getClientID();
        this.ticks = ticks;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getTicks() {
        return ticks;
    }

    public void setTicks(Long ticks) {
        this.ticks = ticks;
    }
//    public ClientCloseEvent(int id , int sid, java.sql.Timestamp timestamp, Double lat , Double lon , int floor , int clientID){
//        this.ClientID=clientID;
//        this.LocationID=id;
//        this.SessionID=sid;
//        this.Timestamp=timestamp;
//        this.Lon=lon;
//        this.Lat=lat;
//        this.Floor=floor;
//    }

    public byte[] serialize(Object arg1) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public Object deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        Location temp = null;
        try {
            temp = mapper.readValue(arg1, Location.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }


}
