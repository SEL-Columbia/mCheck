package org.who.mcheck.core.util;

import static java.lang.Integer.parseInt;

public class IntegerUtil {
    public static int tryParse(String value, int defaultValue) {
        try {
            return parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}