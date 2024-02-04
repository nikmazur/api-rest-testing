package helpers;

import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Methods {

    // Method for obtaining random date within start year to now (minus minimum working age).
    // Breaks for earlier than 1970, might switch to JodaTime.
    @Step("Generate random birthday starting from year {0}")
    public static String getRandomBirthday(int startYear, int minAge) {
        var minDay = LocalDate.of(startYear, 1, 1).toEpochDay();
        var maxDay = LocalDate.now().minusYears(minAge).toEpochDay();
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(
                LocalDate.ofEpochDay(minDay + RandomUtils.nextLong(minDay, maxDay)));
    }

    @Step("Compare 2 dates: {0} and {1}")
    public static boolean compareDates(LocalDate earlier, LocalDate later) {
        return earlier.toEpochDay() < later.toEpochDay();
    }
}
