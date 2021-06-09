package ts.projekt.API_Gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PostService {
    @GetMapping(path = "/posts")
    public ArrayList<Post> getAllPosts() {
        WebClient clientPosts = WebClient.builder()
                .baseUrl("http://localhost:8082/get-posts")
                .build();
        ArrayList<Post> response = clientPosts.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Post>>() {})
                .block();
        for (int i=0; i<response.size(); i++) {
            WebClient clientUser = WebClient.builder()
                    .baseUrl(String.format("http://localhost:8081/users/%d", response.get(i).author.getId()))
                    .build();
            User temp = clientUser.get()
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<User>() {})
                    .block();
            response.get(i).setAuthor(temp);
        }
        return response;
    }
    @PostMapping(path = "/addPost")
    public Post addPost(@RequestBody Post post) {
        WebClient postClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
        return postClient.post()
                .body(Mono.just(post), Post.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Post>() {})
                .block();
    }

}
