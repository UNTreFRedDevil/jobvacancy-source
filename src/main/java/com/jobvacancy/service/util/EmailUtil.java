package com.jobvacancy.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    private static final String validEmailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean emailIsValid(String email) {
        Pattern pattern = Pattern.compile(validEmailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
