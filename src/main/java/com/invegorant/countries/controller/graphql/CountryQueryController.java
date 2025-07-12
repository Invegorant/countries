package com.invegorant.countries.controller.graphql;

import com.invegorant.countries.domain.graphql.CountryGql;
import com.invegorant.countries.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CountryQueryController {

    private final CountryService countryService;

    @Autowired
    public CountryQueryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @QueryMapping()
    public Page<CountryGql> allCountries(@Argument int page, @Argument int size) {
        return countryService.allGqlCountries(PageRequest.of(
                page, size
        ));
    }
}
