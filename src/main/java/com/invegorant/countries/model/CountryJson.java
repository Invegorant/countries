package com.invegorant.countries.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CountryJson(
        @JsonProperty("countryName")
        String name,

        @JsonProperty("countryCode")
        String code) {
}
