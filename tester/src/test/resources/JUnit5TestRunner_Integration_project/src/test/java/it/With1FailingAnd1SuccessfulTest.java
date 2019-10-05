package src.test.java.it;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class With1FailingAnd1SuccessfulTest {
    @Test
    void it_runs_the_test() {
        assertTrue(true);
    }

    @Test
    void it_fails() {throw new RuntimeException("purposely failing");}
}
