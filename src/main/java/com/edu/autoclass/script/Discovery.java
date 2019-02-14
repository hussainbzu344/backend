package com.edu.autoclass.script;

import com.edu.autoclass.bean.EntryPoint;
import com.edu.autoclass.bean.Framework;
import com.edu.autoclass.bean.ProjectType;
import com.edu.autoclass.bean.SearchResult;
import com.edu.autoclass.util.DbConfig;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class Discovery {
    private DbConfig dbConfig;
    SearchResult searchResult = new SearchResult();
    static List<String> searchedFile = new ArrayList<>();
    static List<String> libraries = new ArrayList<>();
    static List<String> dependencies = new ArrayList<>();
    static List<String> unUsedDependencies = new ArrayList<>();
    static List<String> unUsedLibraries = new ArrayList<>();
    static List<String> classNames = new ArrayList<>();

    public Discovery() {
        dbConfig = new DbConfig();
    }

    public void findLibraries(String url) {
        searchedFile = new ArrayList<>();
        libraries = new ArrayList<>();
        dependencies = new ArrayList<>();
        unUsedDependencies = new ArrayList<>();
        unUsedLibraries = new ArrayList<>();
        classNames = new ArrayList<>();
        try {

            String[] extensions = new String[]{"xml", "java", "gradle"};
            String jar_pattern = "(evosuite-tests.zip)";
            Pattern jar_r = Pattern.compile(jar_pattern);

            String pom_pattern = "(pom.xml)";
            String gradle_pattern = "(build.gradle)";
            String java_pattern = "(java)";

            Pattern pom_r = Pattern.compile(pom_pattern);
            Pattern gradle_r = Pattern.compile(gradle_pattern);
            Pattern java_r = Pattern.compile(java_pattern);

            String search_pattern = "import";
            String search_pom_pattern = "<groupId>";
            String search_gradle_pattern = "import";
            Pattern search_r = Pattern.compile(search_pattern);
            Pattern search_p = Pattern.compile(search_pom_pattern);
            Pattern search_g = Pattern.compile(search_gradle_pattern);

            String current_dir="";
               // Matcher jar_match = jar_r.matcher(url);
            File fin = new File("src/main/resources/extracted");
            File theDir = new File("src/main/resources/extracted1");
                    String folder="";
                    File zip_exists=new File(url+"/.xcorpus/evosuite-tests.zip");
                    if(zip_exists.exists()){
                        if (fin.exists()) {
                            folder="src/main/resources/extracted1";

                            theDir.mkdir();
                        }else{
                            folder="src/main/resources/extracted";
                            fin.mkdir();
                        }

                        current_dir=folder;
                        JarFile jar = new JarFile(url+"/.xcorpus/evosuite-tests.zip");
                        Enumeration enuma = jar.entries();
                        while (enuma.hasMoreElements()) {
                            JarEntry file_extract = (JarEntry) enuma.nextElement();
                            File f = new File(folder + File.separator + file_extract.getName());
                            if (file_extract.isDirectory()) { // if its a directory, create it
                                f.mkdir();
                                continue;
                            }
                            InputStream is = jar.getInputStream(file_extract); // get the input stream
                            FileOutputStream fos = new FileOutputStream(f);
                            System.out.println("Files writing::"+file_extract.getName());
                            while (is.available() > 0) {  // write contents of 'is' to 'fos'
                                fos.write(is.read());
                            }
                            fos.close();
                            is.close();
                        }
                        System.out.print("Files created");

                    }else{
                        current_dir=url;
                    }
            File dir = new File(current_dir);

            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
                    for (File file : files) {
                        System.out.println("files::" + file.getName());
                        Matcher pom_match = pom_r.matcher(file.getCanonicalPath());
                        Matcher gradle_match = gradle_r.matcher(file.getCanonicalPath());
                        Matcher java_match = java_r.matcher(file.getCanonicalPath());
                        if (pom_match.find()) {
                            if (file.getName().equals("pom.xml")) {
                                dependencies = ReadXmlFile.getDependenciesFromPomFile(file);
                            }
                        }
                        if (gradle_match.find()) {

                        }
                        if (java_match.find()) {
                            System.out.println("reading file"+file.getName());
                            readJavaFiles(file, search_r);
                        }
                    }
            if(folder.equalsIgnoreCase("src/main/resources/extracted") && zip_exists.exists()){
                if(theDir.exists()){
                    FileUtils.deleteDirectory(new File("src/main/resources/extracted1"));
                    System.out.println("Folder Deleted extracted1");
                }
            }else if(folder.equalsIgnoreCase("src/main/resources/extracted1") && zip_exists.exists()){
                if(fin.exists()){
                    FileUtils.deleteDirectory(new File("src/main/resources/extracted"));
                    System.out.println("Folder Deleted extracted");
                }
            }

            List<Framework> frameworks = new ArrayList<>();
            List<EntryPoint> entryPoints = new ArrayList<>();
            List<ProjectType> projectTypes = new ArrayList<>();
            for (String library : libraries) {
                frameworks.addAll(dbConfig.getFramework(library));
                for (Framework framework : frameworks) {
                    entryPoints.addAll(dbConfig.getEnteryPointsById(framework.getId()));
                    projectTypes.addAll(dbConfig.getType(framework.getTypeId()));
                    framework.setEntryPointList(entryPoints);
                    entryPoints = entryPoints.stream().distinct().collect(Collectors.toList());
                    projectTypes = projectTypes.stream().distinct().collect(Collectors.toList());
                }
            }
            searchResult.setFrameworks(frameworks);
            searchResult.setEntryPoints(entryPoints);
            searchResult.setProjectTypes(projectTypes);
            searchResult.setLibraries(libraries);
            unUsedDependencies = findUnusedDependencies(dependencies, libraries);
          //  unUsedLibraries = findUnusedLibraries(libraries, files);
            for (String name : unUsedDependencies) {
                System.out.println(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public List<String> findUnusedDependencies(List<String> dependencies, List<String> libraries) {

        List<String> usedDependency = new ArrayList<>();

        for (String dependency : dependencies) {
            String compare = "";
            if (dependency.equals("mysql")) {
                compare = "java.sql";
            } else {
                compare = dependency;
            }
            for (String library : libraries) {
                Pattern search = Pattern.compile(compare);
                Matcher search_match = search.matcher(library);
                if (search_match.find()) {
                    if (!usedDependency.contains(dependency)) {
                        usedDependency.add(dependency);
                    }
                }
            }
        }
        if (dependencies.size() == usedDependency.size()) {
            return new ArrayList<>();
        } else {
            for (String dep : usedDependency) {
                dependencies.remove(dep);
            }
        }
        return dependencies;
    }

    public String lastWordInLibrary(String library) {
        String name = library;
        String lastWord = "";
        name = name.replace("*", "");
        if (name.charAt(name.length() - 1) == '.') {
            name = name.substring(0, name.length() - 1);
        }
        if (name.contains(".")) {
            lastWord = name.substring(name.lastIndexOf(".") + 1);
        }
        return lastWord;
    }

    public List<String> findUnusedLibraries(List<String> libraries, List<File> files) throws IOException {
        List<String> usedLibrary = new ArrayList<>();
        for (String library : libraries) {
            String word = lastWordInLibrary(library);
            for (File file:files) {
                String java_pattern = "(java)";
                Pattern java_r = Pattern.compile(java_pattern);
                Matcher java_match = java_r.matcher(file.getCanonicalPath());
                if (java_match.find()) {
                    String search_pattern = word;
                    Pattern search_r = Pattern.compile(search_pattern);
                    String line;
                    FileReader fr = new FileReader(file.getCanonicalPath());
                    BufferedReader br = new BufferedReader(fr);
                    while ((line = br.readLine()) != null) {
                        Matcher search_match = search_r.matcher(line);
                        if (search_match.find()) {
                            System.out.println(line);
                            if(search_match.groupCount()>1){
                             if(!usedLibrary.contains(line)){
                                 usedLibrary.add(line);
                             }
                            }
                        }
                    }
                    fr.close();
                    br.close();

                }
            }
        }
        for (String names:usedLibrary
             ) {
            System.out.println("unused--"+names);
        }
        return libraries;
    }

    public void readJavaFiles(File file, Pattern pattern) throws IOException {
        BufferedReader br = null;
        FileReader fr = null;
        String line;

        fr = new FileReader(file.getCanonicalPath());
        br = new BufferedReader(fr);
        if (!classNames.contains(file.getName()) && !file.getName().contains(".xml")) {
            classNames.add(file.getName().replaceAll(".java", ""));
        }
        classNames = classNames.stream().distinct().collect(Collectors.toList());
        while ((line = br.readLine()) != null) {
            Matcher search_match = pattern.matcher(line);

            if (search_match.find()) {
                if(line.contains("import")){
                    line = line.replace("import ", "");
                    line = line.replace("static", "");
                    line = line.replace(";", "");
                    String check = lastWordInLibrary(line);
                    if (!classNames.contains(check)) {
                        if (!libraries.contains(line) && !(line.contains("<") ||line.contains(">")||line.contains("!") ||line.contains(" ") ||line.contains("\\"))) {
                            libraries.add(line);
                        }
                    }
                    searchedFile.add(file.getCanonicalPath());
                }
            }
        }

        fr.close();
        br.close();

    }

    public static boolean findMainMethod(String name, String location) throws IOException {
        String search_pattern = name;
        String search_main_method = "public static void main";
        String search_main_method_activity = "extends Activity";
        String search_midlet = "void startApp";
        Pattern search_r = Pattern.compile(search_pattern);
        Pattern search_m = Pattern.compile(search_main_method);
        Pattern search_m_a = Pattern.compile(search_main_method_activity);
        Pattern search_micro = Pattern.compile(search_midlet);
        BufferedReader br = null;
        FileReader fr = null;
        String line;
        for (String string : searchedFile) {
            fr = new FileReader(string);
            br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                Matcher search_match = search_r.matcher(line);
                Matcher search_main_match = search_m.matcher(line);
                if (search_match.find()) {
                    if (location.equals("main method")) {
                        Matcher search_main_match_activity = search_m_a.matcher(line);
                        Matcher search_main_match_midlet = search_micro.matcher(line);
                        if (search_main_match.find()) {
                            return true;
                        } else if (search_main_match_activity.find()) {
                            return true;
                        } else if (search_main_match_midlet.find()) {
                            return true;
                        }else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                }
            }

            fr.close();
            br.close();
        }
        return false;
    }

    public String[] getFrameworks() {
        List<Framework> frameworks = searchResult.getFrameworks();
        String[] strings = new String[frameworks.size()];
        int index = 0;
        for (Framework framework : frameworks) {
            strings[index] = framework.getName();
            index++;
        }

        return new HashSet<String>(Arrays.asList(strings)).toArray(new String[0]);
    }

    public String[] getLibraries() {
        List<String> libraries = searchResult.getLibraries();
        String[] strings = new String[libraries.size()];
        int index = 0;
        for (String library : libraries) {
            strings[index] = library;
            index++;
        }
        return strings;
    }

    public String[] getEntryPoints() {
        List<EntryPoint> entryPoints = searchResult.getEntryPoints();
        String[] strings = new String[entryPoints.size()];
        int index = 0;
        for (EntryPoint entryPoint : entryPoints) {
            strings[index] = entryPoint.getLocation();
            index++;
        }

        return new HashSet<String>(Arrays.asList(strings)).toArray(new String[0]);
    }

    public String[] getEntryPointsByFramework(int id) {
        List<Framework> frameworks = searchResult.getFrameworks();
        String[] strings = new String[0];
        for (Framework framework : frameworks) {
            if (framework.getId() == id) {
                List<EntryPoint> entryPoints = framework.getEntryPointList();
                strings = new String[entryPoints.size()];
                int index = 0;
                for (EntryPoint entryPoint : entryPoints) {
                    strings[index] = entryPoint.getLocation();
                    index++;
                }
            }
        }
        return new HashSet<String>(Arrays.asList(strings)).toArray(new String[0]);
    }

    public String[] getProjectTypes() {
        List<ProjectType> projectTypes = searchResult.getProjectTypes();
        String[] strings = new String[projectTypes.size()];
        int index = 0;
        for (ProjectType projectType : projectTypes) {
            if (!projectType.getName().equals("--")) {
                strings[index] = projectType.getName();
                index++;
            }
        }
        return new HashSet<String>(Arrays.asList(strings)).toArray(new String[0]);

    }

    public String[] getProjectType() throws IOException {
        String[] strings = new String[1];
        List<ProjectType> projectTypes = searchResult.getProjectTypes();
        List<ProjectType> projectTypes1 = new ArrayList<>();
        for (ProjectType projectType : projectTypes) {
            if (!projectType.getName().equals("--")) {
                projectTypes1.add(projectType);
            }

        }
        strings[0] = Probability.probablityofProjectType(projectTypes1, searchResult.getFrameworks());
        return new HashSet<String>(Arrays.asList(strings)).toArray(new String[0]);
    }

}