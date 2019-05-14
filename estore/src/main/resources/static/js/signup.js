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
"use strict";

// Provides validation for user input, when input is valid sets input border color to green,
// when input is invalid sets border color to red and provides a custom validation message.
function validate(input) {
    let isValid, message;
    const inputValue = input.value.trim();
    const inputValueLength = inputValue.length;

    switch(input.id) {
        case "firstName":
            isValid = isFirstNameValid(inputValue);
            message = inputValueLength > 1 ? "Invalid first name" : "Please enter your first name";
            break;
        case "lastName":
            isValid = isLastNameValid(inputValue);
            message = inputValueLength > 1 ? "Invalid last name" : "Please enter your last name";
            break;
        case "dateOfBirth":
            isValid = isDateOfBirthValid(inputValue);
            message = inputValueLength > 1 ? "Invalid date of birth" : "Please enter your date of birth";
            break;
        case "email":
            isValid = isEmailValid(inputValue);
            message = inputValueLength > 1 ? "Invalid email" : "Please enter your email";
            break;
        case "password":
            isValid = isPasswordValid(inputValue);
            message = inputValueLength > 1 ? "Password must be less than 30 characters" : "Please enter your password";
            break;
        case "confirmPassword":
            isValid = doPasswordsMatch(inputValue);
            message = inputValueLength > 1 ? "Password do not match" : "Please repeat the password";
            break;
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

function isFirstNameValid(inputValue) {
    return /^[A-Za-z]{1,35}$/.test(inputValue);
}

function isLastNameValid(inputValue) {
    return /^[A-Za-z]{1,35}$/.test(inputValue);
}

function isDateOfBirthValid(inputValue) {
    return /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(inputValue);
}

function isEmailValid(inputValue) {
    return /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(inputValue)
        && inputValue.length < 255;
}

function isPasswordValid(inputValue) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{6,30}$/.test(inputValue);
}

function doPasswordsMatch(inputValue) {
    const passwordInput = document.getElementById("password");
    if (!passwordInput.hasAttribute("value")) {
        return false;
    }
    const passwordInputValue = passwordInput.value.trim();
    return isPasswordValid(passwordInputValue) && passwordInputValue === inputValue;
}

// Tests password complexity
function testPasswordComplexety() {
    const password = document.getElementById("password").value.trim();
    const lengthCheck = document.getElementById("lengthCheck");
    const uppercaseCharCheck = document.getElementById("uppercaseCharCheck");
    const lowercaseCharCheck = document.getElementById("lowercaseCharCheck");
    const numberCheck = document.getElementById("numberCheck");

    // testing for at least 6 characters
    if ((/^[A-Za-z0-9]{6,}$/).test(password)) {
        lengthCheck.innerHTML = "check";
        lengthCheck.style.color = "green";
    } else {
        lengthCheck.innerHTML = "clear";
        lengthCheck.style.color = "red";
    }

    // testing for at least one uppercase character
    if ((/.*[A-Z]+.*/).test(password)) {
        uppercaseCharCheck.innerHTML = "check";
        uppercaseCharCheck.style.color = "green"
    } else {
        uppercaseCharCheck.innerHTML = "clear";
        uppercaseCharCheck.style.color = "red";
    }

    // testing for at least one lowercase character
    if ((/.*[a-z]+.*/).test(password)) {
        lowercaseCharCheck.innerHTML = "check";
        lowercaseCharCheck.style.color = "green";
    } else {
        lowercaseCharCheck.innerHTML = "clear";
        lowercaseCharCheck.style.color = "red";
    }

    // testing for at least one number
    if ((/.*[0-9]+.*/).test(password)) {
        numberCheck.innerHTML = "check";
        numberCheck.style.color = "green";
    } else {
        numberCheck.innerHTML = "clear";
        numberCheck.style.color = "red";
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






