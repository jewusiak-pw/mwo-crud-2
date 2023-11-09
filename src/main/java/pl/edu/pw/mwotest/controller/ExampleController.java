package pl.edu.pw.mwotest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helloworld")
public class ExampleController {

    @Value("${pl.edu.pw.mwotest.version:unknown}") String version;

    @GetMapping
    public ResponseEntity<String> getHelloWorld(@RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.ok("Hello " + (name.isBlank() ? "World" : name) + "! This is version " + version + " of the app.");
    }
}
