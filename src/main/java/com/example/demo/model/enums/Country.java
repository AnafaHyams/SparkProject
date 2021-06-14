package com.example.demo.model.enums;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;


public enum Country {
    USA("USA"),
    PL("PL"),
    DE("DE");

    //private final Integer code;
    private final String country;

    private Country(String newCountry) {
        country = newCountry;
    }


//    public static Country getCountry(String countryName){
//        Country[] countries = values();
//        return Arrays.stream(countries).filter(item -> countryName == item.country)
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException(countryName + " not supported"));
//    }

    @Override
    public String toString() {
        return country;
    }
}
