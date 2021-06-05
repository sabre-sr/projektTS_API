package ts.projekt.API_Gateway;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class UserService {
    @GetMapping(path = "/users/{id}")
    public User getUser(@PathVariable int id) {
        WebClient clientUser = WebClient.builder()
                .baseUrl(String.format("http://localhost:8081/users/%d", id))
                .build();
        return clientUser.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<User>() {})
                .block();
    }
    @GetMapping(path = "/users")
    public User getUser(@RequestParam(name = "login") String login) {
        WebClient clientUser = WebClient.builder()
                .baseUrl("http://localhost:8081/users?login=" + login)
                .build();
        return clientUser.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<User>() {})
                .block();
    }

    @PostMapping(path = "register")
    public void register(AuthUser user) {

    }
}
