package com.agoda.constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingConstants
{
    /**
     * Error logger
     */
    public static final Logger ERROR_LOGGER = LogManager.getLogger("error");

    /**
     * Info Logger
     */
    public static final Logger INFO_LOGGER = LogManager.getLogger("info");
}
