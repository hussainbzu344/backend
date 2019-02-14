package com.edu.autoclass.script;

import java.io.IOException;

public class Confirmation {

    public static boolean confirmEntryPoints(String name, String location) throws IOException {
        if (location.equals("Class")) {

            if (Discovery.findMainMethod(name, location)) {
                return true;
            } else {
                return false;
            }
        } else if (location.equals("main method")) {
            if (Discovery.findMainMethod(name, location)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
