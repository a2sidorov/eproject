package dev.a2.estore.service;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.a2.estore.dao.RoleDao;
import dev.a2.estore.dto.SearchUsersDto;
import dev.a2.estore.dto.UserAddressDto;
import dev.a2.estore.dto.UserDetailsDto;
import dev.a2.estore.dto.UserSignupDto;
import dev.a2.estore.model.Address;
import dev.a2.estore.model.Country;
import dev.a2.estore.model.Order;
import dev.a2.estore.model.Role;
import dev.a2.estore.model.User;
import dev.a2.estore.dao.UserDao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provides implementation for UserService interface.
 *
 * @author Andrei Sidorov
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * Initializes logger for this class.
     */
    private static final Logger logger = Logger.getLogger(UserService.class);

    /**
     * Injects UserDao.
     */
    @Autowired
    private UserDao userDao;

    /**
     * Injects BCryptPasswordEncoder.
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Injects RoleService.
     */
    @Autowired
    private RoleDao roleDao;

    /**
     * Injects OrderService.
     */
    @Autowired
    private OrderService orderService;

    /**
     * Injects CountryService.
     */
    @Autowired
    private CountryService countryService;

    @Override
    public void save(final UserSignupDto userSignupDto) {
        User newUser = new User();
        newUser.setFirstName(userSignupDto.getFirstName());
        newUser.setLastName(userSignupDto.getLastName());
        newUser.setDateOfBirth(userSignupDto.getDateOfBirth());
        newUser.setEmail(userSignupDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleDao.findByName("ROLE_CLIENT");
        roles.add(role);
        newUser.setRoles(roles);

        List<Address> addresses = new ArrayList<>();
        Address newAddress = new Address();
        Country country = countryService.findById(userSignupDto.getCountryId());
        newAddress.setCountry(country);

        newAddress.setCity(userSignupDto.getCity());
        newAddress.setPostalCode(userSignupDto.getPostalCode());
        newAddress.setStreet(userSignupDto.getStreet());
        newAddress.setHouse(userSignupDto.getHouse());
        newAddress.setApartment(userSignupDto.getApartment());
        addresses.add(newAddress);
        newUser.setAddresses(addresses);
        userDao.save(newUser);
    }

    @Override
    public User findByEmail(final String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean checkIfOldPasswordValid(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changePassword(final User user, final String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.update(user);
    }

    @Override
    public void addUserAddress(final User user, final UserAddressDto userAddressDto) {
        Address newAddress = new Address();
        Country country = countryService.findById(userAddressDto.getCountryId());
        newAddress.setCountry(country);
        newAddress.setCity(userAddressDto.getCity());
        newAddress.setPostalCode(userAddressDto.getPostalCode());
        newAddress.setStreet(userAddressDto.getStreet());
        newAddress.setHouse(userAddressDto.getHouse());
        newAddress.setApartment(userAddressDto.getApartment());
        user.getAddresses().add(newAddress);
        userDao.update(user);
    }

    @Override
    public void updateUserAddress(final User user, final UserAddressDto userAddressDto) {
        user.getAddresses().forEach(address -> {
            if (address.getId().equals(userAddressDto.getId())) {
                Country country = countryService.findById(userAddressDto.getCountryId());
                address.setCountry(country);
                address.setCity(userAddressDto.getCity());
                address.setPostalCode(userAddressDto.getPostalCode());
                address.setStreet(userAddressDto.getStreet());
                address.setHouse(userAddressDto.getHouse());
                address.setApartment(userAddressDto.getApartment());
            }
        });
        userDao.update(user);
    }

    @Override
    public void removeUserAddress(final User user, final Long addressId) {
        if (user.getAddresses().removeIf(address -> address.getId().equals(addressId))) {
            userDao.update(user);
        }
    }

    @Override
    public void updateUserDetails(final User user, final UserDetailsDto userDetailsDto) {
        user.setFirstName(userDetailsDto.getFirstName());
        user.setLastName(userDetailsDto.getLastName());
        user.setDateOfBirth(userDetailsDto.getDateOfBirth());
        userDao.update(user);
    }

    @Override
    public List<User> getTopTenClients() {
        List<User> users = userDao.getAllUsers();
        users.forEach(user -> {
            BigDecimal sum = orderService
                    .getAllUserOrders(user.getId())
                    .stream()
                    .map(Order::getTotalPurchasingPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            user.setOrdersSum(sum);
        });

        users.sort((u1, u2) -> u2.getOrdersSum().compareTo(u1.getOrdersSum()));

        if (users.size() > 10) {
            users.subList(10, users.size()).clear();
        }

        users.removeIf(user -> user.getOrdersSum().compareTo(new BigDecimal(0)) <= 0);
        return users;
    }

    @Override
    public List<User> findUsersByCriteria(final SearchUsersDto searchUsersDto) {
        Role role = roleDao.findById(searchUsersDto.getRoleId());
        List<User> users =  userDao.findUsersByCriteria(searchUsersDto);
        if (searchUsersDto.getRoleId() != 0L) {
            users.removeIf(user -> !user.getRoles().contains(role));
        }
        return users;
    }

    @Override
    public void updateUsersRoles(final Long userId, final Long roleId, final Boolean value) {
        User user = userDao.findById(userId);
        Role role = roleDao.findById(roleId);

        if (value) {
            user.getRoles().add(role);
        }

        if (!value) {
            user.getRoles().remove(role);
        }
        logger.info("User roles updated " + user + " " + user.getRoles());
    }

    @Override
    public UserDetails loadUserByUsername(final String email) {
        User user = userDao.findByEmail(email);

        if (user == null) {
            logger.warn("A user tried to sign in with not existing email: " + email);
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        logger.info(user.getEmail() + " has signed in.");
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    /**
     * Maps roles to authorities.
     *
     * @param roles the user roles.
     * @return the collection.
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
