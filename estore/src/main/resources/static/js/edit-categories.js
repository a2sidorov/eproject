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

async function addFolder(parentId) {
    const folderName = prompt("Please enter folder name");

    if (folderName === null) {
        return;
    }

    if (!isValid(folderName.trim())) {
        alert("Invalid folder name");
        return;
    }

    const settings = {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            parentCategoryId: parentId,
            name: folderName,
            type: 'folder',
        })
    };
    const response = await fetch('/category/create', settings);

    if (response.status === 200) {
        window.location.reload();
    }
}

async function addCategory(parentId) {

    const categoryName = prompt("Please enter category name");

    if (categoryName === null) {
        return;
    }

    if (!isValid(categoryName.trim())) {
        alert("Invalid category name");
        return;
    }

    const settings = {
        method: 'POST',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            parentCategoryId: parentId,
            name: categoryName,
            type: 'category',
        })
    };
    const response = await fetch('/category/create', settings);

    if (response.status === 200) {
        window.location.reload();
    }
}

async function deleteCategory(categoryId, categoryName) {
    console.log(categoryId);

    const answer = confirm(`Are sure you want to remove category ${categoryName}?`);

    if (!answer) {
        return
    }

    const settings = {
        method: 'DELETE',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
    };
    const response = await fetch(`/category?id=${categoryId}`, settings);

    if (response.status === 200) {
        window.location.reload();
    }
}

async function deleteFolder(folderId, folderName) {
    const answer = confirm(`Are sure you want to remove category ${folderName}? All subfolders and subcategories will also be removed.`);

    if (!answer) {
        return
    }
    const settings = {
        method: 'DELETE',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
    };
    const response = await fetch(`/category?id=${folderId}`, settings);

    if (response.status === 200) {
        window.location.reload();
    }

    if (response.status === 400) {
        document.getElementById("error").innerText = await response.text();
    }
}

async function renameFolder(categoryId) {
    const newFolderName = prompt("Please enter a new folder name.");

    if (newFolderName === null) {
        return;
    }

    if (!isValid(newFolderName.trim())) {
        alert("Invalid folder name");
        return;
    }

    const settings = {
        method: 'PUT',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
    };
    const response = await fetch(`/category?id=${categoryId}&name=${newFolderName}`, settings);

    if (response.status === 200) {
        window.location.reload();
    }
}


function isValid(category) {
    return /^.{1,35}$/.test(category);
}

