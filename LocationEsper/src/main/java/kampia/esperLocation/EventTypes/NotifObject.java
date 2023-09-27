package kampia.esperLocation.EventTypes;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NotifObject {

    private int clientID;
    private int productID;
    private int productCategoryID;

    private String beacon_name;



    public NotifObject(int clientID,int productID,int productCategoryID, String beacon_name){
        this.clientID=clientID;
        this.productID=productID;
        this.productCategoryID=productCategoryID;
        this.beacon_name=beacon_name;
    }

    public int getClientID() {
        return clientID;
    }

    public int getProductCategoryID() {
        return productCategoryID;
    }

    public int getProductID() {
        return productID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setProductCategoryID(int productCategoryID) {
        this.productCategoryID = productCategoryID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }


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

    public Object deserialize( byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        NotifObject notif = null;
        try {
            notif = mapper.readValue(data, NotifObject.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  notif;


    }


}
