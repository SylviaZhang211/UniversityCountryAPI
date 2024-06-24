package com.example.universitycountryapi.controller;

import com.example.universitycountryapi.model.University;
import com.example.universitycountryapi.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.example.universitycountryapi.model.University;
import com.example.universitycountryapi.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {

    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService){
        this.universityService = universityService;
    }

    @GetMapping
    public ResponseEntity<List<University>> getAllUniversities() {
        return ResponseEntity.ok(universityService.getAllUniversities());
    }

    @GetMapping(params = "countries")
    public ResponseEntity<?> getUniversitiesByCountries(@RequestParam("countries") List<String> countries) {
        return new ResponseEntity<>(universityService.getByCountries(countries), HttpStatus.OK);
    }
}

