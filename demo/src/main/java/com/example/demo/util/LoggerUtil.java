package com.example.demo.util;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger("SystemLogs");

    static {
        try {
            FileHandler fh = new FileHandler("logs/system.log", true); // Logs will be appended
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message, Exception ex) {
        logger.severe(message + " - " + ex.getMessage());
    }
}

