package kampia.esperLocation.config;

public class Configurations {

    public static boolean Local = true;

    public static String RabbitMQHost = "proximiotmq";

    public static String RabbitMQVirtualHost = "proximiot";
    public static String RabbitMQUser = "proximiot_user";
    public static String RabbitMQPass = "super_secret_password";
    public static String Reading_Queue_RabbitMQ = "esper_in";
    public static String Output_Queue_RabbitMQ = "recommendation_in";

    public static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    public static final String DB_URL_Prox = "jdbc:mariadb://db/proximiot_db";
    public static final String DB_URL_CMS = "jdbc:mariadb://db/CMS";
    public static final String USER = "proximiot_user";
    public static final String PASS = "super_secret_db_password";

    public static final String APIUsername = "cms_test";
    public static final String APIPassword = "LdZW75%Txh@v+@q$";

    public static String EPLQueries_path = "/queries/queries_new.epl";

    // Number of events for a client in order to be sent to Esper
    public static int No_Batch_Events = 10;

}
