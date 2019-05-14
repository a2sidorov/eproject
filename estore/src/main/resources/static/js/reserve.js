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

function startTimer(checkoutReserveTimeString, secondsLeftString) {
    const timer = document.getElementById("timer");
    const checkoutReserveTime = parseInt(checkoutReserveTimeString);
    let secondsLeft = parseInt(secondsLeftString);
    secondsLeft = checkoutReserveTime - secondsLeft;
    const interval = setInterval(function() {
        if (secondsLeft < 1) {
            timer.innerText = '00:00';
            timer.style.color = 'red';
            clearInterval(interval);
            return;
        }
        secondsLeft--;
        let minutes = Math.floor(secondsLeft / 60);
        let seconds = secondsLeft % 60;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        seconds = seconds < 10 ? '0' + seconds : seconds;
        timer.innerText = minutes + ':' + seconds;
    }, 1000);
}

