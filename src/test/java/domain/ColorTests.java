package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ColorTests {
    @Test
    public void valuesContainExactlyWhiteAndBlack() {
        assertArrayEquals(new Color[] {Color.WHITE, Color.BLACK}, Color.values());
    }
}
