package sept.major.availability.service.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import sept.major.common.testing.RequestParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public abstract class ServiceConnector<E> {

    @Autowired
    RestTemplate restTemplate;

    protected abstract String getServiceEndpoint();

    protected abstract String getSecondUsernameLabel();

    protected abstract String getServiceName();

    public List<E> getAll(String token, String requestUsername, String workerUsername, String secondUsername) throws ServiceConnectorException {
        List<RequestParameter> requestParameters = getUsernameParameters(workerUsername, secondUsername);

        String url = addRequestParameters(getServiceEndpoint() + "/all", requestParameters);

        return getResponses(token, requestUsername, url);
    }

    public List<E> getRange(String token, String requestUsername, String startDateTime, String endDateTime, String workerUsername, String secondUsername) throws ServiceConnectorException {
        List<RequestParameter> requestParameters = getUsernameParameters(workerUsername, secondUsername);
        requestParameters.add(new RequestParameter("startDateTime", startDateTime));
        requestParameters.add(new RequestParameter("endDateTime", endDateTime));


        String url = addRequestParameters(getServiceEndpoint() + "/range", requestParameters);
        return getResponses(token, requestUsername, url);
    }

    public List<E> getDate(String token, String requestUsername, String date, String workerUsername, String secondUsername) throws ServiceConnectorException {
        List<RequestParameter> requestParameters = getUsernameParameters(workerUsername, secondUsername);
        requestParameters.add(new RequestParameter("date", date));

        String url = addRequestParameters(getServiceEndpoint() + "/date", requestParameters);
        return getResponses(token, requestUsername, url);
    }

    private List<RequestParameter> getUsernameParameters(String workerUsername, String secondUsername) {
        List<RequestParameter> requestParameters = new ArrayList<>();
        if (workerUsername != null) {
            requestParameters.add(new RequestParameter("workerUsername", workerUsername));
        }
        if (secondUsername != null) {
            requestParameters.add(new RequestParameter(getSecondUsernameLabel(), secondUsername));
        }
        return requestParameters;
    }

    private List<E> getResponses(String token, String requesterUsername, String url) throws ServiceConnectorException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            headers.set("username", requesterUsername);
            return convertResultToList(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), List.class).getBody());
        } catch (ResourceAccessException e) {
            throw new ServiceConnectorException(getServiceName(), "connection to service failed, used URL: " + url);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                return new ArrayList<>();
            } else {
                throw new ServiceConnectorException(getServiceName(), e.getMessage());
            }
        }
    }

    /**
     * Adds the provided requestParameters to the provided url in the format needed for API requests.
     * For example: <url>?hoursId=1&workerUsername=bob
     *
     * @param url               The url to add request parameters to
     * @param requestParameters The request parameters to add to the url
     * @return The url with the request parameters added
     */
    protected String addRequestParameters(String url, List<RequestParameter> requestParameters) {
        if (requestParameters != null && !requestParameters.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(url);
            for (int i = 0; i < requestParameters.size(); i++) {
                if (i == 0) {
                    stringBuilder.append("?");
                } else {
                    stringBuilder.append("&");
                }
                stringBuilder.append(requestParameters.get(i));
            }

            return stringBuilder.toString();
        }

        return url;
    }


    protected abstract List<E> convertResultToList(List<Map> mapList);
}
