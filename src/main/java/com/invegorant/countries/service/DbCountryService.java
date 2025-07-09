package com.invegorant.countries.service;

import com.invegorant.countries.data.CountryEntity;
import com.invegorant.countries.data.CountryRepository;
import com.invegorant.countries.model.CountryJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class DbCountryService implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public DbCountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<CountryJson> allCountries() {
        return countryRepository.findAll().stream()
                .map(CountryEntity::toJson).toList();
    }

    @Override
    public CountryJson addCountry(CountryJson country) {
        return countryRepository.save(
                new CountryEntity(
                        null,
                        country.name(),
                        country.code()
                )
        ).toJson();
    }

    @Override
    public CountryJson editCountryName(String code, String newCountryName) {
        CountryEntity country = countryRepository.findCountryByCode(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Country not found with code: " + code
                ));

        country.setCountryName(newCountryName);
        return countryRepository.save(country).toJson();
    }
}
