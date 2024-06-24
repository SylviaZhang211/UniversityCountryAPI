package com.example.universitycountryapi.service.impl;


import com.example.universitycountryapi.model.University;
import com.example.universitycountryapi.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class UniversityServiceImpl implements UniversityService {

    private final RestTemplate restTemplate;
    private final ExecutorService pool;

    //private final ExecutorService pool;

    @Value("${university-url}")
    private String baseUrl;

    @Autowired
    public UniversityServiceImpl(RestTemplate restTemplate, ExecutorService pool) {
        this.restTemplate = restTemplate;
        this.pool = pool;
    }

    @Override
    public List<University> getAllUniversities() {
        University[] universities = restTemplate.getForObject(baseUrl, University[].class);
        return Arrays.asList(universities);
    }

//    @Override
//    public CompletableFuture<List<University>> getUniversitiesByCountry(String country) {
//        return CompletableFuture.supplyAsync(() -> {
//            String url = baseUrl + "?country=" + country.replace(" ", "+");
//            University[] universities = restTemplate.getForObject(url, University[].class);
//            return Arrays.asList(universities);
//        }, pool);
//    }

    @Override
    public List<University> getByCountries(List<String> countries) {
        List<CompletableFuture<University[]>> futures = new ArrayList<>();
        countries.forEach(c ->
                futures.add(
                        CompletableFuture.supplyAsync(() -> restTemplate.getForObject(baseUrl + "?country=" + c, University[].class), pool)
                )
        );
        List<University> res = new ArrayList<>();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept(Void ->
                        futures.forEach(cf -> {
                            University[] data = cf.join();
                            for(University u: cf.join()) {
                                res.add(u);
                            }
                        })
                ).join();
        return res;
    }
}

