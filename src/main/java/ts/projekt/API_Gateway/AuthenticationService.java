package ts.projekt.API_Gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RestController
public class AuthenticationService {
    //test salt: F2 40 20 FA DE 76 39 02 4A D8 BB 0E B6 7E AC 22
    @GetMapping(path = "getsalt/{id}")
    public AuthUser getSalt(@PathVariable int id) {
        String uri = "http://localhost:8083/getsalt/" + id;
        WebClient salt = WebClient.builder()
                .baseUrl(uri)
                .build();
        return salt.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AuthUser>() {
                })
                .block();
    }
    @GetMapping(path = "getsalt")
    public AuthUser getSalt(@RequestParam(name = "login") String login) {
        WebClient clientUser = WebClient.builder()
                .baseUrl("http://localhost:8081/users?login=" + login)
                .build();
        User user = clientUser.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<User>() {})
                .block();
        assert user != null;
        return getSalt(user.getId());
    }

    @PostMapping(path = "login")
    public User login(@RequestBody AuthUser loginCred) throws IOException {
        ObjectMapper mapper =new ObjectMapper();
        String json = mapper.writeValueAsString(loginCred);
        URL url = new URL("http://localhost:8083/login");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response);
        }
        int response = con.getResponseCode();
        if (response == 200) {
            WebClient clientUser = WebClient.builder()
                    .baseUrl(String.format("http://localhost:8081/users/%d", loginCred.getId()))
                    .build();
            return clientUser.get()
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<User>() {})
                    .block();
        }
        return null;
    }

    @PostMapping(path = "register")
    public void register(@RequestBody AuthUser user) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8081/register")
                .build();
        User temp = webClient.post()
                .body(Mono.just(user), User.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<User>() {})
                .block();
        if (temp == null || temp.getId() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Couldn't create valid user in user database.");
        else {
            user.setId(temp.getId());
            webClient = WebClient.builder()
                    .baseUrl("http://localhost:8081/register")
                    .build();
            AuthUser temp2 = webClient.post()
                    .body(Mono.just(user), AuthUser.class)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<AuthUser>() {})
                    .block();
            if (temp2 == null || temp2.getId() == 0)
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Couldn't create valid user in auth database.");
        }
    }
}
