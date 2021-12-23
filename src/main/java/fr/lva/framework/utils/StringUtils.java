package fr.lva.framework.utils;

import java.util.Collection;

public class StringUtils {


    /**
     * Replace all special char by a unique space
     *
     * @param str the string to update
     */
    public static void replaceSpecialChars(String str) {
        str.replaceAll("[\\h\\s\\p{Punct}]+", " ");
    }

    /**
     * Check if a string is <code>null</code>, empty or contains only whitespace characters.
     *
     * @param s the string to check, may be <code>null</code>.
     * @return <code>true</code> if the string is <code>null</code>, empty ("") or contains only
     *         whitespace characters.
     */
    public static boolean isBlank(String s) {
        return ((s == null) || (s.trim().length() == 0));
    }

    /**
     * Join collection elements to build a string.
     *
     * @param c a collection.
     * @param sep separator of collection elements.
     * @return a string containing the string representation of each element of the collection,
     *         separated by the specified separator.
     * @throws IllegalArgumentException if <code>sep</code> is <code>null</code>.
     */
    public static String join(Collection<?> c, String sep) {
        if (sep == null) {
            throw new IllegalArgumentException("sep");
        }
        if ((c == null) || (c.isEmpty())) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object element : c) {
            String elementValue = (element == null) ? "" : element.toString();
            sb.append(elementValue).append(sep);
        }
        // Remove last separator
        sb.setLength(sb.length() - sep.length());
        return sb.toString();
    }
}
