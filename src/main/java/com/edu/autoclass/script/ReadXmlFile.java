package com.edu.autoclass.script;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadXmlFile {
    public static List<String> getDependenciesFromPomFile(File file) throws IOException, XmlPullParserException {
        List<String> dependencies = new ArrayList<>();
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader(file.getCanonicalFile()));
        List<Dependency> dependenciesList = model.getDependencies();
        for (Dependency dependency : dependenciesList) {
            String line = dependency.getGroupId();
            line = line.replaceAll("-", ".");
            line = line.replaceAll("_", ".");
            dependencies.add(line);
        }
        return dependencies;
    }
}