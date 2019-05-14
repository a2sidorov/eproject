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

package dev.a2.estore.dao;

import dev.a2.estore.model.Role;

import java.util.List;

/**
 * This interface provides methods to manipulate the role entity.
 *
 * @author Andrei Sidorov
 */
public interface RoleDao {


    /**
     * Saves a user role.
     *
     * @param role the user role that needs to be saved.
     */
    void save(Role role);

    /**
     * Finds a user role by its name.
     *
     * @param roleName the name of the role that needs to be found.
     * @return the role.
     */
    Role findByName(String roleName);

    /**
     * Finds a role by its id.
     * @param roleId the id of the role that needs to be found.
     * @return a user role.
     */
    Role findById(Long roleId);

    /**
     * Finds all roles.
     *
     * @return the list of all roles.
     */
    List<Role> getAllRoles();

}
