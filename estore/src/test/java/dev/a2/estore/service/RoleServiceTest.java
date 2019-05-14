package dev.a2.estore.service;

import dev.a2.estore.dao.RoleDao;
import dev.a2.estore.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Testing RoleService")
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleService roleService = new RoleServiceImpl();

    @Nested
    @DisplayName("Testing save method")
    class saveTest {
        @DisplayName("when this method is called then method roleDao.save() gets called")
        @Test
        void saveTest1() {
            // given
            Role role = new Role();

            // run
            roleService.save(role);

            // assert
            verify(roleDao, times(1)).save(role);
        }
    }

    @Nested
    @DisplayName("Testing findByName method")
    class findByName {
        @DisplayName("when this method is called then methid roleDao.findByName() gets called")
        @Test
        void findByIdTest1() {
            // given
            String roleName = "roleName";

            // run
            roleService.findByName(roleName);

            // assert
            verify(roleDao, times(1)).findByName(roleName);
        }

    }
}

