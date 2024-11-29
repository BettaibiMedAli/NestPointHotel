package com.nestpointdev.NestPointHotel.utils;

import java.security.SecureRandom;

public class Utils {
    private static final String Alphnumeric_string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode (int length)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length; i++)
        {
            int randomIndex = secureRandom.nextInt(Alphnumeric_string.length());
            char randomchar = Alphnumeric_string.charAt(randomIndex);
            stringBuilder.append(randomchar);
        }
        return stringBuilder.toString();
    }
}
