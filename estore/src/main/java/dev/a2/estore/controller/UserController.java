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

import dev.a2.estore.dto.SearchUsersDto;
import dev.a2.estore.dto.UserAddressDto;
import dev.a2.estore.dto.UserDetailsDto;
import dev.a2.estore.dto.UserPasswordDto;
import dev.a2.estore.dto.UserSignupDto;
import dev.a2.estore.model.User;
import dev.a2.estore.service.CategoryService;
import dev.a2.estore.service.CountryService;
import dev.a2.estore.service.RoleService;
import dev.a2.estore.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * This class provides mapping for user related requests.
 *
 * @author Andrei Sidorov
 */
@Controller
@Validated
public class UserController {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(UserController.class);

    /**
     * Injects bean UserService.
     */
    @Autowired
    private UserService userService;

    /**
     * Injects bean CountryService.
     */
    @Autowired
    private CountryService countryService;

    /**
     * Injects bean CategoryService.
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Injects bean MessageSource.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Injects bean RoleService.
     */
    @Autowired
    private RoleService roleService;

    /**
     * Generates the sign in page.
     *
     * @param error the error.
     * @param model the model.
     * @return the view name 'signin'.
     */
    @GetMapping(path = "/signin")
    public String showSigninPage(final @RequestParam(value = "error", required = false) String error,
                                 final Model model) {
        if (error != null) {
            logger.warn("User authentication failed.");
            String message = messageSource.getMessage("user.signin.failure", null, Locale.US);
            model.addAttribute("error", message);
        }
        return "signin";
    }

    /**
     * Generates the sign up page.
     *
     * @param model the model.
     * @return view name 'signup'
     */
    @GetMapping("/signup")
    public String showSignupForm(final Model model) {
        model.addAttribute("userSignupDto", new UserSignupDto());
        model.addAttribute("countries", countryService.getAllCountries());
        return "signup";
    }

    /**
     * Registers a new user.
     *
     * @param model the model.
     * @param userSignupDto - the dto with user information.
     * @param result the holder for binding validation errors.
     * @param redirectAttributes the attributes for flash messages.
     * @return either view 'signup' with error in case of failure or the redirect to '/' in case of success.
     */
    @PostMapping("/signup")
    public String signup(final Model model,
                         final @ModelAttribute("userSignupDto") @Validated UserSignupDto userSignupDto,
                         final BindingResult result,
                         final RedirectAttributes redirectAttributes) {
        logger.info("A new sign up request: " + userSignupDto);
        User existing = userService.findByEmail(userSignupDto.getEmail());

        if (existing != null) {
            logger.warn("A new user tried to sign up with existing email: " + existing.getEmail());
            String error = messageSource.getMessage("user.signup.failure", null, Locale.US);
            result.rejectValue("email", null, error);
        }

        if (result.hasErrors()) {
            model.addAttribute("countries", countryService.getAllCountries());
            model.addAttribute("categories", categoryService.getTopLevelCategories());
            return "signup";
        }
        userService.save(userSignupDto);
        logger.info("A new user has signed up with email: " + userSignupDto.getEmail());
        String message = messageSource.getMessage("user.signup.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/signin";
    }

    /**
     * Generates the user profile page.
     *
     * @param model the model.
     * @param authentication the current user credentials.
     * @return the view name 'forgot'.
     */
    @GetMapping("/profile")
    public String showProfile(final Model model,
                              final Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("userDetailsDto", new UserDetailsDto());
        model.addAttribute("userAddressDto", new UserAddressDto());
        model.addAttribute("countries", countryService.getAllCountries());
        return "profile";
    }

    /**
     * Updates a user's personal details.
     *
     * @param userDetailsDto the dto with user information.
     * @param result the holder for binding validation errors.
     * @param authentication the current user credentials.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view 'profile'.
     */
    @PostMapping("/profile/personal-details/update")
    public String updateUserDetails(final @ModelAttribute("userDetailsDto") @Validated UserDetailsDto userDetailsDto,
                                    final BindingResult result,
                                    final Authentication authentication,
                                    final RedirectAttributes redirectAttributes) {
        logger.info("A personal-details update request: " + userDetailsDto);
        User user = userService.findByEmail(authentication.getName());

        if (result.hasErrors()) {
            return "profile";
        }
        userService.updateUserDetails(user, userDetailsDto);
        logger.info(user.getEmail() + " has updated personal-details");
        String message = messageSource.getMessage("user.personal-details.update.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/profile";
    }

    /**
     * Generates the password page.
     *
     * @param model the model.
     * @return the view name 'password'.
     */
    @GetMapping("/profile/password")
    public String showPasswordPage(final Model model) {
        model.addAttribute("userPasswordDto", new UserPasswordDto());
        return "password";
    }

    /**
     * Updates a user password.
     *
     * @param userPasswordDto the dto with a new password.
     * @param result the holder for binding validation errors.
     * @param authentication the current user credentials.
     * @param redirectAttributes the attributes for flash messages.
     * @return redurects to view name 'password'.
     */
    @PostMapping("/profile/password")
    public String updateUserPassword(final @ModelAttribute("userPasswordDto") @Validated UserPasswordDto userPasswordDto,
                                     final BindingResult result,
                                     final Authentication authentication,
                                     final RedirectAttributes redirectAttributes) {
        logger.info("A password update request: " + userPasswordDto);
        User user = userService.findByEmail(authentication.getName());

        if (!userService.checkIfOldPasswordValid(user, userPasswordDto.getCurrentPassword())) {
            logger.warn("Password check failed for user: " + user.getEmail());
            String error = messageSource.getMessage("user.password.incorrect", null, Locale.US);
            result.rejectValue("currentPassword", null, error);
        }

        if (result.hasErrors()) {
            return "password";
        }
        userService.changePassword(user, userPasswordDto.getPassword());
        logger.info(user.getEmail() + " has updated his password");
        String message = messageSource.getMessage("user.password.change.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/profile/password";
    }


    /**
     * Generates the address page.
     *
     * @param model the model.
     * @return the view name 'address'.
     */
    @GetMapping("/profile/address/add")
    public String showNewAddressPage(final Model model) {
        model.addAttribute("userAddressDto", new UserAddressDto());
        model.addAttribute("countries", countryService.getAllCountries());
        return "address";
    }

    /**
     * Adds an additional address to user's profile.
     *
     * @param userAddressDto the dto with address information.
     * @param result the holder for binding validation errors.
     * @param authentication the current user credentials.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view name 'address'.
     */
    @PostMapping("/profile/address/add")
    public String addUserAddress(final @ModelAttribute("userAddressDto") @Validated UserAddressDto userAddressDto,
                                 final BindingResult result,
                                 final Authentication authentication,
                                 final RedirectAttributes redirectAttributes) {
        logger.info("An address add request: " + userAddressDto);
        User user = userService.findByEmail(authentication.getName());

        if (result.hasErrors()) {
            return "address";
        }
        userService.addUserAddress(user, userAddressDto);
        logger.info(user.getEmail() + " has added a new address");
        String message = messageSource.getMessage("user.address.add.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/profile/address/add";
    }

    /**
     * Updates a user address.
     *
     * @param userAddressDto the dto with address information.
     * @param result the holder for binding validation errors.
     * @param authentication the current user credentials.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view name 'profile'.
     */
    @PostMapping("/profile/address/update")
    public String updateUserAddress(final @ModelAttribute("userAddressDto") @Validated UserAddressDto userAddressDto,
                                    final BindingResult result,
                                    final Authentication authentication,
                                    final RedirectAttributes redirectAttributes) {
        logger.info("An address update request: " + userAddressDto);
        User user = userService.findByEmail(authentication.getName());

        if (result.hasErrors()) {
            return "profile";
        }
        userService.updateUserAddress(user, userAddressDto);
        logger.info(user.getEmail() + " has updated his address");
        String message = messageSource.getMessage("user.address.update.success", null, Locale.US);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/profile";
    }

    /**
     * Removes a user's additional address.
     *
     * @param addressId the id of the address that needs to be removed.
     * @param authentication the current user credentials.
     * @param redirectAttributes the attributes for flash messages.
     * @return redirects to view name 'profile'.
     */
    @GetMapping("/profile/address/remove/{addressId}")
    public String removeUserAddress(final @PathVariable @NotNull @Min(1) @Max(Long.MAX_VALUE) Long addressId,
                                    final Authentication authentication,
                                    final RedirectAttributes redirectAttributes) {
        logger.info("An address remove request: address id " + addressId);
        User user = userService.findByEmail(authentication.getName());

        if (user.getAddresses().size() > 1) {
            userService.removeUserAddress(user, addressId);
            logger.info(user.getEmail() + " removed one of his adresses.");
            String message = messageSource.getMessage("user.address.remove.success", null, Locale.US);
            redirectAttributes.addFlashAttribute("message", message);
        } else {
            logger.info(user.getEmail() + " tried to remove its only request.");
            String message = messageSource.getMessage("user.address.remove.failure", null, Locale.US);
            redirectAttributes.addFlashAttribute("message", message);
        }
        return "redirect:/profile";
    }

    /**
     * Genearates the top-clients page with the list of top-ten-buying-clients.
     *
     * @param model the model.
     * @return the view name 'top-products'.
     */
    @GetMapping("/reports/top-clients")
    public String getTopClients(final Model model) {
        model.addAttribute("clients", userService.getTopTenClients());
        return "top-clients";
    }

    /**
     * Generates page users for administrators.
     *
     * @param model the model
     * @return the view name 'users'.
     */
    @GetMapping("/users")
    public String showAllUsers(final Model model) {
        model.addAttribute("searchUsersDto", new SearchUsersDto());
        model.addAttribute("roles", roleService.getAllRoles());
        return "users";
    }

    /**
     * Finds users by criteria.
     *
     * @param model the model.
     * @param searchUsersDto the dto with search criteria.
     * @return view 'users' with the list of users.
     */
    @PostMapping("/users/find")
    public String findOrders(final Model model,
                             final @Validated SearchUsersDto searchUsersDto) {
        logger.info("A find users request " + searchUsersDto);
        model.addAttribute("users", userService.findUsersByCriteria(searchUsersDto));
        model.addAttribute("searchUsersDto", searchUsersDto);
        model.addAttribute("roles", roleService.getAllRoles());
        return "users";
    }

}
