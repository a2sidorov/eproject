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
        case "cardNumber":
            isValid = isCardNumberValid(inputValue);
            message = inputValueLength > 1 ? "Invalid card number" : "Please enter card number";
            break;
        case "nameOnCard":
            isValid = isNameOnCardValid(inputValue);
            message = inputValueLength > 1 ? "Invalid name" : "Please enter cardholder name";
            break;
        case "CVV":
            isValid = isCvvValid(inputValue);
            message = inputValueLength > 1 ? "Invalid card verification value" : "Please enter CVV number";
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

function isCardNumberValid(inputValue) {
    return /^[0-9]{16}$/.test(inputValue);
}

function isNameOnCardValid(inputValue) {
    return /^.{1,35}$/.test(inputValue);
}

function isCvvValid(inputValue) {
    return /^[0-9]{3}$/.test(inputValue);
}

const paymentMethodSection = document.getElementById("payment-method-section");
const cardInfo = document.getElementById("card-info");

function showCardInputFields() {
    paymentMethodSection.style.height = "255px";
    setTimeout(() => {
        cardInfo.style.display = "block";
        document.getElementById("cardNumber").value = "";
        document.getElementById("nameOnCard").value = "";
        document.getElementById("CVV").value = "";
    }, 500);
}

function hideCardInputFields() {
    document.getElementById("cardNumber").setCustomValidity("");
    cardInfo.style.display = "none";
    paymentMethodSection.style.height = "86px";
    document.getElementById("cardNumber").value = "0000000000000000";
    document.getElementById("nameOnCard").value = "not entered";
    document.getElementById("CVV").value = "000";
}

function startTimer(checkoutReserveTimeString, secondsLeftString) {
    const timer = document.getElementById("timer");
    const checkoutReserveTime = parseInt(checkoutReserveTimeString);
    let secondsLeft = parseInt(secondsLeftString);
    secondsLeft = checkoutReserveTime - secondsLeft;
    const interval = setInterval(function() {
        if (secondsLeft < 1) {
            timer.innerText = '00:00';
            timer.style.color = 'red';
            clearInterval(interval);
            return;
        }
        secondsLeft--;
        let minutes = Math.floor(secondsLeft / 60);
        let seconds = secondsLeft % 60;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        seconds = seconds < 10 ? '0' + seconds : seconds;
        timer.innerText = minutes + ':' + seconds;
    }, 1000);
}


