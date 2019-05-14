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
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

async function createMeasureUnits() {
    const measureUnitsName = prompt("Please enter measure units name");

    if (measureUnitsName === null) {
        return;
    }
    if (!isValid(measureUnitsName.trim())) {
        alert("Invalid measure units name");
        return;
    }
    const response = await fetch(`/product/measure-units/create?name=${measureUnitsName}`);

    if (response.status === 200) {
        window.location.reload();
    }
}

async function renameMeasureUnits(measureUnitsId) {
    const measureUnitsNewName = prompt("Please enter new name.");

    if (measureUnitsNewName === null) {
        return;
    }

    if (!isValid(measureUnitsNewName.trim())) {
        alert("Invalid measure units name");
        return;
    }

    const settings = { method: 'PUT' };
    const response = await fetch(`/product/measure-units/rename?id=${measureUnitsId}&name=${measureUnitsNewName}`, settings);

    if (response.status === 200) {
        window.location.reload();
    }

    if (response.status === 400) {
        document.getElementById("error").innerText = await response.text();
    }
}

async function deleteMeasureUnits(measureUnitsId) {
    const settings = { method: 'DELETE' };
    const response = await fetch(`/product/measure-units/delete?id=${measureUnitsId}`, settings);

    if (response.status === 200) {
        window.location.reload();
    }

    if (response.status === 400) {
        document.getElementById("error").innerText = await response.text();
    }
}

function isValid(measureUnitsName) {
    return /^[A-Za-z]{1,35}$/.test(measureUnitsName);
}

