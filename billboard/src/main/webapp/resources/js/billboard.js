'use strict';

async function fetchTopProducts() {
    console.log('fetching');

    let products;
    try {
        const response = await fetch('/api/top-products');
        if (response.status === 200) {
            products = await response.json();
        }
    } catch (error) {
        console.error(error);
    }
    if (products !== undefined) {
        displayProducts(products);
    }
    openWebsocket();
}

const wsUri = "ws://localhost:8080/billboard/websocket";

function openWebsocket() {
    const websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) {
        onOpen(evt);
    };
    websocket.onmessage = function(evt) {
        onMessage(evt);
    };
    websocket.onerror = function(evt) {
        onError(evt);
    };
    websocket.onclose = function (evt) {
        console.log('Websocket is closed. Reconnect will be attempted in 2 second.', evt.reason);
        setTimeout(function() {
            connect();
        }, 2000);
    }
}
function onOpen(evt) {
    console.log("Connected to server!");
}
function onMessage(evt) {
    displayProducts(JSON.parse(evt.data));
}
function onError(evt) {
    console.error(env.data);
    openWebsocket();
}
//window.addEventListener("load", init, false);

function displayProducts(products) {
    const table = document.getElementById('table');

    while (table.firstChild) {
        table.removeChild(table.firstChild);
    }

    const headerCellOne = document.createElement('th');

    const headerCellTwo = document.createElement('th');

    const headerCellThree = document.createElement('th');
    headerCellThree.innerText = 'Price';

    const headerRow = document.createElement('tr');
    headerRow.appendChild(headerCellOne);
    headerRow.appendChild(headerCellTwo);
    headerRow.appendChild(headerCellThree);
    table.appendChild(headerRow);

    products.forEach(product => {

        const image = document.createElement('img');
        image.setAttribute('src', product.imageUrl);
        image.setAttribute('alt', 'No image found');

        const imageCell = document.createElement('td');
        imageCell.classList.add('product-image');
        imageCell.appendChild(image);

        const nameCell = document.createElement('td');
        nameCell.innerText = product.name;

        const saleCountCell = document.createElement('td');
        saleCountCell.innerText = '$' + product.sellingPrice;

        const row = document.createElement('tr');
        row.appendChild(imageCell);
        row.appendChild(nameCell);
        row.appendChild(saleCountCell);
        table.appendChild(row);
    });
}

