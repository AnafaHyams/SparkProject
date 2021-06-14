package com.example.demo.model;

import com.example.demo.model.enums.Country;
import lombok.*;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {
    @Id
    @Column(name="ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="LASTNAME")
    private String lastName;

    @Column(name="COUNTRYOFORIGIN")
    private String countryOfOrigin;

    @Column(name="EMAIL")
    private String email;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public String getEmail() {
        return email;
    }
}
