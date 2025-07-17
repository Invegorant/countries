package com.invegorant.countries.service;

import com.google.protobuf.Empty;
import com.invegorant.countries.domain.graphql.CountryGql;
import com.invegorant.countries.domain.graphql.CountryInputGql;
import com.invegorant.countries.model.CountryJson;
import guru.qa.grpc.country.*;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GrpcCountryService extends CountryServiceGrpc.CountryServiceImplBase {

    private final CountryService countriesService;

    public GrpcCountryService(CountryService countriesService) {
        this.countriesService = countriesService;
    }

    @Override
    public void allCountries(Empty request, StreamObserver<CountryListResponse> responseObserver) {
        List<CountryJson> countries = countriesService.allCountries();
        List<CountryResponse> response = countries.stream()
                .map(country -> CountryResponse.newBuilder()
                        .setName(country.name())
                        .setCode(country.code())
                        .build())
                .toList();

        CountryListResponse countryListResponse = CountryListResponse
                .newBuilder()
                .addAllCountries(response)
                .build();
        responseObserver.onNext(countryListResponse);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<CountryRequest> addCountries(StreamObserver<CountResponse> responseObserver) {
        AtomicInteger count = new AtomicInteger();

        return new StreamObserver<>() {

            @Override
            public void onNext(CountryRequest countryRequest) {
                countriesService.addGqlCountry(
                        new CountryInputGql(
                                countryRequest.getName(),
                                countryRequest.getCode()
                        )
                );

                count.incrementAndGet();
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                CountResponse response = CountResponse.newBuilder()
                        .setCount(count.get())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void updateCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {

        CountryGql updatedCountry = countriesService.editGqlCountryName(
                request.getCode(),
                request.getName());

        CountryResponse response = CountryResponse.newBuilder()
                .setId(updatedCountry.id().toString())
                .setName(updatedCountry.name())
                .setCode(updatedCountry.code())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
