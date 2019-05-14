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

@DisplayName("Testing PasswordValidator class")
public class PasswordValidatorTest {

    private static PasswordValidator passwordValidator;

    @BeforeAll
    static void initAll() {
        passwordValidator = new PasswordValidator();
    }

    @DisplayName("when password empty then return false")
    @Test
    void isValidTest1() {
        //given
        String input = "";

        //run
        boolean result = passwordValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when password has no upercase characters then return false")
    @Test
    void isValidTest2() {
        //given
        String input = "password";

        //run
        boolean result = passwordValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when password has no lowercase characters then return false")
    @Test
    void isValidTest3() {
        //given
        String input = "PASSWORD";

        //run
        boolean result = passwordValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when password has no digit then return false")
    @Test
    void isValidTest4() {
        //given
        String input = "Password";

        //run
        boolean result = passwordValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

    @DisplayName("when password has only white space then return false")
    @Test
    void isValidTest5() {
        //given
        String input = " ";

        //run
        boolean result = passwordValidator.isValid(input, null);

        //assert
        assertFalse(result);
    }

}
