package com.bikeshare.lab3;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.bikeshare.model.Bike;

public class BikeStructuralTest {



    @Nested
    class FunctionalTests {
        Bike testBike;

        @BeforeEach
            public void setUp() {
                testBike = new Bike("B123", Bike.BikeType.ELECTRIC);
            }


        @Test
        void testStartRide() {

            testBike.startRide();

            assertTrue(testBike.getStatus() == Bike.BikeStatus.IN_USE);
        }

        @Test
        void testStartRideWithBikeInUse() {
            testBike.reserve();
            testBike.startRide();

            assertThrows(IllegalStateException.class, () -> testBike.startRide());
        }

        @Test
        void testStartElectricRideWithLowBatteryLevel() {

            testBike.startRide();
            testBike.endRide(45.00000000000001);

            assertThrows(IllegalStateException.class, () -> testBike.startRide(), "Percent left in bike: " + testBike.getBatteryLevel());
        }

        @Test
        void testEndRide() {

            assertThrows(IllegalStateException.class, () -> testBike.endRide(10));
        }

        @Test
        void testEndRideWithNegativeDistanceTraveled() {
            testBike.startRide();

            assertThrows(IllegalArgumentException.class, () -> testBike.endRide(-1));
        }

        @Test
        void testSendToMaintenanceWhileBikeInUse() {
            testBike.startRide();

            assertThrows(IllegalStateException.class, () -> testBike.sendToMaintenance());
        }

        @Test
        void testSendToMaintenance() {
            testBike.sendToMaintenance();

            assertTrue(testBike.getStatus() == Bike.BikeStatus.MAINTENANCE);
        }

        @Test
        void testCheckMaintenanceRequiredForBatteryLevel() {
            testBike.startRide();
            testBike.endRide(48);
            assertTrue(testBike.needsMaintenance());
        }

        @Test
        void testCheckMaintenanceRequiredForDistance() {
            Bike nonElectricBike = new Bike("B123", Bike.BikeType.STANDARD);

            nonElectricBike.startRide();
            nonElectricBike.endRide(1000);
            assertTrue(nonElectricBike.needsMaintenance());
        }

        @Test
        void test100XRidesOnNonElectricBikeNeedsMaintenance() {
            Bike nonElectricBike = new Bike("B123", Bike.BikeType.PREMIUM);

            for (int i = 0; i <100; i++) {
                nonElectricBike.startRide();
                nonElectricBike.endRide(1);
            }

            assertTrue(nonElectricBike.needsMaintenance());
        }

        @Test
        void testChargeNonElectricBike() {
            Bike nonElectricBike = new Bike("B123", Bike.BikeType.PREMIUM);

            assertThrows(IllegalStateException.class, () -> nonElectricBike.chargeBattery(10));
        }

        @Test
        void testChargeElectricBike() {

            assertThrows(IllegalArgumentException.class, () -> testBike.chargeBattery(-1));
            assertThrows(IllegalArgumentException.class, () -> testBike.chargeBattery(101));
        }

        @Test
        void testChargeBike() {
            testBike.startRide();
            testBike.endRide(1);

            double prevBatteryLevel = testBike.getBatteryLevel();
            testBike.chargeBattery(2);

            assertTrue(prevBatteryLevel + 2 == testBike.getBatteryLevel());
        }

        @Test //flytta till getters test
        void testGetBikeType() {

           assertTrue(testBike.getType() == Bike.BikeType.ELECTRIC);
        }

        @Test
        void testBikeStatusInMaintenance() {
            testBike.sendToMaintenance();
            assertTrue(testBike.getStatus() == Bike.BikeStatus.MAINTENANCE);
        }

        @Test
        void testCompleteMaintenanceWithBikeNotInMaintenance() {
            testBike.startRide();

            assertThrows(IllegalStateException.class, () -> testBike.completeMaintenance());
            assertFalse(testBike.getStatus() == Bike.BikeStatus.MAINTENANCE);
        }

        @Test
        void testBikeInMaintenance() {
            testBike.sendToMaintenance();
            testBike.completeMaintenance();
            LocalDateTime currentDate = LocalDateTime.now();


            assertTrue(testBike.getStatus() == Bike.BikeStatus.AVAILABLE);
            assertTrue(testBike.getLastMaintenanceDate().isEqual(currentDate));
            assertFalse(testBike.needsMaintenance());
        }

        @Test
        void testCompleteMaintenenaceForElectricBike() {
            testBike.startRide();
            testBike.endRide(10);

            testBike.sendToMaintenance();
            testBike.completeMaintenance();
            assertTrue(testBike.getBatteryLevel() == 100.0);
        }



        @Test
        void testReserveAvailable() {
            testBike.reserve();

            assertTrue(testBike.getStatus() == Bike.BikeStatus.RESERVED);
        }

        @Test
        void testReserveNotAvailable() {
            testBike.reserve();

            assertTrue(testBike.getStatus() == Bike.BikeStatus.RESERVED);
            assertThrows(IllegalStateException.class, () -> testBike.reserve());

        }

        @Test
        void testStandardBikeValues() {

            String actualStandardBikeDisplayName = Bike.BikeType.STANDARD.getDisplayName();
            double actualStandardBikeValue = Bike.BikeType.STANDARD.getPriceMultiplier();

            assertEquals("Standard", actualStandardBikeDisplayName);
            assertEquals(0.0, actualStandardBikeValue);

        }

        @Test
        void testElectricBikeValues() {

            String actualElectricBikeDisplayName = Bike.BikeType.ELECTRIC.getDisplayName();
            double actualElectricBikeValue = Bike.BikeType.ELECTRIC.getPriceMultiplier();

            assertEquals("Electric", actualElectricBikeDisplayName);
            assertEquals(1.5, actualElectricBikeValue);

        }

        @Test
        void testPremiumBikeValues() {

            String actualPremiumBikeDisplayName = Bike.BikeType.PREMIUM.getDisplayName();
            double actualPremiumBikeValue = Bike.BikeType.PREMIUM.getPriceMultiplier();

            assertEquals("Premium", actualPremiumBikeDisplayName);
            assertEquals(2.0, actualPremiumBikeValue);

        }

        @Test
        void testRejectsNullId() {

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new Bike(null, Bike.BikeType.STANDARD));

            assertEquals("Bike ID cannot be null or empty", ex.getMessage());
        }

        @Test
        void testRejectsEmptyId() {

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new Bike(" ", Bike.BikeType.STANDARD));

            assertEquals("Bike ID cannot be null or empty", ex.getMessage());
        }

        @Test
        void testRejectsNullBikeType() {

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new Bike("b123", null));

            assertEquals("Bike type cannot be null", ex.getMessage());


        }

        @Test
        void testMarkAsBroken() {

            testBike.markAsBroken();

            assertEquals(Bike.BikeStatus.BROKEN, testBike.getStatus());
            assertTrue(testBike.needsMaintenance());
        }

        @Test
        void testBikeIsNotAvailableWhenNeedsMaintenanceIsTrue()
        {
            testBike.startRide();
            testBike.endRide(1000.0);

            assertTrue(testBike.needsMaintenance());
            assertFalse(testBike.isAvailable());
        }

        @Test
        void testBikeIsNotAvailableWhenStatusNotAvailable() {

            testBike.startRide();
            assertFalse(testBike.isAvailable());

        }



        @Test
        void testGettersInitialAndAfterChanges() {

            assertEquals("B123", testBike.getBikeId());

            assertEquals(0, testBike.getTotalRides());
            assertEquals(0.0, testBike.getTotalDistance());
            assertNull(testBike.getLastUsedDate());
            assertNull(testBike.getCurrentStationId());
            assertTrue(testBike.isAvailable());

            testBike.setCurrentStationId("S123");
            assertEquals("S123", testBike.getCurrentStationId());

        }

        @Test
        void testToString() {

            String bikeString = testBike.toString();

            assertTrue(bikeString.contains("B123"));
            assertTrue(bikeString.contains("ELECTRIC")); //Testsas på fler ställen i think
            assertTrue(bikeString.contains("AVAILABLE"));

            
        }

        

        

    }
}
