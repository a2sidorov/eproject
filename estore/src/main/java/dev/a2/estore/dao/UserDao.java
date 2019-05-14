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

package dev.a2.estore.dao;

import dev.a2.estore.dto.SearchUsersDto;
import dev.a2.estore.model.User;

import java.util.List;

/**
 * This interface provides methods to manipulate the user entity.
 *
 * @author Andrei Sidorov
 */
public interface UserDao {

    /**
     * Saves a user.
     *
     * @param user the user that needs to be saved.
     */
    void save(User user);

    /**
     * Updates a user.
     *
     * @param user the user that needs to be updated.
     */
    void update(User user);

    /**
     * Finds a user by its email.
     *
     * @param email the email of the user that needs to be found.
     * @return a user.
     */
    User findByEmail(String email);

    /**
     * Finds a user by its id.
     *
     * @param id the id of the user that needs to be found.
     * @return a user.
     */
    User findById(Long id);

    /**
     * Finds all users.
     *
     * @return the list of all users.
     */
    List<User> getAllUsers();

    /**
     * Finds users by criteria.
     *
     * @param searchUsersDto the dto with criteria.
     * @return the list of users.
     */
    List<User> findUsersByCriteria(SearchUsersDto searchUsersDto);

}
