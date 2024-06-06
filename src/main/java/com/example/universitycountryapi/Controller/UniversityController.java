package com.example.universitycountryapi.Controller;

import com.example.universitycountryapi.Model.University;
import com.example.universitycountryapi.Service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @GetMapping
    public ResponseEntity<List<University>> getAllUniversities() {
        List<University> universities = universityService.getAllUniversities()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(universities);
    }

    @PostMapping
    public ResponseEntity<List<University>> getUniversitiesByCountries(@RequestBody List<String> countries) {
        List<University> universities = universityService.getUniversitiesByCountries(countries)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(universities);
    }


    private University mapToResponse(University university) {
        University response = new University();
        response.setName(university.getName());
        response.setDomains(university.getDomains());
        response.setWeb_pages(university.getWeb_pages());
        return response;
    }
}
