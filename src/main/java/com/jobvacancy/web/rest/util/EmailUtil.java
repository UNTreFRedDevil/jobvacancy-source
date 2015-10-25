package com.jobvacancy.web.rest.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {
    private static final String validEmailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean emailIsValid(String email){
        boolean isValid;

        Pattern pattern = Pattern.compile(validEmailPattern);
        Matcher matcher = pattern.matcher(email);
        isValid = matcher.matches();

        return isValid;
    }
}
