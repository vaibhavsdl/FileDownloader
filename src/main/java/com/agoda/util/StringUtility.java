package com.agoda.util;

import java.util.Arrays;
import java.util.List;

public class StringUtility
{
    /**
     * Splits urls into list using split char provided to seperate urls
     *
     * @param baseString
     * @param splitChar
     * @return
     */
    public static List<String> splitStringToList(String baseString, String splitChar)
    {
        baseString = baseString.trim();
        if(baseString.contains(" "))
        {
            baseString = baseString.replaceAll(" ", "");
        }

        if(baseString.contains(splitChar))
        {
            return Arrays.asList(baseString.split(splitChar));
        }
        else {
            return Arrays.asList(baseString);
        }
    }

    /**
     * replaces specials char by _ except '_ and -'
     *
     * @param inputString
     * @return
     */
    public static String cleanPathString(String inputString)
    {
        return inputString.replaceAll("[^a-zA-Z0-9_-]+", "_");
    }

    /**
     * replaces specials char by _ except '_ and - and .'
     * forms the name by which file would be downloaded to local folder
     *
     * @param inputString
     * @return
     */
    public static String cleanFileNameString(String inputString)
    {
        return inputString.replaceAll("[^a-zA-Z0-9_\\.-]+", "_");
    }
}
