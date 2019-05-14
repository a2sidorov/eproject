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

// Disables all input fields
const inputs = document.getElementsByTagName("input");
for (let i = 0; i < inputs.length; i++) {
    inputs[i].disabled = true;
}
document.getElementById("measureUnitsId").disabled = true;
document.getElementById("categoryId").disabled = true;

// Provides validation for user input, when input is valid sets input border color to green,
// when input is invalid sets border color to red and provides a custom validation message.
function validate(input) {
    let isValid, message;
    const inputValue = input.value.trim();
    const inputValueLength = inputValue.length;

    switch(input.id) {
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
        case "image":
            isValid = isImageValid(input);
            message = "Invalid image";
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

function isProductNameValid(inputValue) {
    return /^.{1,255}$/.test(inputValue);
}

function isNumber(inputValue) {
    return !isNaN(inputValue);
}

function toggleCategory(thisButton) {
    // caculating necessary height based on number of elements
    const dropdownContent = thisButton.parentElement.nextElementSibling;
    //const numOfChildren = dropdownContent.children.length;
    let numOfUlElement = dropdownContent.querySelectorAll("li").length;
    let numOfLiElement = dropdownContent.querySelectorAll("ul").length;
    //const height = (numOfUlElement + numOfLiElement) * 28 + 5;

    // opening/closing dropdown and changing arrow icon
    if (thisButton.innerHTML === "keyboard_arrow_down") {
        dropdownContent.style.height = "auto";
        thisButton.innerHTML = "keyboard_arrow_up"
    } else {
        dropdownContent.style.height = "0px";
        thisButton.innerHTML = "keyboard_arrow_down"
    }
}



// Sets the red border and provides error messsage to the measure units field if it is not chosen
const measureUnitsId = document.getElementById("measureUnitsId");
measureUnitsId.addEventListener("focusout", () => {
    if (measureUnitsId.value !== "Choose measure units") {
        measureUnitsId.style.borderColor = "green";
        measureUnitsId.setCustomValidity("");
    } else {
        measureUnitsId.style.borderColor = "red";
        measureUnitsId.setCustomValidity("Please choose measure units");
    }
});

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

    if (isProductNameValid(document.getElementById("name")) &&
        isNumber(document.getElementById("purchasingPrice")) &&
        isNumber(document.getElementById("sellingPrice")) &&
        isNumber(document.getElementById("height")) &&
        isNumber(document.getElementById("width")) &&
        isNumber(document.getElementById("depth")) &&
        isNumber(document.getElementById("weight"))) {
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

    let selectElements = formElement.querySelectorAll("select");
    selectElements.forEach(e => {
        e.value = e.getAttribute("data-currentValue");
        e.style.borderColor = "#ccc";
        e.disabled = true;
    });

    discard.parentElement.style.display = "none";
    discard.parentElement.parentElement.querySelector("em").style.display = "block";
}

function findFormElement(e) {
    let parentElement = e.parentElement;
    while (parentElement.tagName !== 'FORM') {
        parentElement = parentElement.parentElement;
    }
    return parentElement;
}

function highlightCategory(clickedCategory) {
    const categoryDiv = document.getElementById("create-product-categories");
    const categories = categoryDiv.querySelectorAll('a');
    categories.forEach(category => {
        category.style.color = "black";
    });
    clickedCategory.style.color = "green";
}
