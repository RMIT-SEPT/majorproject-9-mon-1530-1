package sept.major.hours.blackbox;

import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application-test.properties")
public class PostBlackBoxTest extends HoursBlackBoxHelper {
//
//    @Autowired
//    protected TestRestTemplate testRestTemplate;
//
//    @Test
//    public void simpleGet() {
//        Map<String, String> postValues = new HashMap() {{
//            put("customerUsername", randomAlphanumericString(20));
//            put("workerUsername", randomAlphanumericString(20));
//            put("startDateTime", Timestamp.valueOf(LocalDateTime.now()).toString());
//            put("endDateTime", Timestamp.valueOf(LocalDateTime.now()).toString());
//        }};
//
//        ResponseEntity<String> postResult = testRestTemplate.postForEntity(getUrl(), postValues, String.class);
//        System.out.println(postResult);
//        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl("all"), String.class);
//
//        assertThat(getResult.getBody()).isEqualTo(Arrays.asList(postResult.getBody()).toString());
//        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
////        ResponseEntity<String> result = testRestTemplate.getForEntity(getUrl("all"), String.class);
////        assertThat(result.getBody()).isEqualTo("No records within provided bounds were found");
////        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    public void postAndGet() {
//        Map<String, String> postValues = new HashMap() {{
//            put("customerUsername", randomAlphanumericString(20));
//            put("workerUsername", randomAlphanumericString(20));
//            put("startDateTime", Timestamp.valueOf(LocalDateTime.now()).toString());
//            put("endDateTime", Timestamp.valueOf(LocalDateTime.now()).toString());
//        }};
//
//        ResponseEntity<String> postResult = testRestTemplate.postForEntity(getUrl(), postValues, String.class);
//        System.out.println(postResult);
//        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl("all"), String.class);
//
//        assertThat(getResult.getBody()).isEqualTo(Arrays.asList(postResult.getBody()).toString());
//        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
//    }

}
