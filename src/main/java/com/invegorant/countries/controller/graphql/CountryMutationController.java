package com.invegorant.countries.controller.graphql;

import com.invegorant.countries.domain.graphql.CountryGql;
import com.invegorant.countries.domain.graphql.CountryInputGql;
import com.invegorant.countries.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CountryMutationController {

    private final CountryService countryService;

    @Autowired
    public CountryMutationController(CountryService countryService) {
        this.countryService = countryService;
    }

    @MutationMapping
    public CountryGql addCountry(@Argument CountryInputGql input) {
        return countryService.addGqlCountry(input);
    }

    @MutationMapping
    public CountryGql editCountryName(@Argument CountryInputGql input) {
        return countryService.editGqlCountryName(input.code(), input.name());
    }
}
