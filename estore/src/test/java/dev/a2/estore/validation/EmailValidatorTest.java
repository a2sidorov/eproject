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

package dev.a2.estore.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testing EmailValidator class")
public class EmailValidatorTest {

    private static EmailValidator emailValidator;

    @BeforeAll
    static void initAll() {
        emailValidator = new EmailValidator();
    }

    @DisplayName("when email empty then return false")
    @Test
    void isValidTest1() {
        //given
        String input = "";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when email has no @ character then return false")
    @Test
    void isValidTest2() {
        //given
        String input = "Abc.example.com";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when email has more than one @ character then return false")
    @Test
    void isValidTest3() {
        //given
        String input = "A@b@c@example.com";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when email has special outside quotation marks then return false")
    @Test
    void isValidTest4() {
        //given
        String input = "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when email has quoted strings without dot separation then return false")
    @Test
    void isValidTest5() {
        //given
        String input = "just\"not\"right@example.com";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when email has escaped spaces, quotes, and backslashes outside quotation marks then return false")
    @Test
    void isValidTest6() {
        //given
        String input = "this\\ still\\\"not\\\\allowed@example.com";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when email has double dot before @ then return false")
    @Test
    void isValidTest7() {
        //given
        String input = "ohn..doe@example.com";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when email has double dot after @ then return false")
    @Test
    void isValidTest8() {
        //given
        String input = "john.doe@example..com";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when email is valid then return true")
    @Test
    void isValidTest9() {
        //given
        String input = "foo@bar.com";

        //run
        boolean result = emailValidator.isValid(input, null);

        //assert
        assertTrue(result);
    }

}