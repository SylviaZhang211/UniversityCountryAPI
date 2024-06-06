package com.example.universitycountryapi.Controller;

import com.example.universitycountryapi.Model.University;
import com.example.universitycountryapi.Service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    @GetMapping
    public List<University> getAllUniversities() {
        return universityService.getAllUniversities()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public List<University> getUniversitiesByCountries(@RequestBody List<String> countries) {
        return universityService.getUniversitiesByCountries(countries)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private University mapToResponse(University university) {
        University response = new University();
        response.setName(university.getName());
        response.setDomains(university.getDomains());
        response.setWeb_pages(university.getWeb_pages());
        return response;
    }
}
