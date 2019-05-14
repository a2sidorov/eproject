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

let isFilterApplied = false;
let maxResults = 12;

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

async function findProducts() {
    let searchCriteria = {};
    //const maxResults = 8;
    const searchInput = document.getElementById("searchInput");
    const categoryIdInput = document.getElementById("categoryIdInput");

    searchCriteria['input'] = searchInput.value;
    searchCriteria['categoryId'] = categoryIdInput.value;

    const attributeList = document.getElementById('attribute-list');
    const selectInputs = attributeList.querySelectorAll('select');

    if (isFilterApplied) {
        selectInputs.forEach(select => {
            searchCriteria[select.name] = select.options[select.selectedIndex].value;
        });
    }

    const settings = {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(searchCriteria
            /*
            {
            input: searchInput.value,
            categoryId: categoryIdInput.value,
        }
        */
        )
    };

    let products;
    try {
        const response = await fetch(`/products/find`, settings);
        products = await response.json();
    } catch (error) {
        console.error(error.message);
    }
    displayProductsPartly(products, 1, maxResults);
    displayResultNavBar(products, 1,  1, maxResults);
}

async function getProducts() {
    //const maxResults = 8;

    let products;
    try {
        const response = await fetch('/products');
        products = await response.json();
    } catch (error) {
        console.error(error.message);
    }
    displayProductsPartly(products, 1, maxResults);
    displayResultNavBar(products, 1,  1, maxResults);
}

function displayResultNavBar(products, index, start, maxResults) {
    const resultNavBar = document.getElementById("product-list-navbar");
    while (resultNavBar.firstChild) {
        resultNavBar.removeChild(resultNavBar.firstChild);
    }
    for (let i = start; i <= Math.ceil(products.length / maxResults) && i < 5 + start; i++) {
        const li = document.createElement('li');
        const indexButton = document.createElement("button");
        indexButton.addEventListener('click', () => {
            displayProductsPartly(products, i, maxResults);
        });
        indexButton.innerText = i;
        if (i === index) {
            indexButton.style.color = "green";
        }
        li.appendChild(indexButton);
        resultNavBar.appendChild(li);
    }
    const nextButton = document.createElement("button");

}

async function getProductsByCategory(categoryId, categoryType, thisLink) {
    //const maxResults = 8;
    const searchInput = document.getElementById('searchInput');
    const categoryIdInput = document.getElementById('categoryIdInput');
    searchInput.value = "";
    categoryIdInput.value = categoryId;

    let products;
    try {
        const response = await fetch('/products/category?id=' + categoryId);
        products = await response.json();
    } catch (error) {
        console.error(error.message);
    }
    displayProductsPartly(products, 1, maxResults);
    displayResultNavBar(products, 1,  1, maxResults);

    let attributes;
    try {
        const response = await fetch('/attributes/category?id=' + categoryId);
        attributes = await response.json();
    } catch (error) {
        console.error(error.message);
    }
    if (attributes !== undefined) {
        displayAttributeFilter(attributes);
    }
    highlightCategory(thisLink);
    displayCategoryName(thisLink);
}

function highlightCategory(clickedCategory) {
    const categoryDiv = document.getElementById("home-categories");
    const categories = categoryDiv.querySelectorAll('a');
    categories.forEach(category => {
        category.style.color = "black";
    });
    clickedCategory.style.color = "green";
}

function displayCategoryName(thisCategory) {
    document.getElementById('categoryName').innerText = 'Category: ' + thisCategory.innerText;
}

function displayProductsPartly(products, index) {
    const productList = document.getElementById('product-list');
    while (productList.firstChild) {
        productList.removeChild(productList.firstChild);
    }
    const start = (index - 1) * maxResults;
    const end = index * maxResults;

    for (let i = start; i < end && i < products.length; i++) {
        const img = document.createElement('img');
        img.setAttribute('src', products[i].imageUrl);
        img.setAttribute('alt', 'No image uploaded');

        const link = document.createElement('a');
        link.setAttribute('href', '/product?id=' + products[i].id)
        link.appendChild(img);

        const imageDiv = document.createElement('div');
        imageDiv.classList.add('product-image');
        imageDiv.appendChild(link);

        const nameDiv = document.createElement('div');
        nameDiv.classList.add('product-name');
        nameDiv.innerText = products[i].name;

        const buyInfoDiv = document.createElement('div');
        buyInfoDiv.classList.add('buy-info');

        const priceDiv = document.createElement('div');
        priceDiv.innerText = '$' + parseFloat(products[i].sellingPrice).toFixed(2);

        const qtyInStockDiv = document.createElement('div');
        qtyInStockDiv.innerText = `In stock: ${products[i].quantityInStock} ${products[i].measureUnits.name}`;

        const inputDiv = document.createElement('div');
        const input = document.createElement('input');
        input.value = '1';
        input.addEventListener("keyup", () => {
            validateQuantity(input, products[i].quantityInStock);
        });
        const submitButton = document.createElement('button');
        submitButton.setAttribute('type', 'submit');
        inputDiv.appendChild(input);
        inputDiv.appendChild(submitButton);

        buyInfoDiv.appendChild(priceDiv);
        buyInfoDiv.appendChild(qtyInStockDiv);
        buyInfoDiv.appendChild(inputDiv);

        const quantityForm = document.createElement('form');
        quantityForm.setAttribute('id', 'productQuantityDto');
        quantityForm.setAttribute('action', '/product/order/add');
        quantityForm.setAttribute('method', 'post');
        quantityForm.appendChild(imageDiv);
        quantityForm.appendChild(nameDiv);
        quantityForm.appendChild(buyInfoDiv);

        const addToCartButton = document.createElement('button');
        addToCartButton.addEventListener('click', () => {
            addProductToCart(input, products[i].quantityInStock, products[i].id, addToCartButton, submitButton);
        });
        addToCartButton.innerText = 'Add to cart';

        const li = document.createElement('li');
        li.appendChild(quantityForm);
        li.appendChild(addToCartButton);
        productList.appendChild(li);
    }

    let startIndex;
    if (index < 3) {
        startIndex = 1;
    } else {
        startIndex = index - 2;
    }

    displayResultNavBar(products,  index, startIndex,  maxResults);
}


function displayAttributeFilter(attributes) {
    const filterSection = document.getElementById("filter-section");
    const attributeList = document.getElementById("attribute-list");

    while (attributeList.firstChild) {
        attributeList.removeChild(attributeList.firstChild);
    }
    filterSection.style.display = 'none';

    if (attributes.length === 0) {
        return;
    }

    attributes.forEach(attribute => {
        const li = document.createElement('li');
        const label = document.createElement('label');
        label.innerText = attribute.name;
        const select = document.createElement('select');
        select.setAttribute('name', attribute.name);
        const defaultOption = document.createElement('option');
        defaultOption.setAttribute('value', 'All');
        defaultOption.innerText = 'All';
        select.appendChild(defaultOption);
        attribute.values.forEach(value => {
            const option = document.createElement('option');
            option.setAttribute('value', value);
            option.innerText = value;
            select.appendChild(option);
        });
        li.appendChild(label);
        li.appendChild(select);
        attributeList.appendChild(li);
    });
    filterSection.style.display = 'block';
}

// Add a product to the cart
async function addProductToCart(input, quantityInStock, productId, thisButton, submitButton) {
    // Triggers submit button in case of validation failure to show a validation error
    if (!validateQuantity(input, quantityInStock)) {
        submitButton.click();
        return;
    }

    // Disables button while data is submitted
    thisButton.disabled = true;

    const settings = {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            productId: productId,
            quantity: input.value,
        })
    };

    let result;
    try {
        const response = await fetch(`/order/product/add`, settings);
        result = await response.json();
        if (response.status === 400) {
            input.style.borderColor = "red";
            input.setCustomValidity("Total amount in the cart exceeds available.");
            submitButton.click();
            thisButton.disabled = false;
            return;
        }
    } catch (error) {
        console.error(error.message);
        input.style.borderColor = "red";
        input.setCustomValidity("Total amount in the cart exceeds available.");
        submitButton.click();
        thisButton.disabled = false;
        return;
    }

    input.style.borderColor = "green";
    input.setCustomValidity("");
    document.getElementById("productsInCart").innerHTML = result.totalNumberOfProducts;
    thisButton.disabled = false;
}

// Provides validation of the input of the product quantity value
function validateQuantity(input, quantityInStock) {

    const quantityInStockValue = parseInt(quantityInStock);
    const inputValue = parseInt(input.value);

    let isQuantityValid = true;
    let message;

    if (!/^[0-9]{1,10}$/.test(input.value)) {
        isQuantityValid = false;
        message = "Invalid quantity."
    }

    if (inputValue > quantityInStockValue) {
        isQuantityValid = false;
        message = "Quantity cannot be greater than is in stock."
    }

    if (inputValue < 1) {
        isQuantityValid = false;
        message = "Invalid quantity."
    }

    if (isQuantityValid) {
        input.style.borderColor = "green";
        input.setCustomValidity("");
        return true;
    } else {
        input.style.borderColor = "red";
        input.setCustomValidity(message);
        return false;
    }
}

function applyFilter(checkbox) {
    isFilterApplied = checkbox.checked;
    console.log(isFilterApplied)
}



