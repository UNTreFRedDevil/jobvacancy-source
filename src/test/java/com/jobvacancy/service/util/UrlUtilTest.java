package com.jobvacancy.service.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UrlUtilTest {

    private static String[] validUrls;
    private static String[] invalidUrls;

    /**
     * Load data to run the tests.
     */
    @BeforeClass
    public static void emailProviderText() {
        validUrls = new String[]{
            "http://www.example.com/resume.pdf",
            "https://www.example.com/resume.pdf",
            "ftp://www.example.com/resume.pdf"
        };

        invalidUrls = new String[]{
            "www.example.com",
            "wwwexamplecom",
            "http://www example com"
        };
    }

    @Test
    public void cuandoEvaluaUnaUrlConFormatoValidoDebeDevolverTrue() {
        for (String url : validUrls) {
            Assert.assertTrue(UrlUtil.urlIsValid(url));
        }
    }

    @Test
    public void cuandoEvaluaUnaUrlConFormatoInvalidoDebeDevolverFalse() {
        for (String url : invalidUrls) {
            Assert.assertFalse(UrlUtil.urlIsValid(url));
        }
    }

}
