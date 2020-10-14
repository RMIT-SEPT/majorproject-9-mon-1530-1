package sept.major.availability.service.connector;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception class for microservice exceptions related to connectors.
 */
public class ServiceConnectorException extends Exception {

    private String service;
    private JsonNode response;

    public ServiceConnectorException(String service, String message) {
        super(message);
        this.service = service;
    }

    public ServiceConnectorException(String service, JsonNode response) {
        super("Error when connecting to " + service + " service");
        this.service = service;
        this.response = response;
    }

    public Map<String, Object> getJsonFormat() {
        Map<String, Object> map = new HashMap<>();
        map.put("service", service);
        if (response != null) {
            map.put("message", response);
        } else {
            map.put("message", getMessage());
        }

        return map;
    }


}
