package fr.lva.framework.utils;

/**
 * A utility class to check arguments validity.
 */
public final class Args {

    /**
     * Default constructor, private on purpose.
     *
     * @throws UnsupportedOperationException always.
     */
    private Args() {
        throw new UnsupportedOperationException();
    }

    /**
     * Check that a given argument is not null.
     *
     * @param o the argument to check.
     * @param msg the error message.
     * @return the argument.
     * @if <code>o</code> is not <code>null</code>.
     */
    public static <T> T notNull(T o, String msg) {
        if (o == null) {
            throw new IllegalArgumentException(msg);
        }
        return o;
    }
}
