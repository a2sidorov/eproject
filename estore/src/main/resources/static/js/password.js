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

// Displays submit button in page Change password
function showSubmitButton() {
    document.querySelector("button[type='submit']").style.display = "block";
}

// Provides validation for user input, when input is valid sets input border color to green,
// when input is invalid sets border color to red and provides a custom validation message.
function validate(input) {
    let isValid, message;
    const inputValue = input.value.trim();
    const inputValueLength = inputValue.length;

    switch(input.id) {
        case "password":
            isValid = validatePassword(inputValue);
            message = inputValueLength > 1 ? "Password must be less than 30 characters" : "Please enter your password";
            break;
        case "confirmPassword":
            isValid = matchPasswords(inputValue);
            message = inputValueLength > 1 ? "Password do not match" : "Please repeat the password";
            break;
    }

    if (isValid) {
        input.style.borderColor = "green";
        input.setCustomValidity("");
    } else {
        input.style.borderColor = "red";
        input.setCustomValidity(message);

    }

}

function validatePassword(inputValue) {
    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{6,30}$/.test(inputValue);
}

function matchPasswords(inputValue) {
    const passwordInputValue = document.getElementById("password").value.trim();
    return validatePassword(passwordInputValue) && passwordInputValue === inputValue;
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

