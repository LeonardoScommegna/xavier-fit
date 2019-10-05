package src.test.java.it;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class With2SkippedAnd1SuccessfulTest {
    @Test
    @Disabled("reason")
    void it_runs_the_test() {
        assertTrue(true);
    }

    @Test
    @Disabled
    void it_runs_the_test_2() {
        assertTrue(true);
    }

    @Test
    void it_runs_the_test_3() {
        assertTrue(true);
    }
}