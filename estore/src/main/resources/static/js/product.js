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

// Add a product to the cart
function addToCart(thisButton, productId, quantityInStock) {
    const quantityInput = thisButton.parentElement.querySelector(".quantityInput");
    validateQuantity(quantityInput, quantityInStock);
    const submitButton = quantityInput.parentElement.querySelector("button[type='submit']");

    // Triggers submit button in case of validation failure to show a validation error
    if (!quantityInput.checkValidity()) {
        submitButton.click();
        return;
    }
    thisButton.disabled = true;

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {

        if (this.readyState === 4) {

            if (this.status == 400) {
                quantityInput.style.borderColor = "red";
                quantityInput.setCustomValidity("Total amount in the cart exceeds available.");
                submitButton.click();
                thisButton.disabled = false;
                return;

            }

            if (this.status == 200) {
                const parsedResponse = JSON.parse(this.responseText);
                 quantityInput.style.borderColor = "green";
                 quantityInput.setCustomValidity("");
                 document.getElementById("productsInCart").innerHTML = parsedResponse.totalNumberOfProducts;

                thisButton.disabled = false;
            }

        }
    };
    xhttp.open("POST", "/order/product/add", true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    const data = JSON.stringify({
        productId: productId,
        quantity: quantityInput.value,
    });
    xhttp.send(data);

}


// provides quantity update for a product on event focusout
function updateQuantity(quantityinput, productid, quantityinstock) {


    // trigger submit button to show validation error messages
    if (quantityinput.value > quantityinstock || !validatequantity(quantityinput, quantityinstock)) {
        const sumbmitbutton = quantityinput.parentelement.queryselector("button");
        sumbmitbutton.click();
        return;
    }

    const xhttp = new xmlhttprequest();
    xhttp.onreadystatechange = function() {
        if (this.readystate === 4) {

            if (this.status !== 200) {
                window.location.href = "/error"
                return;
            }

            if (this.status == 200) {
                const parsedresponse = json.parse(this.responsetext);
                quantityinput.value = parsedresponse.quantity;
                document.getelementbyid("productsincart").innerhtml = parsedresponse.totalnumberofproducts;
                document.getelementbyid("totalprice").innerhtml = "$" + parsedresponse.totalorderprice;

            }

        }
    };
    xhttp.open("post", "/order/product/update", true);
    xhttp.setrequestheader("content-type", "application/json");
    const data = json.stringify({
        productid: productid,
        quantity: quantityinput.value,
    });
    xhttp.send(data);
}



