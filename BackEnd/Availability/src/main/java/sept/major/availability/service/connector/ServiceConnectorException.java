package sept.major.availability.service.connector;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class ServiceConnectorException extends Exception {

    @Autowired
    private String service;

    public ServiceConnectorException(String service, String message) {
        super(message);
        this.service = service;
    }

    public Map<String, String> getJsonFormat() {
        Map<String, String> map = new HashMap<>();
        map.put("service", service);
        map.put("message", getMessage());
        return map;
    }


}
