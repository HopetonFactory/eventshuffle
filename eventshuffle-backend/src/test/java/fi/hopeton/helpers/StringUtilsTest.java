package fi.hopeton.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    void isEmptyOrNullShouldBeEmpty() {
        assertTrue(StringUtils.isEmptyOrNull(""));
    }

    @Test
    void isEmptyOrNullShouldBeNull() {
        assertTrue(StringUtils.isEmptyOrNull(null));
    }

    @Test
    void isEmptyOrNullShouldBeFalse() {
        assertFalse(StringUtils.isEmptyOrNull("unit-test"));
    }
}