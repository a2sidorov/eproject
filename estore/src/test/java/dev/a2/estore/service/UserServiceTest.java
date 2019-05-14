package dev.a2.estore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.a2.estore.dao.RoleDao;
import dev.a2.estore.dao.UserDao;
import dev.a2.estore.dto.UserAddressDto;
import dev.a2.estore.dto.UserDetailsDto;
import dev.a2.estore.dto.UserSignupDto;

import dev.a2.estore.model.Address;
import dev.a2.estore.model.Role;
import dev.a2.estore.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DisplayName("Testing UserService")
//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private OrderService orderService;

    @Mock
    private CountryService countryService;
    @Mock
    private UserDao userDao;

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Nested
    @DisplayName("Testing save method")
    class saveTest {
        @DisplayName("when this method is called then a user is created and saved")
        @Test
        void saveTest1() {
            // given
            UserSignupDto userSignupDto = new UserSignupDto();

            // run
            userService.save(userSignupDto);

            // assert
            verify(userDao, times(1)).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Testing findByEmail method")
    class findByEmailTest {
        @DisplayName("when a user is found then this user is returned")
        @Test
        void findByEmailTest1() {
            // given
            User user = new User();
            String email = "client@mail.dev";
            when(userDao.findByEmail(email)).thenReturn(user);

            // run
            User result = userService.findByEmail(email);

            // assert
            assertNotNull(result);
            verify(userDao, times(1)).findByEmail(email);
        }

        @DisplayName("when a user is not found then null is returned")
        @Test
        void findByEmailTest2() {
            // given
            String email = "client@mail.dev";
            when(userDao.findByEmail(email)).thenReturn(null);

            // run
            User result = userService.findByEmail(email);

            // assert
            assertNull(result);
            verify(userDao, times(1)).findByEmail(email);
        }
    }

    @Nested
    @DisplayName("Testing checkIfOldPasswordValid method")
    class checkIfOldPasswordValidTest {
        @DisplayName("when a password is valid then true is returned")
        @Test
        void checkIfOldPasswordValidTest1() {
            // given
            User mockedUser = mock(User.class);
            when(mockedUser.getPassword()).thenReturn("Password1");
            when(passwordEncoder.matches("Password1", "Password1")).thenReturn(true);
            String validPassword = "Password1";

            // run
            boolean result = userService.checkIfOldPasswordValid(mockedUser, validPassword);

            // assert
            assertTrue(result);
            verify(passwordEncoder, times(1)).matches(validPassword, mockedUser.getPassword());
        }

        @DisplayName("when a password is invalid then false is returned")
        @Test
        void checkIfOldPasswordValidTest2() {
            // given
            User mockedUser = mock(User.class);
            when(mockedUser.getPassword()).thenReturn("Password1");
            when(passwordEncoder.matches("Password2", "Password1")).thenReturn(false);
            String invalidPassword = "Password2";

            // run
            boolean result = userService.checkIfOldPasswordValid(mockedUser, invalidPassword);

            // assert
            assertFalse(result);
            verify(passwordEncoder, times(1)).matches(invalidPassword, mockedUser.getPassword());
        }
    }

    @Nested
    @DisplayName("Testing changePassword method")
    class changePasswordTest {
        @DisplayName("when this method is called then a password is changed")
        @Test
        void changePasswordTest1() {
            // given
            User user = new User();
            user.setPassword("oldPassword");
            String newPassword = "newPassword";
            when(passwordEncoder.encode(newPassword)).thenReturn("newPassword");

            // run
            userService.changePassword(user, newPassword);

            // assert
            assertEquals(newPassword, user.getPassword());
            verify(userDao, times(1)).update(user);
        }

    }

    @Nested
    @DisplayName("Testing addUserAddress method")
    class addUserAddressTest {
        @DisplayName("when this method is called then a new address is added")
        @Test
        void addUserAddressTest1() {
            // given
            User user = new User();
            List<Address> addresses = new ArrayList<>();
            user.setAddresses(addresses);
            UserAddressDto userAddressDto = new UserAddressDto();

            // run
            userService.addUserAddress(user, userAddressDto);

            // assert
            assertEquals(user.getAddresses().size(), 1);
            verify(userDao, times(1)).update(user);
        }
    }

    @Nested
    @DisplayName("Testing updateUserAddress method")
    class updateUserAddressTest {

        @DisplayName("when this method is called then an address is updated")
        @Test
        void updateUserAddress() {
            // given
            User user = new User();
            Address address = new Address();
            address.setId(1L);
            List<Address> addresses = new ArrayList<>();
            addresses.add(address);
            user.setAddresses(addresses);

            UserAddressDto userAddressDto = new UserAddressDto();
            userAddressDto.setId(1L);
            userAddressDto.setCountryId(1L);
            userAddressDto.setCity("anotherCity");
            userAddressDto.setPostalCode("postal code");
            userAddressDto.setStreet("street name");
            userAddressDto.setHouse("1");
            userAddressDto.setApartment("1");

            // run
            userService.updateUserAddress(user, userAddressDto);

            // assert
            assertEquals("anotherCity", user.getAddresses().get(0).getCity());
            verify(userDao, times(1)).update(user);
        }
    }

    @Nested
    @DisplayName("Testing removeUserAddress method")
    class removeUserAddressTest {

        @DisplayName("when this method is called then an address is removed")
        @Test
        void removeUserAddressTest1() {
            // given
            Address address1 = new Address();
            address1.setId(1L);
            Address address2 = new Address();
            address2.setId(2L);
            User user = new User();
            List<Address> addresses = new ArrayList<>();
            addresses.add(address1);
            addresses.add(address2);
            user.setAddresses(addresses);

            // run
            userService.removeUserAddress(user, 1L);

            // assert
            assertEquals(user.getAddresses().size(), 1);
            verify(userDao, times(1)).update(user);
        }

    }

    @Nested
    @DisplayName("Testing updateUserDetails method")
    class updateUserDetailsTest {
        @DisplayName("when this method is called then a user's details are updated")
        @Test
        void updateUserDetailsTest1() {
            // given
            User user = new User();
            UserDetailsDto userDetailsDto = new UserDetailsDto();
            userDetailsDto.setFirstName("newFirstname");

            // run
            userService.updateUserDetails(user, userDetailsDto);

            // assert
            assertEquals("newFirstname", user.getFirstName());
            verify(userDao, times(1)).update(user);

        }
    }

    @Nested
    @DisplayName("Testing getTopClients method")
    class getTopClientsTest {
        @DisplayName("when this method is called " +
                "then the list of top clients is returned, limited to a specified number")
        @Test
        void getTopClientsTest1() {
            // given
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                User user = mock(User.class);
                doNothing().when(user).setOrdersSum(any());
                when(user.getOrdersSum()).thenReturn(new BigDecimal(20));
                users.add(user);
            }
            when(userDao.getAllUsers()).thenReturn(users);

            // run
            List<User> result = userService.getTopTenClients();

            // assert
            assertEquals(10, result.size());
        }

        @DisplayName("when clients have no purchases then those clients are ignored")
        @Test
        void getTopClientsTest3() {
            // given
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                User user = mock(User.class);
                doNothing().when(user).setOrdersSum(any());
                when(user.getOrdersSum()).thenReturn(new BigDecimal(0));
                users.add(user);
            }
            when(userDao.getAllUsers()).thenReturn(users);

            // run
            List<User> result = userService.getTopTenClients();

            // assert
            assertEquals(0, result.size());
        }

        @DisplayName("when there are no clients then an empty list is returned")
        @Test
        void getTopClientsTest4() {
            // given
            when(userDao.getAllUsers()).thenReturn(new ArrayList<>());

            // run
            List<User> result = userService.getTopTenClients();

            // assert
            assertEquals(0, result.size());
        }

    }

    @Nested
    @DisplayName("Testing loadUserByUsername method")
    class loadUserByUsernameTest {
        @DisplayName("when a user is found then this user is returned")
        @Test
        void loadUserByUsernameTest1() {
            //given
            String email = "client@mail.dev";
            User user = new User();
            user.setEmail(email);
            user.setPassword("Password1");
            Set<Role> roles = new HashSet<>();
            Role role = new Role();
            role.setName("ROLE_CLIENT");
            roles.add(role);
            user.setRoles(roles);
            when(userDao.findByEmail(email)).thenReturn(user);

            // run
            UserDetails userDetails = userService.loadUserByUsername(email);

            // assert
            assertEquals(email, userDetails.getUsername());

        }

        @DisplayName("when a user is not found then exception UsernameNotFoundException is thrown")
        @Test
        void loadUserByUsernameTest2() {
            //given
            String email = "client@mail.dev";
            when(userDao.findByEmail(email)).thenReturn(null);

            // run and assert
            assertThrows(UsernameNotFoundException.class, () -> {
                userService.loadUserByUsername(email);
            });

        }

    }

}

