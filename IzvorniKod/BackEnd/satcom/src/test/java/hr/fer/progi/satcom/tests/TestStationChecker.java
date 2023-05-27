package hr.fer.progi.satcom.tests;
import static org.junit.jupiter.api.Assertions.*;
import hr.fer.progi.satcom.pullingFromApi.StationChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestStationChecker {
    @Test
    @DisplayName("Test checker for identical stations")
    void testStationCheckerTrue(){
        boolean result=StationChecker.checkStations((long) 6);
        assertTrue(result);

    }
    @Test
    @DisplayName("Test checker for stations with difference in station data")
    void testStationCheckerFalseStation(){
        //pobrinuti se da prije testiranja barem jedan podatak o ovoj stanici je krivi u bazi
        boolean result=StationChecker.checkStations((long) 9);
        assertFalse(result);
    }
    @Test
    @DisplayName("Test checker for stations with difference in antenna data")
    void testStationCheckerFalseAntenna(){
        //pobrinuti se da prije testiranja barem jedan podatak o antenama ove stanice krivi
        boolean result=StationChecker.checkStations((long) 12);
        assertFalse(result);
    }
}
