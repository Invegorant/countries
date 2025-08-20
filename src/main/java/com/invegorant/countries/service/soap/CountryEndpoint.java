package com.invegorant.countries.service.soap;

import com.invegorant.countries.config.AppConfig;
import com.invegorant.countries.domain.graphql.CountryGql;
import com.invegorant.countries.domain.graphql.CountryInputGql;
import com.invegorant.countries.service.CountryService;
import com.invegorant.countries.xml.country.CountriesResponse;
import com.invegorant.countries.xml.country.Country;
import com.invegorant.countries.xml.country.CountryInputRequest;
import com.invegorant.countries.xml.country.CountryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CountryEndpoint {
    private final CountryService countryService;

    public CountryEndpoint(CountryService countryService) {
        this.countryService = countryService;
    }


    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "allRequest")
    @ResponsePayload
    public CountriesResponse all(@RequestPayload PageRequest request) {
        Page<CountryGql> countries = countryService.allGqlCountries(PageRequest.of(
                request.getPageNumber(), request.getPageSize()
        ));
        CountriesResponse response = new CountriesResponse();
        response.getCountries().addAll(
                countries.stream()
                        .map(CountryEndpoint::fromGql)
                        .toList()
        );

        return response;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "countryInputRequest")
    @ResponsePayload
    public CountryResponse add(@RequestPayload CountryInputRequest request) {
        CountryGql country = countryService.addGqlCountry(
                new CountryInputGql(request.getName(), request.getCode())
        );
        CountryResponse response = new CountryResponse();
        Country xmlCountry = fromGql(country);
        response.setCountry(xmlCountry);

        return response;
    }

    private static Country fromGql(CountryGql country) {
        Country xmlCountry = new Country();
        xmlCountry.setId(country.id().toString());
        xmlCountry.setName(country.name());
        xmlCountry.setCode(country.code());
        return xmlCountry;
    }
}
