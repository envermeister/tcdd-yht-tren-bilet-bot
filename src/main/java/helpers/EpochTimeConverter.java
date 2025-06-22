package helpers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EpochTimeConverter {

    static String timeZone = "Asia/Baghdad";

    public static String convertEpochToDateTime(long epochTimeMillis) {
        try {

            long epochTimeSeconds = epochTimeMillis / 1000;

            Instant instant = Instant.ofEpochMilli(epochTimeMillis);
            ZoneId zoneId = ZoneId.of(timeZone);
            ZonedDateTime zonedDateTime = instant.atZone(zoneId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMMM d, yyyy HH:mm:ss (a)");
            String formattedDate = zonedDateTime.format(formatter);

            String zoneOffset = zonedDateTime.getOffset().getId();

            return formattedDate + " (" + timeZone + ")" + zoneOffset;
        } catch (Exception e) {
            return "Error: Unable to convert epoch time. " + e.getMessage();
        }
    }


}
