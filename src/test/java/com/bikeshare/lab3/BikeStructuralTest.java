package com.bikeshare.lab3;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.bikeshare.model.Bike;

public class BikeStructuralTest {

    @Nested
    class IntegrityTests {

        @Test
        void enumBikeTypeShouldBePublic() {

            assertTrue(Modifier.isPublic(Bike.BikeType.class.getModifiers()));
        }

        @Test
        void bikeConstructorShouldBePublic() {

            assertTrue(Modifier.isPublic(Bike.class.getModifiers()));
        }

    }

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
        void testReserveAvailable() {
            testBike.reserve();

            assertTrue(testBike.getStatus() == Bike.BikeStatus.RESERVED);

        }

        @Test
        void testReserveNotAvailable() {
            testBike.reserve();

            assertThrows(IllegalStateException.class, () -> testBike.reserve(), "mesagfe");

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

            System.out.println("Message: " + ex.getMessage());

            assertEquals("Bike ID cannot be null or empty", ex.getMessage());
        }

    }
}
