package ir.ac.kntu.style;

import ir.ac.kntu.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VendiloPlusAccountManageTest {

    @Test
    void testGeneratePercentCode_ValidInput() {
        String simulatedInput = "20\n5\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        DiscountCodeManage manage = new DiscountCodeManage();
        String purpose = "special offer";

        manage.generatePercentCode(purpose);

        List<DiscountCode> codes = DataBase.getInstance().getDiscountCodeList();
        assertFalse(codes.isEmpty());

        DiscountCode code = codes.get(0);
        assertEquals(purpose, code.getPurpose());
        assertInstanceOf(PercentDiscount.class, code);
    }


}
