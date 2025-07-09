package com.invegorant.countries.controller;

import com.invegorant.countries.model.CountryJson;
import com.invegorant.countries.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/country")
public class CountriesController {

    private final CountryService countryService;

    @Autowired
    public CountriesController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    public List<CountryJson> allCountries() {
        return countryService.allCountries();
    }

    @PostMapping("/add")
    public CountryJson addCountry(@RequestBody CountryJson countryJson) {
        return countryService.addCountry(countryJson);
    }

    @PatchMapping("/edit/{code}")
    public CountryJson editCountryName(@PathVariable("code") String code,
                                       @RequestBody CountryJson countryJson) {
        return countryService.editCountryName(code, countryJson.name());
    }
}
