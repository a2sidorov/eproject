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
"use strict";

// Provides validation for user input, when input is valid sets input border color to green,
// when input is invalid sets border color to red and provides a custom validation message.
function validate(input) {
    let isValid, message;
    const inputValue = input.value.trim();
    const inputValueLength = inputValue.length;

    switch(input.id) {
        case "city":
            isValid = /^.{1,35}$/.test(inputValue);
            message = inputValueLength > 1 ? "Invalid city name" : "Please enter your city name";
            break;
        case "postalCode":
            isValid = /^.{1,16}$/.test(inputValue);
            message = inputValueLength > 1 ? "Invalid postal code" : "Please enter your postal code";
            break;
        case "street":
            isValid = /^.{1,35}$/.test(inputValue);
            message = inputValueLength > 1 ? "Invalid street name" : "Please enter your street name";
            break;
        case "house":
            isValid = /^.{1,16}$/.test(inputValue);
            message = inputValueLength > 1 ? "Invalid house number" : "Please enter your house number";
            break;
        case "apartment":
            isValid = /^.{1,16}$/.test(inputValue);
            message = inputValueLength > 1 ? "Invalid apartment number" : "Please enter your apartment number";
            break;
    }

    if (isValid) {
        input.style.borderColor = "green";
        input.setCustomValidity("");
        return true;
    } else {
        input.style.borderColor = "red";
        input.setCustomValidity(message);
        return false;
    }

}

// Sets the red border and provides error messsage to the country field if a country is not chosen
const countryInput = document.getElementById("countryId");
if (countryInput.value === "Choose your country") {
    countryInput.setCustomValidity("Please choose your country");
}

countryInput.addEventListener("focusout", () => {
    if (countryInput.value !== "Choose your country") {
        countryInput.style.borderColor = "green";
        countryInput.setCustomValidity("");
    } else {
        countryInput.style.borderColor = "red";
        countryInput.setCustomValidity("Please choose your country");
    }
});

// Provides red border for the email field if a user with the entered email exists.
const errors = document.querySelectorAll(".error");
for (let i = 0; i < errors.length; i++) {
    const input = errors[i].parentElement.querySelector("#email");
    input.style.borderColor = "red";

}


