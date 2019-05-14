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



let interval = "monthly";

function chooseIntervalMonthly() {
    interval = "monthly";
}

function chooseIntervalWeekly() {
    interval = "weekly";
}

async function getRevenues() {
    const fromInput = document.getElementById("start");
    const toInput = document.getElementById("end");
    const submitButton = document.getElementById("submit-button");

    if (!isTimePeriodValid(fromInput.value, toInput.value)) {
        fromInput.style.borderColor = "red";
        fromInput.setCustomValidity("Invalid time period")
        toInput.style.borderColor = "red";
        toInput.setCustomValidity("Invalid time period")
        submitButton.click();
        return;
    } else {
        fromInput.style.borderColor = "green";
        fromInput.setCustomValidity("");
        toInput.style.borderColor = "green";
        toInput.setCustomValidity("");
    }


    const settings = {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            start: fromInput.value,
            end: toInput.value,
            interval: interval,
        })
    };

    let result;
    try {
        const response = await fetch(`/reports/revenue`, settings);
        result = await response.json();
    } catch (error) {
        console.error(error);
    }

    const table = document.getElementById("revenue-table");

    //Remove previous result
    while (table.firstChild) {
        table.removeChild(table.firstChild);
    }

    const headRow = document.createElement("tr");
    const firstHeadCell = document.createElement("td");
    const secondHeadCell = document.createElement("td");
    secondHeadCell.innerText = 'Revenue';
    headRow.appendChild(firstHeadCell);
    headRow.appendChild(secondHeadCell);
    table.appendChild(headRow);

    if (interval === 'monthly') {
        firstHeadCell.innerText = 'Month';

        result.forEach(e => {
            const newRow = document.createElement("tr");
            const intervalCell = document.createElement("td");
            const revenueCell = document.createElement("td");

            intervalCell.innerText = e.month;
            revenueCell.innerText = '$' + e.revenue;
            newRow.appendChild(intervalCell);
            newRow.appendChild(revenueCell);
            table.appendChild(newRow);
        });
    } else {
        firstHeadCell.innerText = 'Week';

        result.forEach(e => {
            const newRow = document.createElement("tr");
            const intervalCell = document.createElement("td");
            const revenueCell = document.createElement("td");

            intervalCell.innerText = e.week;
            revenueCell.innerText = '$' + e.revenue;
            newRow.appendChild(intervalCell);
            newRow.appendChild(revenueCell);
            table.appendChild(newRow);
        });

    }
    table.style.visibility = "visible";


}

function isDateValid(date) {
    return /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(date);

}

function isTimePeriodValid(startDate, endDate) {
    return new Date(startDate) < new Date(endDate);
}
