package kr.co.polycube.backendtest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) 
@Transactional 
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class APITest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;
    private ObjectMapper objectMapper;

    Map<Long, String> users;
    Random rand;
    final int TRIALS = 5;

    @Autowired
    public APITest(TestRestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.users = new HashMap<Long, String>();
        rand = new Random();
    }

    @Test
    void testAPIFails() throws Exception { 
        String url_front = "http://localhost:" + port;

        //curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/users/1?id=5&description=test"
        ResponseEntity<String> response = restTemplate.exchange(url_front+"/users/1?id=5&description=test", HttpMethod.PUT, null, String.class);
        assertThat(response.getStatusCode().value()==400).isTrue(); 
        JsonNode body = objectMapper.readTree(response.getBody());
        assertThat(body.has("reason")).isTrue(); 

        //curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/users?name=test"
        response = restTemplate.exchange(url_front+"/users?name=test", HttpMethod.PUT, null, String.class);
        assertThat(response.getStatusCode().value()==404).isTrue(); 
        body = objectMapper.readTree(response.getBody());
        assertThat(body.has("reason")).isTrue(); 

        //curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/usears/1?name=test"
        response = restTemplate.exchange(url_front+"/usears/1?name=test", HttpMethod.PUT, null, String.class);
        assertThat(response.getStatusCode().value()==404).isTrue(); 
        body = objectMapper.readTree(response.getBody());
        assertThat(body.has("reason")).isTrue(); 

        //curl -X DELETE -H "Content-Type: application/json" "http://localhost:8080/users/1?name=test"
        response = restTemplate.exchange(url_front+"/users/1?name=test", HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCode().value()==404).isTrue(); 
        body = objectMapper.readTree(response.getBody());
        assertThat(body.has("reason")).isTrue(); 
    }
    
    @Test
    void testUserAPI() throws Exception {
        String url_front = "http://localhost:" + port + "/users";

        //curl -X POST -H "Content-Type: application/json" http://localhost:8080/users?name={name}
        for (int i = 0; i<TRIALS; i++) {
            int name = rand.nextInt(1000);
            ResponseEntity<String> response = restTemplate.exchange(url_front+"?name="+name, HttpMethod.POST, null, String.class);
            JsonNode user = objectMapper.readTree(response.getBody());
            assertThat(user.has("id")).isTrue(); 
            users.put(user.get("id").asLong(), name+"");
        }
        assertThat(users.size()==TRIALS).isTrue();

        //curl -X GET -H "Content-Type: application/json" http://localhost:8080/users/(id)
        for (Map.Entry<Long, String> e : users.entrySet()) {
            Long id = e.getKey();
            String name = e.getValue();
            ResponseEntity<String> response = restTemplate.exchange(url_front+"/"+id, HttpMethod.GET, null, String.class);
            JsonNode user = objectMapper.readTree(response.getBody());
            assertThat(user.has("id")).isTrue(); 
            assertThat(user.has("name")).isTrue();
            assertThat(id.equals(user.get("id").asLong())).isTrue();
            assertThat(name.equals(user.get("name").asText())).isTrue();
        }

        //curl -X PUT -H "Content-Type: application/json" http://localhost:8080/users/{id}?name={name)
        for (Map.Entry<Long, String> e : users.entrySet()) {
            Long id = e.getKey();
            String nameBefore = e.getValue();
            String nameAfter = nameBefore+"PUT";
            ResponseEntity<String> response = restTemplate.exchange(url_front+"/"+id+"?name="+nameAfter, HttpMethod.PUT, null, String.class);
            JsonNode user = objectMapper.readTree(response.getBody());
            assertThat(user.has("id")).isTrue(); 
            assertThat(user.has("name")).isTrue();
            assertThat(id.equals(user.get("id").asLong())).isTrue();
            assertThat(nameAfter.equals(user.get("name").asText())).isTrue();
        }

        //filter test
        //curl -X PUT -H "Content-Type: application/json" http://localhost:8080/users/{id}?name=test\!\!
        for (Map.Entry<Long, String> e : users.entrySet()) {
            Long id = e.getKey();
            ResponseEntity<String> response = restTemplate.exchange(url_front+"/"+id+"?name=test!!", HttpMethod.PUT, null, String.class);
            assertThat(response.getStatusCode().value()==400).isTrue();
        }
    }

    @Test
    void testLottoAPI() throws Exception {
    
        String url = "http://localhost:" + port + "/lottos";
        int digits = 6;

        //curl -X POST -H "Content-Type: application/json" http://localhost:8080/lottos
        for (int i = 0; i<TRIALS; i++) {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
            JsonNode lotto = objectMapper.readTree(response.getBody());
            assertThat(lotto.has("numbers")).isTrue(); 
            assertThat(lotto.get("numbers").isArray()).isTrue();
            JsonNode numbers = lotto.get("numbers");
            for (int j = 0; j<digits; j++) {
                assertThat(numbers.get(j).isInt()).isTrue();
            }
        }

    }
}
