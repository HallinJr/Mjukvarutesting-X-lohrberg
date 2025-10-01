package com.bikeshare.lab3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.bikeshare.model.Bike;
import com.bikeshare.model.Station;

import java.util.Arrays;
import java.util.Collection;

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
        void testStationStatus() {

            assertEquals(0, station.getTotalBikeCount());

            station.addBike(testBikeElectric);
            station.addBike(testbike);

            assertEquals(2, station.getTotalBikeCount());
            assertTrue(station.isFull());
            station.removeBike(testBikeElectric.getBikeId());
            assertFalse(station.isFull());

        }

        @Test
        void testRemoveBikeFromStation() {
            station.addBike(testbike);
            assertEquals("S01", testbike.getCurrentStationId());
            assertEquals(1, station.getTotalBikeCount());
            station.removeBike(testbike.getBikeId());
            assertNull(testbike.getCurrentStationId());

        }

        @Test
        void testBikeIsAtCorrectStation() {
            station.addBike(testBikeElectric);
            station.addBike(testbike);

            assertEquals("S01", testBikeElectric.getCurrentStationId());
            assertEquals("S01", testbike.getCurrentStationId());
        }

        @Test
        void testChargeElectricBike() {

            testBikeElectric.startRide();
            testBikeElectric.endRide(10);
            Double beforeCharge = testBikeElectric.getBatteryLevel();

            station.addBike(testBikeElectric);

            assertThrows(IllegalStateException.class, () -> station.chargeElectricBikes(5));

            station.enableCharging(1);
            station.chargeElectricBikes(5.0);
            Double afterCharge = testBikeElectric.getBatteryLevel();

            assertTrue(afterCharge > beforeCharge);

            
        }

        @Test
        void testCantAddBikeWhenStationIsFull()
        {
            station.addBike(testbike);
            station.addBike(testBikeElectric);
            Bike anotherBike = new Bike("C123", Bike.BikeType.PREMIUM);

            assertThrows(IllegalStateException.class, () -> station.addBike(anotherBike));

        }

        @Test
        void testCantRemoveBikeThatsNotPresentOrIsReserved() {
            station.addBike(testbike);
            assertThrows(IllegalStateException.class, () -> station.removeBike("Wrong Id"));

            station.reserveBike("A123");
            assertEquals(Bike.BikeStatus.RESERVED, testbike.getStatus());
            assertThrows(IllegalStateException.class, () -> station.removeBike("A123"));
        }

        @Test
        void testAddBikeToInactiveStation() {
            station.deactivate();
            assertThrows(IllegalStateException.class, () -> station.addBike(testbike));
        }



    }
    
}
