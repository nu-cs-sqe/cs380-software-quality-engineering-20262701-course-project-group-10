package domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class ColorTests {
    @Test
    public void valuesContainExactlyWhiteAndBlack() {
        assertArrayEquals(new Color[] {Color.WHITE, Color.BLACK}, Color.values());
    }
}
