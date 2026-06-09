package service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class CalculateurDureeTravailTest {

    @Test
    void calculeUniquementLesMinutesEntre8hEt16h() {
        long minutes = CalculateurDureeTravail.calculerMinutes(
                LocalDateTime.of(2026, 6, 8, 7, 30),
                LocalDateTime.of(2026, 6, 8, 17, 15));

        assertEquals(480L, minutes);
    }

    @Test
    void ignoreLesNuitsEtLesWeekEnds() {
        long minutes = CalculateurDureeTravail.calculerMinutes(
                LocalDateTime.of(2026, 6, 5, 15, 0),
                LocalDateTime.of(2026, 6, 8, 10, 0));

        assertEquals(180L, minutes);
    }

    @Test
    void retourneZeroQuandIntervalleHorsHeuresOuvrees() {
        long minutes = CalculateurDureeTravail.calculerMinutes(
                LocalDateTime.of(2026, 6, 6, 9, 0),
                LocalDateTime.of(2026, 6, 7, 12, 0));

        assertEquals(0L, minutes);
    }
}
