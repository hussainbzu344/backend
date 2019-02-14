package com.edu.autoclass.util;

import com.edu.autoclass.bean.EntryPoint;
import com.edu.autoclass.bean.Framework;
import com.edu.autoclass.bean.ProjectType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConfig {
    Connection conn;

    public DbConfig() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/automatic_classification?characterEncoding=utf8", "root", "multan90");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProjectType> getType(int id) {
        List<ProjectType> projectType = new ArrayList<ProjectType>();
        try {
            Statement stmt = conn.createStatement();
            String strSelect = "select * from project_type where id=" + id;
            ResultSet rset = stmt.executeQuery(strSelect);
            while (rset.next()) {   // Move the cursor to the next row, return false if no more row
                ProjectType type = new ProjectType();
                type.setId(rset.getInt("id"));
                type.setName(rset.getString("name"));
                type.setType(rset.getString("type"));
                projectType.add(type);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return projectType;
    }

    public List<Framework> getFramework(String name) {
        List<Framework> frameworks = new ArrayList<Framework>();
        try {
            Statement stmt = conn.createStatement();
            String strSelect = "select * from framework where ('" + name + "' like concat('%',core_library,'%'))";
            ResultSet rset = stmt.executeQuery(strSelect);
            while (rset.next()) {
                Framework framework = new Framework();
                framework.setId(rset.getInt("id"));
                framework.setName(rset.getString("name"));
                framework.setCoreLibrary(rset.getString("core_library"));
                framework.setTypeId(rset.getInt("type_id"));
                frameworks.add(framework);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return frameworks;
    }

    public List<EntryPoint> getEnteryPointsById(int id) {
        List<EntryPoint> entryPoints = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            String strSelect = "select * from entry_point where framework_id=" + id;
            ResultSet rset = stmt.executeQuery(strSelect);
            while (rset.next()) {
                EntryPoint entryPoint = new EntryPoint();
                entryPoint.setId(rset.getInt("id"));
                entryPoint.setName(rset.getString("name"));
                entryPoint.setType(rset.getString("type"));
                entryPoint.setLocation(rset.getString("location"));
                entryPoint.setFrameworkId(rset.getInt("framework_id"));
                entryPoints.add(entryPoint);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return entryPoints;
    }
}