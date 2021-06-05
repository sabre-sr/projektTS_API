package ts.projekt.API_Gateway;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

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
}
