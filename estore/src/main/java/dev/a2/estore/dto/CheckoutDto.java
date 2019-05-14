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

import dev.a2.estore.model.PaymentMethod;
import dev.a2.estore.model.ShippingMethod;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * This class provides the dto of payment information.
 *
 * @author Andrei Sidorov
 */
public class CheckoutDto {

    /**
     * The payment method.
     */
    private PaymentMethod paymentMethod;

    /**
     * The shipping method.
     */
    private ShippingMethod shippingMethod;

    /**
     * The number of a client's credit card.
     */
    @Pattern(regexp = "^[0-9]{16}$", message = "Invalid card number")
    private String cardNumber;

    /**
     * The name on a client's credit card.
     */
    @Size(max = 70, message = "Invalid cardholder name")
    private String nameOnCard;

    /**
     * The month when client's credit card gets expired.
     */
    private String expirationMonth;

    /**
     * The year when client's credit card gets expired.
     */
    private String expirationYear;

    /**
     * The card cerification value of a client's credit card.
     *
     */
    @Pattern(regexp = "^[0-9]{3}$", message = "Invalid CVV")
    private String CVV;

    /**
     * The id of a client's address.
     */
    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long addressId;

    /* Getters an setters. */

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "CheckoutDto{" +
                "paymentMethod=" + paymentMethod +
                ", shippingMethod=" + shippingMethod +
                ", cardNumber='" + cardNumber + '\'' +
                ", nameOnCard='" + nameOnCard + '\'' +
                ", expirationMonth='" + expirationMonth + '\'' +
                ", expirationYear='" + expirationYear + '\'' +
                ", CVV='" + CVV + '\'' +
                ", addressId=" + addressId +
                '}';
    }
}

