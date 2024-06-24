package com.example.universitycountryapi.service;

import com.example.universitycountryapi.model.University;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UniversityService {
    List<University> getAllUniversities();
    List<University> getByCountries(List<String> countries);
    //List<University> getUniversitiesByCountries(List<String> countries);
}
