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

import dev.a2.estore.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * This class provides rest mapping for user related requests.
 *
 * @author Andrei Sidorov
 */
@RestController
@Validated
public class UserRestController {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(UserRestController.class);

    /**
     * Injects bean UserService.
     */
    @Autowired
    private UserService userService;

    /**
     * Updates user roles.
     *
     * @param userId the id of a user whose roles need to be updated.
     * @param roleId the id of a role that needs to add or remove from a user.
     * @param value a boolean value, true to add a role and false to remove a role from a user.
     */
    @PutMapping("/users")
    public void updateUserRoles(final @RequestParam("userId") @Min(0) @Max(Long.MAX_VALUE) Long userId,
                                final @RequestParam("roleId") @Min(0) @Max(Long.MAX_VALUE) Long roleId,
                                final @RequestParam("value") Boolean value) {
        logger.info("A user role update request, " +
                "user id '" + userId + "' role id '" + roleId + "' value '" + value);
        userService.updateUsersRoles(userId, roleId, value);
    }

}
