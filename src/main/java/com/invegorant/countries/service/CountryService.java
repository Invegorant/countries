package com.invegorant.countries.service;

import com.invegorant.countries.model.CountryJson;

import java.util.List;

public interface CountryService {

    List<CountryJson> allCountries();

    CountryJson addCountry(CountryJson country);

    CountryJson editCountryName(String code, String newCountryName);
}
