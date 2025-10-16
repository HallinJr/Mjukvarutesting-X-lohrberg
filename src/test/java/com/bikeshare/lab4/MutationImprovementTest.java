package com.bikeshare.lab4;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeshare.model.User;
import com.bikeshare.service.auth.BankIDService;
import com.bikeshare.service.validation.AgeValidator;
import com.bikeshare.service.validation.IDNumberValidator;

/**
 * Lab 4: Mutation Testing Improvement Template
 * 
 * GOAL: Increase mutation score by writing focused tests using mocks
 * 
 * STRATEGY:
 * 1. Run mutation testing: mvn clean test
 * org.pitest:pitest-maven:mutationCoverage -Pcoverage-comparison
 * 2. Open target/pit-reports/index.html and analyze survived mutations (red
 * lines)
 * 3. Write targeted tests using mocks to kill specific mutations
 * 4. Focus on boundary conditions, error scenarios, and logic verification
 * 5. Re-run mutation testing to measure improvement
 * 
 * HINTS:
 * - Look for mutations in AgeValidator.java (lines 43-44 are known survivors)
 * - Use mocks to create precise test scenarios
 * - Test both positive and negative cases
 * - Verify that tests fail when mutations are present
 * 
 * CURRENT BASELINE (from CoverageEx.java):
 * - AgeValidator: 94% line coverage, 75% mutation coverage
 * - 2 mutations survived in birthday logic
 * - Your job: Kill those mutations!
 * - Or Kill other mutations you find interesting
 * 
 * IF NOT POSSIBLE TO KILL THE MUTANTS? DEAD CODE? WHAT NOW?
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Lab 4: Mutation Testing Improvement")
public class MutationImprovementTest {

    // TODO: Set up mocks for dependencies
    @Mock
    private IDNumberValidator mockIdValidator;

    @Mock
    private BankIDService mockBankIdService;

    // TODO: Inject mocks into the class under test
    @InjectMocks
    private AgeValidator ageValidator;

    // TODO: You can also test User class mutations
    // Hint: Look at User.java for other mutation opportunities

    @BeforeEach
    void setUp() {
        // MockitoExtensions handles mock initialization
        // Add any common setup here if needed
    }

    @Nested
    class AgeValidation {

        // TODO: Write tests to kill boundary condition mutations
        // Hint: The mutation >= vs > is a common survivor
        @Test
        @DisplayName("Should kill boundary mutation: exactly 18 years old")
        void shouldKillBoundaryMutation_Exactly18() {
            // TODO: Create a person who is exactly 18 years old
            LocalDate today = LocalDate.now();
            LocalDate birthDate = today.minusYears(18);

            String exactly18ID = birthDate.toString().replace("-", "") + "1234";

            when(mockIdValidator.isValidIDNumber(exactly18ID)).thenReturn(true);
            when(mockBankIdService.authenticate(exactly18ID)).thenReturn(true);

            boolean result = ageValidator.isAdult(exactly18ID);

            assertTrue(result, "Person exactly 18 should be adult");
            verify(mockIdValidator).isValidIDNumber(exactly18ID);
            verify(mockBankIdService).authenticate(exactly18ID);

            // Hint: Use a birthday that makes them 18 today
            // Hint: Mock the dependencies to return true for validation and auth
            // Hint: This test should kill the >= vs > mutation

            // String exactly18ID = "???"; // Figure out the right format
            // when(mockIdValidator.isValidIDNumber(exactly18ID)).thenReturn(???);
            // when(mockBankIdService.authenticate(exactly18ID)).thenReturn(???);

            // boolean result = ageValidator.isAdult(exactly18ID);

            // assertTrue(result, "Person exactly 18 should be adult");
            // verify(mockIdValidator).isValidIDNumber(exactly18ID);
            // verify(mockBankIdService).authenticate(exactly18ID);
        }
    }

    @Nested
    class UserMutation {

        User u;

        @BeforeEach
        void setUp() {
            u = new User("900101-2385", "liam@gmail.com", "Liam", "Lol");

        }

        @Test
        void verifyEmailTriggersPendingVerification() {

            u.verifyEmail();

            assertTrue(u.isEmailVerified());
            assertEquals(User.UserStatus.PENDING_VERIFICATION, u.getStatus());
        }

        @Test
        void verifyEmailAfterPhoneIsVerifiedTriggersActivate() {

            u.setPhoneNumber("0701234567");

            u.verifyPhone();
            assertTrue(u.isPhoneVerified());

            u.verifyEmail();
            assertTrue(u.isEmailVerified());

            assertEquals(User.UserStatus.ACTIVE, u.getStatus());

        }

        @Test
        void negativeAmountThrows() {

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> u.addFunds(-0.01));

            assertEquals("Amount must be positive", ex.getMessage());
        }

        @Test
        void zeroAmountThrows() {

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> u.addFunds(0.0));

            assertEquals("Amount must be positive", ex.getMessage());
        }

        @Test
        void aboveUpperLimitThrows() {

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> u.addFunds(1000.01));

            assertEquals("Cannot add more than $1000 at once", ex.getMessage());
        }

        @Test
        void exactlyUpperLimitAllowed() {

            double before = u.getAccountBalance();
            u.addFunds(1000.0);

            assertEquals(before + 1000.0, u.getAccountBalance());
        }

        @Test
        void deductExactlyBalanceAllowed() {

            u.addFunds(50.0);

            u.deductFunds(50.0);

            assertEquals(0.0, u.getAccountBalance());
            assertEquals(50.0, u.getTotalSpent());
        }

        @Test
        void deductNegativeAmountThrows() {
            u.addFunds(100);

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> u.deductFunds(-0.01));

            assertEquals("Amount cannot be negative", ex.getMessage());
        }

        @Test
        void deductZeroAmountAllowedNoChange() {

            u.addFunds(50.0);
            double beforeBalance = u.getAccountBalance();
            double beforeSpent = u.getTotalSpent();

            u.deductFunds(0.0);

            assertEquals(beforeBalance, u.getAccountBalance());
            assertEquals(beforeSpent, u.getTotalSpent());
        }

        @Test
        void deductAboveBalanceThrows() {
            u.addFunds(50.0);

            IllegalStateException ex = assertThrows(
                    IllegalStateException.class,
                    () -> u.deductFunds(50.01));

            assertEquals("Insufficient balance", ex.getMessage());
            
        }

    }

}