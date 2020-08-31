package uk.co.danielbryant.djshopping.shopfront;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping ("/api")
public class Controller {

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/hello")
    public String index1(){
        return "hello";
    }
}
