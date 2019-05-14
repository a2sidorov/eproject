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
'use strict';

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

// Disables all input fields
const inputs = document.getElementsByTagName("input");
for (let i = 0; i < inputs.length; i++) {
    inputs[i].disabled = true;
}

// Disables country select fields
const countryFields = document.getElementsByTagName("select");
for (let i = 0; i < countryFields.length; i++) {
    countryFields[i].disabled = true;
}

// Enables field editing
function edit(pen) {
    const formElement = findFormElement(pen);

    formElement.querySelectorAll("input").forEach(inputField => {
        inputField.disabled = false;
    });

    let selectElements = formElement.querySelectorAll("select");
    selectElements.forEach(e => {
        if (e != null) {
            e.disabled = false;
        }
    });

    pen.style.display = "none";
    pen.parentElement.lastElementChild.style.display = "block";
}

function commit(check) {
    const formElement = findFormElement(check);

    if (isFirstNameValid(document.getElementById("firstName"))
        && isLastNameValid(document.getElementById("lastName"))
        && isDateOfBirthValid(document.getElementById("dateOfBirth"))) {
        check.parentElement.style.display = "none";
    }
    formElement.querySelector("button").click();
}

function discard(discard) {
    const formElement = findFormElement(discard);

    formElement.querySelectorAll("input").forEach(inputField => {
       inputField.value = inputField.getAttribute("data-currentValue");
       inputField.disabled = true;
       inputField.style.borderColor = "#ccc";
    });

    let selectElement = formElement.querySelector("select");

    if (selectElement != null) {
        const currentValue = selectElement.getAttribute("data-currentValue");
        selectElement.value = currentValue;
        selectElement.disabled = true;
    }

    discard.parentElement.style.display = "none";
    discard.parentElement.parentElement.querySelector("i").style.display = "block";
}


function removeAddress(id, country, city, postalCode, street, house, apartment) {
    const ask = confirm(`Are you sure you want to remove address:\n
        ${house} ${street} Apt. ${apartment} ${city} ${country} ${postalCode}`);

    if (ask) {
        window.location=`/profile/address/remove/${id}`;
    }
}

function findFormElement(e) {
    let parentElement = e.parentElement;
    while (parentElement.tagName !== 'FORM') {
        parentElement = parentElement.parentElement;
    }
    return parentElement;
}
