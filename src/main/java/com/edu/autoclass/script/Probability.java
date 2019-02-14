package com.edu.autoclass.script;

import com.edu.autoclass.bean.EntryPoint;
import com.edu.autoclass.bean.Framework;
import com.edu.autoclass.bean.ProjectType;

import java.io.IOException;
import java.util.List;

public class Probability {

    public static String probablityofProjectType(List<ProjectType> projectTypes, List<Framework> frameworks) throws IOException {
        if (projectTypes.isEmpty() || projectTypes == null) {
            String rule = "public static void main";
            if (Confirmation.confirmEntryPoints(rule, "main method")) {
                return "DesktopApplication::cmd";
            }
            for (Framework framework:frameworks) {
                if(framework.getName().contains("junit")||framework.getName().contains("junit")||framework.getName().contains("assertj")||framework.getName().contains("mockito")){
                    return "DesktopApplication::Test";
                }
            }
        }
        for (Framework framework : frameworks) {
            for (EntryPoint entryPoint : framework.getEntryPointList()) {
                entryPoint.setStatus(Confirmation.confirmEntryPoints(entryPoint.getName(), entryPoint.getLocation()));
            }
            framework.setEntryPointList(framework.getEntryPointList());
            for (ProjectType projectType : projectTypes) {
                if (projectType.getName().equals("android")) {
                    return projectType.getName() + "::" + projectType.getType();
                } else if (projectType.getName().equals("gui-javaFX") || projectType.getName().equals("gui-awt") || projectType.getName().equals("gui-swing")) {
                    return projectType.getName() + "::" + projectType.getType();
                } else if(projectType.getName().equals("javaME")){
                    return projectType.getName() + "::" + projectType.getType();
                }
            }
            return projectTypes.get(0).getName() + "::" + projectTypes.get(0).getType();
        }

        return "";
    }
}
