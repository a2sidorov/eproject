package dev.a2.estore.service;

import dev.a2.estore.dao.MeasureUnitsDao;
import dev.a2.estore.model.MeasureUnits;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Testing MeasureUnitsService")
@ExtendWith(MockitoExtension.class)
class MeasureUnitsServiceTest {

    @Mock
    private MeasureUnitsDao measureUnitsDao;

    @InjectMocks
    private MeasureUnitsService measureUnitsService = new MeasureUnitsServiceImpl();

    @Nested
    @DisplayName("Testing save method")
    class saveTest {
        @DisplayName("when this method is called then new measure units are created and saved")
        @Test
        void saveTest1() {
            // given
            String measureUnitsName = "measure units name";

            // run
            measureUnitsService.save(measureUnitsName);

            // assert
            verify(measureUnitsDao, times(1)).save(any(MeasureUnits.class));
        }
    }

    @Nested
    @DisplayName("Testing getAllMeasureUnits method")
    class getAllMeasureUnitsTest {
        @DisplayName("when this method is called then method measureUnitsDao.getAllMeasureUnits() gets called")
        @Test
        void findByIdTest1() {
            // given

            // run
            measureUnitsService.getAllMeasureUnits();

            // assert
            verify(measureUnitsDao, times(1)).getAllMeasureUnits();
        }
    }

    @Nested
    @DisplayName("Testing findById method")
    class findByIdTest {
        @DisplayName("when this method is called then method measureUnitsDao.findById() gets called")
        @Test
        void findByIdTest1() {
            // given
            Long measureUnitsId = 1L;

            // run
            measureUnitsService.findById(measureUnitsId);

            // assert
            verify(measureUnitsDao, times(1)).findById(measureUnitsId);
        }
    }
}

