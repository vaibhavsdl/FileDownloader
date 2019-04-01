package com.agoda.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtility
{
    private static final BufferedReader BUFFERED_READER = new BufferedReader(new InputStreamReader(System.in));

    /**
     * prints to console
     *
     * @param msg
     */
    public static void printToConsole(String msg)
    {
        System.out.println(msg);
    }

    /**
     * handles all user inputs from console
     *
     * @return
     * @throws IOException
     */
    public static String readFromConsole() throws IOException
    {
        return BUFFERED_READER.readLine().trim();
    }
}
