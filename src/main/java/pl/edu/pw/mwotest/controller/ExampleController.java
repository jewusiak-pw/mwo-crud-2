package pl.edu.pw.mwotest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/helloworld")
public class ExampleController {

    @GetMapping
    public ResponseEntity<String> getHelloWorld(@RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.ok("Hello " + (name.isBlank() ? "World" : name) + "!");
    }
}
