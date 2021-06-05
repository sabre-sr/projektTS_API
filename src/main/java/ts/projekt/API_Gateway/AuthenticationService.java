package ts.projekt.API_Gateway;

import net.minidev.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
    public User login(AuthUser loginCred) {
        WebClient authUrl = WebClient.builder()
                .baseUrl("http://localhost:8081/login")
                .build();
        return authUrl.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(loginCred), AuthUser.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<User>() {})
                .block();
    }
}
