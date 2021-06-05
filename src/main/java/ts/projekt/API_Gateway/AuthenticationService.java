package ts.projekt.API_Gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationService {
    @GetMapping(path = "getsalt/{id}")
    public byte[]
}
