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

function sortBy(column, direction) {
    document.getElementById('sortBy').value = column;
    document.getElementById('sortDirection').value = direction;
    document.getElementById('findButton').click();
}

function validate(input) {
    let isValid, message;
    const inputValue = input.value.trim();
    const inputValueLength = inputValue.length;

    if (inputValue.length > 0) {
        switch(input.id) {
            case "orderId":
                isValid = isOrderIdValid(inputValue);
                message = "Invalid order number";
                break;
            case "startDate":
                isValid = isDateValid(inputValue);
                message = "Invalid date";
                break;
            case "endDate":
                isValid = isDateValid(inputValue);
                message = "Invalid date";
                break;
            case "minPrice":
                isValid = isPriceValid(inputValue);
                message = "Invalid price";
                break;
            case "maxPrice":
                isValid = isPriceValid(inputValue);
                message = "Invalid price";
                break;
        }
    } else {
        isValid = true;
    }

    if (isValid) {
        input.style.borderColor = "#ccc";
        input.setCustomValidity("");
        return true;
    } else {
        input.style.borderColor = "red";
        input.setCustomValidity(message);
        return false;
    }

}

function isOrderIdValid(inputValue) {
    return !isNaN(inputValue) && inputValue >= 1;
}

function isDateValid(inputValue) {
    return /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(inputValue);
}

function isPriceValid(inputValue) {
    return !isNaN(inputValue) && inputValue >= 0;
}


async function toggleOrderDetails(thisButton, orderId, totalPrice, isFetched) {
    const nextRow = thisButton.parentElement.parentElement.nextElementSibling;

    if (!isFetched) {

        let products;
        try {
            const response = await fetch(`/my-orders/${orderId}`);
            products = await response.json();
            console.log(products);
        } catch (error) {
            console.error(error.message);
        }

        products.forEach(product => {

            const productRow = document.createElement("tr");

            const link = document.createElement("a");
            link.href = "/product?id=" + product.id;

            const image = document.createElement("img");
            image.src = product.imageUrl;
            const imageCell = document.createElement("td");
            imageCell.classList.add("product-image");
            link.appendChild(image);
            imageCell.appendChild(link);
            productRow.appendChild(imageCell);

            const productNameCell = document.createElement("td");
            productNameCell.innerHTML = product.name;
            productNameCell.classList.add("product-name");
            productRow.appendChild(productNameCell);

            const productPriceCell = document.createElement("td");
            productPriceCell.innerHTML = '$' + product.price;
            productPriceCell.classList.add("product-price");
            productRow.appendChild(productPriceCell);

            const productQuantityCell = document.createElement("td");
            productQuantityCell.innerHTML = product.quantity + " " + product.units;
            productQuantityCell.classList.add("product-quantity");
            productRow.appendChild(productQuantityCell);

            const productAmountCell = document.createElement("td");
            productAmountCell.innerHTML = '$' + product.amount;
            //productAmountCell.classList.add("product-quantity");
            productRow.appendChild(productAmountCell);

            const tableHeader = nextRow.firstElementChild.firstElementChild.firstElementChild.firstElementChild;
            tableHeader.after(productRow);
        });
    }


    if (nextRow.style.display === "none") {
        nextRow.style.display = "table-row";
        thisButton.firstElementChild.innerHTML = "keyboard_arrow_up";
        thisButton.onclick = () => toggleOrderDetails(thisButton, orderId, totalPrice,true);
    } else {
        nextRow.style.display = "none";
        thisButton.firstElementChild.innerHTML = "keyboard_arrow_down";
    }



}




