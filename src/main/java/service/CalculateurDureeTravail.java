package service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public final class CalculateurDureeTravail {

    private static final LocalTime DEBUT_JOURNEE_TRAVAIL = LocalTime.of(8, 0);
    private static final LocalTime FIN_JOURNEE_TRAVAIL = LocalTime.of(16, 0);

    private CalculateurDureeTravail() {
    }

    public static long calculerMinutes(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null || !fin.isAfter(debut)) {
            return 0L;
        }

        long total = 0L;
        LocalDate date = debut.toLocalDate();
        LocalDate dateFin = fin.toLocalDate();

        while (!date.isAfter(dateFin)) {
            if (estJourOuvrable(date)) {
                LocalDateTime debutJour = LocalDateTime.of(date, DEBUT_JOURNEE_TRAVAIL);
                LocalDateTime finJour = LocalDateTime.of(date, FIN_JOURNEE_TRAVAIL);
                LocalDateTime debutEffectif = max(debut, debutJour);
                LocalDateTime finEffectif = min(fin, finJour);

                if (finEffectif.isAfter(debutEffectif)) {
                    total += ChronoUnit.MINUTES.between(debutEffectif, finEffectif);
                }
            }
            date = date.plusDays(1);
        }

        return total;
    }

    private static boolean estJourOuvrable(LocalDate date) {
        DayOfWeek jour = date.getDayOfWeek();
        return jour != DayOfWeek.SATURDAY && jour != DayOfWeek.SUNDAY;
    }

    private static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        return a.isAfter(b) ? a : b;
    }

    private static LocalDateTime min(LocalDateTime a, LocalDateTime b) {
        return a.isBefore(b) ? a : b;
    }
}
