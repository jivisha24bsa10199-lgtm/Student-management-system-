package com.sms;

import com.sms.controllers.ConsoleController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting Student Management System");
            ConsoleController controller = new ConsoleController();
            controller.start();
            logger.info("Student Management System terminated successfully");
        } catch (Exception e) {
            logger.error("Fatal error in Student Management System", e);
            System.err.println("An unexpected error occurred: " + e.getMessage());
            System.exit(1);
        }
    }
}
