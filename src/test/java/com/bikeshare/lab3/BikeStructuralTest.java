package com.bikeshare.lab3;

import com.bikeshare.model.Bike;
import com.bikeshare.model.BikeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BikeStructuralTest {

    @Nested
    class IntegrityTests {

        @Test
        void enumBikeTypeShouldBePublic() {

            assertTrue(Modifier.isPublic(BikeType.class.getModifiers()));
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

    }
}
