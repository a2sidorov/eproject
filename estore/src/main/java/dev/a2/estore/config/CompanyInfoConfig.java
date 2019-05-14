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
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package dev.a2.estore.config;

import dev.a2.estore.model.CompanyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * This class provides a bean with company information.
 *
 * @author Andrei Sidorov
 */
@Configuration
@PropertySource("classpath:company.properties")
public class CompanyInfoConfig {

    /**
     * Holds the properties of file '/resources/company.properties'.
     */
    private final Environment env;

    /**
     * Initializes company properties.
     *
     * @param env a holder for company properties.
     */
    @Autowired
    public CompanyInfoConfig(final Environment env) {
        this.env = env;
    }

    /**
     * This bean provides an object with company information.
     *
     * @return an object with the information about the company.
     */
    @Bean
    public CompanyInfo initCompanyInfo() {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setName(env.getProperty("company.name"));
        companyInfo.setEmail(env.getProperty("company.email"));
        companyInfo.setWebsite(env.getProperty("company.website"));
        companyInfo.setPhoneNumber(env.getProperty("company.phone_number"));
        companyInfo.setFaxNumber(env.getProperty("company.fax_number"));
        companyInfo.setCountry(env.getProperty("company.address.country"));
        companyInfo.setPostalCode(env.getProperty("company.address.postal_code"));
        companyInfo.setCity(env.getProperty("company.address.city"));
        companyInfo.setStreet(env.getProperty("company.address.street"));
        companyInfo.setHouse(env.getProperty("company.address.house"));
        companyInfo.setApartment(env.getProperty("company.address.apartment"));
        return companyInfo;
    }

}
