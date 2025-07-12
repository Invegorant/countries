package com.invegorant.countries.service;

import com.invegorant.countries.domain.graphql.CountryGql;
import com.invegorant.countries.domain.graphql.CountryInputGql;
import com.invegorant.countries.model.CountryJson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountryService {

    List<CountryJson> allCountries();

    Page<CountryGql> allGqlCountries(Pageable pageable);

    CountryJson addCountry(CountryJson country);

    CountryGql addGqlCountry(CountryInputGql input);

    CountryJson editCountryName(String code, String newCountryName);

    CountryGql editGqlCountryName(String code, String newCountryName);
}
