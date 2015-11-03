package com.jobvacancy.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

    private static final String validUrlPattern =
        "^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public static boolean urlIsValid(String url) {
        Pattern pattern = Pattern.compile(validUrlPattern);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

}
