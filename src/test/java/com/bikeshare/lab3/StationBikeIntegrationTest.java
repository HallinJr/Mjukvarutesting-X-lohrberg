package com.bikeshare.lab3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.bikeshare.model.Bike;
import com.bikeshare.model.Station;

public class StationBikeIntegrationTest {


    @Nested
    class FunctionalTests {

        Station station;
        Bike testbike;
        Bike testBikeElectric;
        

        @BeforeEach
        void setup() {
            station = new Station("S01", "Central", "Street 1", 1.0, 1.0, 2);
            testbike = new Bike("A123", Bike.BikeType.STANDARD);
            testBikeElectric = new Bike("B123", Bike.BikeType.ELECTRIC);

        }

        @Test
        void testAddBikeAndSetsBikeStationStatus() {

            assertEquals(0, station.getTotalBikeCount());
            station.addBike(testBikeElectric);
            station.addBike(testbike);

            assertEquals(2, station.getTotalBikeCount());
            assertEquals("S01", station.getStationId());
            assertTrue(station.getAvailableBikeCount() >= 0);
            assertTrue(station.isFull());

            station.removeBike("B123");
            assertFalse(station.isFull());

        }

        @Test
        void testChargeElectricBike() {

            testBikeElectric.startRide();
            testBikeElectric.endRide(10);
            Double beforeCharge = testBikeElectric.getBatteryLevel();

            station.addBike(testBikeElectric);
            station.addBike(testbike);

            station.enableCharging(1);
            station.chargeElectricBikes(5.0);
            Double afterCharge = testBikeElectric.getBatteryLevel();

            assertTrue(afterCharge > beforeCharge);

            
        }

    }
    
}
