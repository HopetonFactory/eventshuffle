package fi.hopeton.helpers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionUtilsTest {

    @Test
    void isEmptyOrNullShouldBeEmpty() {
        assertTrue(CollectionUtils.isEmptyOrNull(Collections.emptyList()));
    }

    @Test
    void isEmptyOrNullShouldBeNull() {
        assertTrue(CollectionUtils.isEmptyOrNull(null));
    }

    @Test
    void isEmptyOrNullShouldBeFalse() {
        assertFalse(CollectionUtils.isEmptyOrNull(Arrays.asList("one", "two")));
    }
}