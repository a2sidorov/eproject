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

// Provides validation of an input of a product quantity value
function validateQuantity(input, quantityInStock) {

    quantityInStockValue = parseInt(quantityInStock);
    inputValue = parseInt(input.value);

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

// Provides quantity update for a product on event focusout
async function updateQuantity(quantityInput, productId, quantityInStock) {
    // Trigger submit button to show validation error messages
    if (quantityInput.value > quantityInStock || !validateQuantity(quantityInput, quantityInStock)) {
        const sumbmitButton = quantityInput.parentElement.querySelector("button");
        sumbmitButton.click();
        return;
    }
    const amountCell = findAmountCell(quantityInput);

    const settings = {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            productId: productId,
            quantity: quantityInput.value,
        })
    };

    const response = await fetch('/order/product/update', settings);
    const result = await response.json();

    if (response.status === 200) {
        document.getElementById("productsInCart").innerText = result.totalNumberOfProducts;
        amountCell.lastElementChild.innerText = parseFloat(result.totalSellingPrice).toFixed(2);
        document.getElementById("orderPrice")
            .lastElementChild.innerText = parseFloat(result.orderPrice).toFixed(2);
    }
}

function findAmountCell(e) {
    let parentElement = e.parentElement;
    while (!parentElement.classList.contains("product-quantity")) {
        parentElement = parentElement.parentElement;
    }
    return parentElement.nextElementSibling;
}

function proceedToReserve() {
    const inputs = document.getElementsByClassName('quantity');
    for(let input of inputs) {
        if (!input.validity.valid) {
            return;
        }
    }
    window.location = "/checkout/reserve";
}

