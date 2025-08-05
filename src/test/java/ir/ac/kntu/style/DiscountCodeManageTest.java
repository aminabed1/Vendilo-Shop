package ir.ac.kntu.style;

import ir.ac.kntu.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountCodeManageTest {

    @Test
    public void testGenerateRandomDiscountCodeFormat() {
        DiscountCodeManage manager = DiscountCodeManage.getInstance();
        String code = manager.generateRandomDiscountCode();

        assertNotNull(code, "Generated code should not be null");

        assertEquals(8, code.length(), "Code should be 8 characters long");

        assertTrue(code.matches("(\\d[A-Z]){4}"), "Code format must match digit+uppercase letter, repeated 4 times");
    }
}
