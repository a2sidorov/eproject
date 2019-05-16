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

function toggleCategory(thisButton) {
    // caculating necessary height based on number of elements
    const dropdownContent = thisButton.parentElement.nextElementSibling;
    //const numOfChildren = dropdownContent.children.length;
    let numOfUlElement = dropdownContent.querySelectorAll("li").length;
    let numOfLiElement = dropdownContent.querySelectorAll("ul").length;
    //const height = (numOfUlElement + numOfLiElement) * 28 + 5;

    // opening/closing dropdown and changing arrow icon
    if (thisButton.innerHTML == "keyboard_arrow_down") {
        dropdownContent.style.height = "auto";
        thisButton.innerHTML = "keyboard_arrow_up"
    } else {
        dropdownContent.style.height = "0px";
        thisButton.innerHTML = "keyboard_arrow_down"
    }
}

// Sets the red border and provides error messsage to the margin level field if it is not chosen
const categoryInput = document.getElementById("categoryId");
categoryInput.setCustomValidity("Please choose category");
categoryInput.addEventListener("focusout", () => {
    if (categoryInput.value !== "Choose category") {
        categoryInput.style.borderColor = "green";
        categoryInput.setCustomValidity("");
    } else {
        categoryInput.style.borderColor = "red";
        categoryInput.setCustomValidity("Please choose category");
    }
});


// Sets the red border and provides error messsage to the measure units field if it is not chosen
const measureUnitsId = document.getElementById("measureUnitsId");
measureUnitsId.setCustomValidity("Please choose measure units");
measureUnitsId.addEventListener("focusout", () => {
    if (measureUnitsId.value !== "Choose measure units") {
        measureUnitsId.style.borderColor = "green";
        measureUnitsId.setCustomValidity("");
    } else {
        measureUnitsId.style.borderColor = "red";
        measureUnitsId.setCustomValidity("Please choose measure units");
    }
});

// Provides validation for user input, when input is valid sets input border color to green,
// when input is invalid sets border color to red and provides a custom validation message.
function validate(input) {
    let isValid, message;
    const inputValue = input.value.trim();
    const inputValueLength = inputValue.length;

    switch(input.id) {
        case "categoryId":
            isValid = isNumber(inputValue);
            message = inputValueLength > 1 ? "Invalid category" : "Please choose cateogry";
            break;
        case "name":
            isValid = isProductNameValid(inputValue);
            message = inputValueLength > 1 ? "Invalid product name" : "Please enter product name";
            break;
        case "purchasingPrice":
            isValid = isNumber(inputValue);
            message = inputValueLength > 1 ? "Invalid purchasing price" : "Please enter purchasing price";
            break;
        case "sellingPrice":
            isValid = isNumber(inputValue);
            message = inputValueLength > 1 ? "Invalid selling price" : "Please enter selling price";
            break;
        case "height":
            isValid = isNumber(inputValue);
            message = inputValueLength > 1 ? "Invalid product height" : "Please enter product height";
            break;
        case "width":
            isValid = isNumber(inputValue);
            message = inputValueLength > 1 ? "Invalid product width" : "Please enter product width";
            break;
        case "depth":
            isValid = isNumber(inputValue);
            message = inputValueLength > 1 ? "Invalid product depth" : "Please enter product depth";
            break;
        case "weight":
            isValid = isNumber(inputValue);
            message = inputValueLength > 1 ? "Invalid product weight" : "Please enter product weight";
            break;
        case "inStock":
            isValid = isNumber(inputValue);
            message = inputValueLength > 1 ? "Invalid product quantity" : "Please enter product quantity";
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

function isProductNameValid(inputValue) {
    return /^.{1,70}$/.test(inputValue);
}

function isNumber(inputValue) {
    console.log(!isNaN(inputValue) && inputValue >= 0);
    return !isNaN(inputValue) && inputValue >= 0;
}

