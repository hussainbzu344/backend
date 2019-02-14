package com.edu.autoclass.script;

enum Types {
    DesktopApplication, EmbeddedApplication, MobileApplication, WebServer, WebService;

    public static boolean contains(String s) {
        for (Types types : values())
            if (types.name().equals(s))
                return true;
        return false;
    }

}
