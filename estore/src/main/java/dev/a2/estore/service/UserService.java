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

package dev.a2.estore.service;

import java.util.List;

import dev.a2.estore.dto.SearchUsersDto;
import dev.a2.estore.dto.UserAddressDto;
import dev.a2.estore.dto.UserDetailsDto;
import dev.a2.estore.dto.UserSignupDto;
import dev.a2.estore.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * This interface provides methods to manage users.
 *
 * @author Andrei Sidorov
 */
public interface UserService extends UserDetailsService {

    /**
     * Creates and saves a user.
     *
     * @param userDto the dto with user information.
     */
    void save(UserSignupDto userDto);

    /**
     * Finds a user by its email.
     *
     * @param email the email of the user that needs to be found.
     * @return a user.
     */
    User findByEmail(String email);

    /**
     * Checks if a provided password is valid.
     *
     * @param user the existing user.
     * @param password the password that needs to be checked.
     * @return true if password is valid and false it is invalid.
     */
    boolean checkIfOldPasswordValid(User user, String password);

    /**
     * Changes a user password.
     *
     * @param user the user whose password needs to be changed.
     * @param password a new password.
     */
    void changePassword(User user, String password);

    /**
     * Adds a user address.
     *
     * @param user a user.
     * @param userAddressDto the dto with address information.
     */
    void addUserAddress(User user, UserAddressDto userAddressDto);

    /**
     * Updates a user address.
     *
     * @param user a user whose address needs to be updated.
     * @param userAddressDto the dto with new address information.
     */
    void updateUserAddress(User user, UserAddressDto userAddressDto);

    /**
     * Removes a user address.
     *
     * @param user a user whose address needs to be removed.
     * @param addressId the id of the address that needs to be removed.
     */
    void removeUserAddress(User user, Long addressId);

    /**
     * Updates user personal details.
     *
     * @param user the user whose personal details need to be updated.
     * @param userDetailsDto the dto with user's updated personal-details.
     */
    void updateUserDetails(User user, UserDetailsDto userDetailsDto);

    /**
     * Calculates top-ten clients based on the sum of the total price of their orders.
     *
     * @return the list of users.
     */
    List<User> getTopTenClients();

    /**
     * Finds users by criteria.
     *
     * @param searchUsersDto the dto with search criteria.
     * @return the list of users.
     */
    List<User> findUsersByCriteria(SearchUsersDto searchUsersDto);

    /**
     * Updates user roles.
     *
     * @param userId the id of a user whose roles need to be updated.
     * @param roleId the id of a role that needs to add or remove from a user.
     * @param value a boolean value, true to add a role and false to remove a role from a user.
     */
    void updateUsersRoles(Long userId, Long roleId, Boolean value);

}
