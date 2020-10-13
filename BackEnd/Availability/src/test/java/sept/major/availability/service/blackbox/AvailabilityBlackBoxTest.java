package sept.major.availability.service.blackbox;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sept.major.availability.service.RequestParameter;
import sept.major.availability.service.blackbox.mock.MockServices;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Component
public abstract class AvailabilityBlackBoxTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int servicePort;

    @BeforeAll
    public static void setUpMocks() {
        MockServices.startUpServer();
    }

    @AfterAll
    public static void closeMocks() {
        MockServices.stopServer();
        ;
    }


    public String getUrl() {
        return String.format("http://localhost:%s/availability", this.servicePort);
    }

    public String getUrl(List<RequestParameter> requestParameters) {
        return this.addRequestParameters(getUrl(), requestParameters);
    }

    public String getUrl(String urlExtension, List<RequestParameter> requestParameters) {
        return this.addRequestParameters(getUrl(urlExtension), requestParameters);
    }

    public String addRequestParameters(String url, List<RequestParameter> requestParameters) {
        if (requestParameters != null && !requestParameters.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(url);

            for (int i = 0; i < requestParameters.size(); ++i) {
                if (i == 0) {
                    stringBuilder.append("?");
                } else {
                    stringBuilder.append("&");
                }

                stringBuilder.append(requestParameters.get(i));
            }

            return stringBuilder.toString();
        } else {
            return url;
        }
    }

    public String getUrl(String urlExtension) {
        return String.format("%s/%s", getUrl(), urlExtension);
    }


    public ResponseEntity<String> getRequest(String url) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                getHeaders(),
                String.class
        );
    }

    public HttpEntity getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "foo");
        httpHeaders.set("username", "bar");
        return new HttpEntity(httpHeaders);
    }
}
