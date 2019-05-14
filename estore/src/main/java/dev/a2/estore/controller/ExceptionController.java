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

package dev.a2.estore.controller;

import dev.a2.estore.exception.ProductNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * This class intercepts exceptions in the specified controller classes and handle them.
 *
 * @author Andrei Sidorov
 */
@ControllerAdvice(assignableTypes = { UserController.class, ProductController.class, OrderController.class })
public class ExceptionController {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ExceptionController.class);

    /**
     * Injects the maximum image size specified in the application properties.
     */
    @Value("${max.image.size}")
    private long maxImageSize;

    /**
     * Intercepts the product not found exception and returns view 'error-404'.
     *
     * @param request the http request.
     * @param e the product not found exception.
     * @return the view name.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFound(final HttpServletRequest request, final Exception e)   {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return "error-404";
    }

    /**
     * Intercepts the constraint violation exceptions and returns view 'error-400'.
     *
     * @param request the http request.
     * @param e the product not found exception.
     * @return the view name 'error-400'.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolation(final HttpServletRequest request, final ConstraintViolationException e)   {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return "error-400";
    }

    /**
     * Intercepts the missing servlet request parameter exception and returns view 'error-400'.
     *
     * @param request the http request.
     * @param e the missing servlet request parameter exception.
     * @return the view name 'error-400'.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingRequestParameter(final HttpServletRequest request, final Exception e)   {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return "error-400";
    }

    /**
     * Intercepts the method argument type mismatch exception and returns view 'error-400'.
     *
     * @param request the http request.
     * @param e the method argument type mismatch exception.
     * @return the view name 'error-400'.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleArgumentMismatch(final HttpServletRequest request, final MethodArgumentTypeMismatchException e)   {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return "error-400";
    }

    /**
     * Intercepts the maximum uploaded size exceeded exception and returns the flash message
     * to the page.
     *
     * @param request the http request.
     * @param e the maximum uploaded size exceeded exception
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects back to the page.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxFileSize(final HttpServletRequest request,
                               final MaxUploadSizeExceededException e,
                               final RedirectAttributes redirectAttributes) {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        redirectAttributes.addFlashAttribute("error",
                "The size of an uploaded file must be less than " + (maxImageSize/1000) + " kilobytes");
        return "redirect:" + request.getServletPath();

    }

    /**
     * Intercepts the other exceptions and returns view 'error-500'.
     *
     * @param request the http request.
     * @param e the exception
     * @return the view name 'error-500'.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleError(final HttpServletRequest request, final Exception e)   {
        logger.error("Request: " + request.getRequestURL() + " raised " + e);
        return "error-500";
    }



}
