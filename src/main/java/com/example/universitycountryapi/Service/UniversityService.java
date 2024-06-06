package com.example.universitycountryapi.Service;

import com.example.universitycountryapi.Model.University;
import com.example.universitycountryapi.Exception.NoUniversityFoundException;
import com.example.universitycountryapi.Exception.NoCountryFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public List<University> getAllUniversities() {
        String url = "http://universities.hipolabs.com/search";
        University[] universities = restTemplate.getForObject(url, University[].class);
        if (universities == null || universities.length == 0) {
            throw new NoUniversityFoundException("No universities found");
        }
        return Arrays.asList(universities);
    }

    public CompletableFuture<List<University>> getUniversitiesByCountry(String country) {
        return CompletableFuture.supplyAsync(() -> {

            try {
                String url = "http://universities.hipolabs.com/search?country=" + country.replace(" ","+");
                University[] universities = restTemplate.getForObject(url, University[].class);
                if (universities == null || universities.length == 0) {
                    throw new NoUniversityFoundException("No universities found for country: " + country);
                }
                return Arrays.asList(universities);
            } catch (HttpClientErrorException e) {
                throw new NoCountryFoundException("No such country found: " + country);
            } catch (RestClientException e) {
                throw new RuntimeException("An unexpected error occurred", e);
            }
        }, executorService);
    }

public List<University> getUniversitiesByCountries(List<String> countries) {
    List<CompletableFuture<List<University>>> futures = countries.stream()
            .map(country -> getUniversitiesByCountry(country).handle((result, ex) -> {
                if (ex != null) {

                    throw new NoCountryFoundException("No country found for: " + country);
                }
                return result;
            }))
            .collect(Collectors.toList());

    return futures.stream()
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .collect(Collectors.toList());
}
}
