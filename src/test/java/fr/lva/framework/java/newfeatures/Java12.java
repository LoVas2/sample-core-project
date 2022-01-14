package fr.lva.framework.java.newfeatures;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * New methods to the String class : indent and transform.
 * File::mismatch Method.
 * Compact Number Formatting.
 * New Switch expression.
 * Cast in instanceOf.
 */
public class Java12 {

    @Test
    public void stringMethodsTest() {
        // New methods to the String class : indent and transform
        assertEquals("Indent isn't ok", "    test\n", "test".indent(4));
        String text = "abc";
        String test = text.transform(value ->
                value.toUpperCase(Locale.ROOT)
        );
        assertEquals("Transform isn't ok", "ABC", test);
    }

    @Test
    public void filesMethodsTest() throws IOException {
        // File::mismatch Method
        Path filePath1 = Files.createTempFile("file1", ".txt");
        Path filePath2 = Files.createTempFile("file2", ".txt");
        Files.writeString(filePath1, "Java 12 Article");
        Files.writeString(filePath2, "Java 12 Article");

        long mismatch = Files.mismatch(filePath1, filePath2);
        assertEquals("Mismatch isn't ok", -1, mismatch);
    }

    @Test
    public void compactNumberFormattingTest() {
        NumberFormat likesShort =
                NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        likesShort.setMaximumFractionDigits(2);
        assertEquals("2.59K", likesShort.format(2592));

        NumberFormat likesLong =
                NumberFormat.getCompactNumberInstance(Locale.FRANCE, NumberFormat.Style.LONG);
        likesLong.setMaximumFractionDigits(2);
        assertEquals("2,59 mille", likesLong.format(2592));
    }

    @Test
    public void newSwitchTest() {
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        String typeOfDay = switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Working Day";
            case SATURDAY, SUNDAY -> "Day Off";
        };
        assertEquals("New Switch isn't ok", "Working Day", typeOfDay);
    }

    @Test
    public void castInInstanceOfTest() {
        Object obj = "";
        if (obj instanceof String s) {
            int length = s.length();
            assertEquals("Cast isn't ok", 0, length);
        }
    }

}
