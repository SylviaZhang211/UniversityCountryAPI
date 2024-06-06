package com.example.universitycountryapi.Controller;

import com.example.universitycountryapi.Model.University;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/universities")
public class UniversityController {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newFixedThreadPool(10);
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<List<University>> getAllUniversities() {
        String url = "http://universities.hipolabs.com/search";
        University[] universities = restTemplate.getForObject(url, University[].class);
        List<University> result = filterUniversityData(universities);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<List<University>> getUniversitiesByCountries(@RequestBody List<String> countries) {
        List<CompletableFuture<List<University>>> futures = new ArrayList<>();
        for (String country : countries) {
            futures.add(fetchUniversitiesByCountry(country));
        }
        List<University> result = new ArrayList<>();
        for (CompletableFuture<List<University>> future : futures) {
            result.addAll(future.join());
        }
        return ResponseEntity.ok(result);
    }

    @Async
    public CompletableFuture<List<University>> fetchUniversitiesByCountry(String country) {
        String url = "http://universities.hipolabs.com/search?country=" + country.replace(" ", "+");
        University[] universities = restTemplate.getForObject(url, University[].class);
        return CompletableFuture.completedFuture(filterUniversityData(universities));
    }

    private List<University> filterUniversityData(University[] universities) {
        List<University> result = new ArrayList<>();
        for (University university : universities) {
            University u = new University();
            u.setName(university.getName());
            u.setDomain(university.getDomain());
            u.setWeb_pages(university.getWeb_pages());
            result.add(u);
        }
        return result;
    }
}
