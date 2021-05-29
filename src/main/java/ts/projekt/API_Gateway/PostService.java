package ts.projekt.API_Gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PostService {
    private static final ObjectMapper mapper = new ObjectMapper();


    @GetMapping(path = "/posts")
    public ArrayList<Post> getAllPosts() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082/get-posts")
                .build();
        ArrayList<Post> response = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Post>>() {})
                .block();
        return response;
//        Object[] objects = response.block();
//        assert objects != null;
//        return Arrays.stream(objects)
//                .map(object -> mapper.convertValue(object, Post.class))
//                .collect(Collectors.toList());
    }
}
