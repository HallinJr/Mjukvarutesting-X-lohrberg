package com.bikeshare.lab2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.bikeshare.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lab 2 Template: Black Box Testing for User class
 * 
 
 * 
 * TODO for students:
 * - Challenge 2.1: Add Equivalence Partitioning tests for email validation, name, telephone number (With GenAI help), and fund addition
 * - Challenge 2.2: Add Boundary Value Analysis tests for fund addition
 * - Challenge 2.3: Add Decision Table tests for phone number validation
 * - Optional Challenge 2.4: Add error scenario tests
 */

// This test is just an example to get you started. You will need to add more tests as per the challenges.
@DisplayName("Verify name handling in User class")
class UserBlackBoxTest {
    
    @Test
    @DisplayName("Should store and retrieve user names correctly")
    void shouldStoreAndRetrieveUserNamesCorrectly() {
        // Arrange - Set up test data
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer
        
        // Act - Execute the method under test
        User user = new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName);
        String actualFirstName = user.getFirstName();
        String actualLastName = user.getLastName();
        String actualFullName = user.getFullName();
        
        // Assert - Verify the expected outcome
        assertNotNull(user, "User should be created successfully");
        assertEquals(expectedFirstName, actualFirstName, "First name should match");
        assertEquals(expectedLastName, actualLastName, "Last name should match");
        assertEquals("John Doe", actualFullName, "Full name should be formatted correctly");
    }
    
    // TODO: Challenge 2.1 - Add Equivalence Partitioning tests for email validation
    // Hint: Test valid emails (user@domain.com) and invalid emails (missing @, empty, etc.)
    @Test
    void shouldDeclineInvalidEmailFormat() {
        ArrayList<String> invalidEmails = new ArrayList<>();
        invalidEmails.add("john.doeexample.com");
        invalidEmails.add("john.doe@@example.com");
        invalidEmails.add("john.doe@examplecom");
        invalidEmails.add("");

        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        // String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer

        for (int i = 0; i < invalidEmails.size(); i++) {
            int hej = i;
            assertThrows(IllegalArgumentException.class,
                    () -> new User(validPersonnummer, invalidEmails.get(hej), expectedFirstName, expectedLastName),
                    "Expected exception for invalid email: " + invalidEmails.get(i));
        }
    }

    @Test
    void shouldValidateCorrectEmailFormat(){
        ArrayList<String> validEmails = new ArrayList<>();
        validEmails.add("john.doe@email.com");
        validEmails.add("john.d@email.com");
        validEmails.add("jn.doe@exle.com");

        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer

        for (int i = 0; i < validEmails.size(); i++) {
            // Act - Execute the method under test
            User user = new User(validPersonnummer, validEmails.get(i), expectedFirstName, expectedLastName);

            String actualEmail = user.getEmail();

            assertEquals(validEmails.get(i), actualEmail, "");

        }
    }


    
    // TODO: Challenge 2.2 - Add Boundary Value Analysis tests for fund addition
    // Hint: Test minimum (0.01), maximum (1000.00), and invalid amounts (0, negative, > 1000)

    @DisplayName("Fund Addition Boundary Tests")

    @Test
    void testUpperFundBoundary() {
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer

        User user = new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName);

        double previousFunds = user.getAccountBalance();
        user.addFunds(1000.00);

        assertEquals((previousFunds + 1000.00), user.getAccountBalance(), "Funds should match");
    }

    @Test
    void testLowerFundBoundary() {
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer

        User user = new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName);

        double previousFunds = user.getAccountBalance();
        user.addFunds(0.01);

        assertEquals((previousFunds + 0.01), user.getAccountBalance(), "Funds should match");
    }

    @Test
    void testInvalidNumberFund() {
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer


        assertThrows(IllegalArgumentException.class,
                () -> new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName).addFunds(0.0), "Expected exception for invalid fund");
    }

    @Test
    void testAboveMaxLimitFunds() {
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer


        assertThrows(IllegalArgumentException.class,
                () -> new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName).addFunds(1000.01), "Expected exception for invalid fund");
    }

    @Test
    void testNegativeNumberFunds() {
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer


        assertThrows(IllegalArgumentException.class,
                () -> new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName).addFunds(-10.00), "Expected exception for invalid fund");
    }


    
    // TODO: Challenge 2.3 - Add Decision Table tests for phone number validation
    // Hint: Test Swedish phone formats (+46701234567, 0701234567) and invalid formats
    @Test
    void shouldValidateCorrectPhoneNumberFormat(){
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer

        User user = new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName);

        String expectedPhoneNumber = "+46701234567"; // Valid format
        user.setPhoneNumber(expectedPhoneNumber);

        assertEquals("+46701234567", user.getPhoneNumber(), "Numbers should match");

        expectedPhoneNumber = "0701234567";
        user.setPhoneNumber(expectedPhoneNumber);

        assertEquals("0701234567", user.getPhoneNumber(), "Phone number should match");
    }

    @Test
    void shouldDeclineInvalidPhoneNumberFormats() {
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String validEmail = "john.doe@example.com";
        String validPersonnummer = "901101-1237"; // Valid Swedish personnummer

        User user = new User(validPersonnummer, validEmail, expectedFirstName, expectedLastName);

        // List of invalid phone numbers
        List<String> invalidPhoneNumbers = List.of(
                "123456",      // Too short
                "abcdefghij",  // Non-numeric
                "070123456789" // Too long
        );

        for (String phoneNumber : invalidPhoneNumbers) {
            assertThrows(IllegalArgumentException.class,
                    () -> user.setPhoneNumber(phoneNumber),
                    "Expected exception for invalid phone number: " + phoneNumber);
        }

        user.setPhoneNumber("");
        assertNull(user.getPhoneNumber(), "Phone number should be null for empty input");
    }
    
    // TODO: Challenge 2.4 - Add error scenario tests
    // Hint: Test insufficient balance, invalid inputs, state violations
}
