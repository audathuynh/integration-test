package org.maxsure.test.common;

import java.util.UUID;

/**
 *
 * @author Dat Huynh
 * @since 1.0
 */
public final class Utils {

    private Utils() {}

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

}
