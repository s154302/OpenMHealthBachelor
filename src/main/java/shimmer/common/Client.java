package shimmer.common;

import common.PropertiesLoad;
import org.apache.commons.codec.binary.Base64;

/**
 * Client class used for authorisation when getting an access token.
 */
public class Client {
    private final static String CLIENT_ID = PropertiesLoad.getProperty("clientUsername");
    private final static String CLIENT_SECRET = PropertiesLoad.getProperty("clientSecret");


    /**
     * @return the client id and secret encoded in base64
     */
    public static String getEncodedClientInfo(){
        return new String(Base64.encodeBase64String((CLIENT_ID + ":"+ CLIENT_SECRET).getBytes()));
    }
}
