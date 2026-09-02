package lk.icbt.dental.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class AppointmentNumberGenerator {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private AppointmentNumberGenerator() {
    }

    public static String generate(LocalDate appointmentDate) {
        LocalDate date = appointmentDate == null ? LocalDate.now() : appointmentDate;
        int suffix = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "APT-" + date.format(DATE_FORMAT) + "-" + suffix;
    }
}
