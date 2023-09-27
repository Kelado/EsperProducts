package kampia.esperLocation.EventTypes;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;

public class Location{
    private int clientID;
    private String latitude;
    private String longitude;
    private String eventType;

    private String beacon_name;

    public int getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
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

    public String getBeacon_name() {
        return beacon_name;
    }

    public void setBeacon_name(String beacon_name) {
        this.beacon_name = beacon_name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "clientID=" + clientID +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", eventType='" + eventType + '\'' +
                ", beacon_name='" + beacon_name + '\'' +
                '}';
    }
}
