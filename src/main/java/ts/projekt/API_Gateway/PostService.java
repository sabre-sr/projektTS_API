package ts.projekt.API_Gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class PostService {
    private final HttpServletRequest request;
    private final static Logger log = LoggerFactory.getLogger(PostService.class);
    public PostService(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping(path = "/posts")
    public ArrayList<Post> getAllPosts() {
        WebClient clientPosts = WebClient.builder()
                .baseUrl("http://localhost:8082/get-posts")
                .build();
        return getPostArray(clientPosts);
    }
    @PostMapping(path = "/addPost")
    public Post addPost(@RequestBody Post post) {
        WebClient postClient = WebClient.builder()
                .baseUrl("http://localhost:8082/addpost")
                .build();
        return postClient.post()
                .body(Mono.just(post), Post.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Post>() {})
                .block();
    }
    @GetMapping(path="replies/{id}")
    public ArrayList<Post> getReplies(@PathVariable int id) {
        WebClient clientPosts = WebClient.builder()
                .baseUrl("http://localhost:8082/replies/"+id)
                .build();
        return getPostArray(clientPosts);
    }

    private ArrayList<Post> getPostArray(WebClient clientPosts) {
        ArrayList<Post> response = clientPosts.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Post>>() {})
                .block();
        for (int i = 0; i< Objects.requireNonNull(response).size(); i++) {
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
    @PostMapping(path = "uploadFile")
    public Post uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        WebClient postClient = WebClient.builder()
                .baseUrl("http://localhost:8082/uploadFile")
                .build();
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        if (!file.isEmpty()) {
            String uploadsDir = "/temp/";
            String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
            if (!new File(realPathtoUploads).exists()) {
                new File(realPathtoUploads).mkdir();
            }
            String orgName = file.getOriginalFilename();
            String filePath = realPathtoUploads + orgName;
            File dest = new File(filePath);
            file.transferTo(dest);
            return postClient.post()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(fromFile(dest)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Post>() {})
                    .block();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
    }

    public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        return builder.build();
    }

    @GetMapping(path="getFile/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String name) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082/getFile/" + name)
                .build();
        return webClient.get()
                .accept(MediaType.IMAGE_JPEG)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
}
