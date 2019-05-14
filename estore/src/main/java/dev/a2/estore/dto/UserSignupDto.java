/*
 * MIT License
 *
 * Copyright (c) 2019 Andrei Sidorov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.a2.estore.dto;

import dev.a2.estore.validation.FieldMatch;
import dev.a2.estore.validation.ValidEmail;
import dev.a2.estore.validation.ValidPassword;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * This class provides the dto of a new user.
 *
 * @author Andrei Sidorov
 */
@FieldMatch(field = "password", fieldMatch = "confirmPassword")
public class UserSignupDto {

    /**
     * The first name of a new user.
     */
    @NotBlank
    @Size(max = 35, message = "Invalid first name")
    private String firstName;

    /**
     * The last name of a new user.
     */
    @NotBlank
    @Size(max = 35, message = "Invalid last name")
    private String lastName;

    /**
     * The date of birth of a new user.
     */
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    /**
     * The email of a new user.
     */
    @NotBlank
    @ValidEmail
    private String email;

    /**
     * The password of a new user.
     */
    @NotBlank
    @ValidPassword
    private String password;

    /**
     * The confirm password of a new user.
     */
    private String confirmPassword;

    /**
     * The id of a country.
     */
    @NotNull
    private Long countryId;

    /**
     * The name of a city.
     */
    @NotBlank
    @Size(max = 35, message = "Invalid city")
    private String city;

    /**
     * The postal code of an address.
     */
    @NotBlank
    @Size(max = 16, message = "Invalid postal code")
    private String postalCode;

    /**
     * The name of a street.
     */
    @NotBlank
    @Size(max = 35, message = "Invalid street")
    private String street;

    /**
     * The number of a house.
     */
    @NotBlank
    @Size(max = 16, message = "Invalid house")
    private String house;

    /**
     * The number of an apartment.
     */
    @NotBlank
    @Size(max = 16, message = "Invalid apartment")
    private String apartment;

    /* Getters and setters. */

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return "UserSignupDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                ", password='" + "******" + '\'' +
                ", confirmPassword='" + "******" + '\'' +
                ", countryId=" + countryId +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                ", apartment='" + apartment + '\'' +
                '}';
    }

}
