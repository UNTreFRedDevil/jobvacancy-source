package com.jobvacancy.service.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmailUtilTest {
    private static String[] validEmails, invalidEmails;

    /**
     * Load data to run the tests.
     */
    @BeforeClass
    public static void emailProviderText() {
        validEmails = new String[]{"test@example.com",
            "test-101@example.com", "test+101@example.com", "test.101@yahoo.com",
            "test101@example.com", "test_101@example.com",
            "test-101@test.net", "test.100@example.com.au", "test@e.com.ar",
            "test@1.com", "test@example.com.com", "test+101@example.com",
            "101@example.com", "test-101@example-test.com"};

        invalidEmails = new String[]{"example", "example@.com.com",
            "exampel101@test.a", "exampel101@.com", ".example@test.com",
            "example**()@test.com", "example@%*.com", "@example", "example@",
            "example..101@test.com", "example.@test.com",
            "test@example_101.com", "example@test@test.com",
            "example@test.com.a5", "example@example", "    @test.com"};
    }

    @Test
    public void cuandoEvaluaUnMailConFormatoValidoDebeDevolverTrue() {
        for (String email : validEmails) {
            Assert.assertTrue(EmailUtil.emailIsValid(email));
        }
    }

    @Test
    public void cuandoEvaluaUnMailConFormatoInvalidoDebeDevolverFalse() {
        for (String email : invalidEmails) {
            Assert.assertFalse(EmailUtil.emailIsValid(email));
        }
    }
}
