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

package dev.a2.estore.controller;

import dev.a2.estore.exception.CategoryDeleteException;
import dev.a2.estore.exception.MeasureUnitsDeleteException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * This class intercepts exceptions in the specified rest controller classes and handle them.
 *
 * @author Andrei Sidorov
 */
@RestControllerAdvice(assignableTypes = { ProductRestController.class,
                                          OrderRestController.class,
                                          UserRestController.class})
public class ExceptionRestController {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ExceptionRestController.class);

    /**
     * Intercepts the method argument not valid exception and returns response with status 404.
     *
     * @param request the http request.
     * @param e the method argument not valid exception.
     * @return the response entity with error message and status 404.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,
                       MethodArgumentTypeMismatchException.class,
                       ConstraintViolationException.class,
                       MissingServletRequestParameterException.class,
                       CategoryDeleteException.class,
                       MeasureUnitsDeleteException.class})
    public ResponseEntity<String> handleMethodArgumentNotValid(final HttpServletRequest request, final Exception e)   {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Intercepts the other exceptions and generates response with status 500.
     *
     * @param request the http request.
     * @param e the exception.
     * @return the response entity with error message and status 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleError(final HttpServletRequest request, final Exception e)   {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
