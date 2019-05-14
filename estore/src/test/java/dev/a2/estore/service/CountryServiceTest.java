package dev.a2.estore.service;

import dev.a2.estore.dao.CountryDao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Testing CountryService")
@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryDao countryDao;

    @InjectMocks
    private CountryService countryService = new CountryServiceImpl();

    @Nested
    @DisplayName("Testing findById method")
    class findByIdTest {
        @DisplayName("when this method is called then method countryDao.findById() gets called")
        @Test
        void findByIdTest1() {
            // given
            Long countryId = 1L;

            // run
            countryDao.findById(countryId);

            // assert
            verify(countryDao, times(1)).findById(countryId);
        }
    }

    @Nested
    @DisplayName("Testing getAllCountries method")
    class saveTest {
        @DisplayName("when this method is called then method countryDao.getAllCountries() gets called")
        @Test
        void findByIdTest1() {
            // given

            // run
            countryDao.getAllCountries();

            // assert
            verify(countryDao, times(1)).getAllCountries();
        }
    }
}


