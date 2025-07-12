package com.invegorant.countries.service;

import com.invegorant.countries.data.CountryEntity;
import com.invegorant.countries.data.CountryRepository;
import com.invegorant.countries.domain.graphql.CountryGql;
import com.invegorant.countries.domain.graphql.CountryInputGql;
import com.invegorant.countries.model.CountryJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<CountryGql> allGqlCountries(Pageable pageable) {
        return countryRepository.findAll(pageable)
                .map(fe -> new CountryGql(
                        fe.getId(),
                        fe.getCountryName(),
                        fe.getCountryCode()
                ));
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
    public CountryGql addGqlCountry(CountryInputGql input) {
        CountryEntity ce = new CountryEntity();
        ce.setCountryCode(input.code());
        ce.setCountryName(input.name());
        CountryEntity saved = countryRepository.save(ce);
        return new CountryGql(
                saved.getId(),
                saved.getCountryName(),
                saved.getCountryCode()
        );
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

    @Override
    public CountryGql editGqlCountryName(String code, String newCountryName) {
        CountryEntity ce;
        try {
            ce = countryRepository.findCountryByCode(code)
                    .orElseThrow(Exception::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ce.setCountryName(newCountryName);
        CountryEntity updated = countryRepository.save(ce);
        return new CountryGql(
                updated.getId(),
                updated.getCountryName(),
                updated.getCountryCode()
        );
    }
}
