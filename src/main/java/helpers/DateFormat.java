package helpers;

public class DateFormat {

    public static String updateDateFormat (String date) {

        String[] splitDate = date.split("-");

        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);

        day -= 1;

        return String.join("-",String.valueOf(day),String.valueOf(month));

    }

}
