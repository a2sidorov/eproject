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

package dev.a2.estore.dto;

import dev.a2.estore.validation.FieldMatch;
import dev.a2.estore.validation.ValidPassword;
import org.hibernate.validator.constraints.NotBlank;

/**
 * This class provides the dto of a user password.
 *
 * @author Andrei Sidorov
 */
@FieldMatch(field = "password", fieldMatch = "confirmPassword")
public class UserPasswordDto {

    /**
     * The current password.
     */
    @NotBlank
    private String currentPassword;

    /**
     * The new password.
     */
    @NotBlank
    @ValidPassword
    private String password;

    /**
     * The confirm password.
     */
    private String confirmPassword;

    /* Getters and setters */

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "UserPasswordDto{" +
                "currentPassword='" + "******" + '\'' +
                ", password='" + "******" + '\'' +
                ", confirmPassword='" + "******" + '\'' +
                '}';
    }
}


