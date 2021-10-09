package de.nilswitt.sqlite;


import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHandler {

    private static final String dbUrl = "jdbc:sqlite:components.db";
    public static Connection conn = null;


    public static void connect() {

        try {
            conn = DriverManager.getConnection(dbUrl);
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void saveGuiComponentMain(GUIComponent component) {
        ArrayList<String> tables = getTables();

        String componentClassName = component.getClass().getSimpleName();

        if (!tables.contains(componentClassName)) {
            createComponentTable((Class<GUIComponent>) component.getClass());
        }

        int cCid = createComponentClassEntry(component);

        createGuiCompoentEntry(component.getId(), componentClassName, component.getX(), component.getY(), cCid);
    }


    private static void createGuiCompoentEntry(int componentId, String className, double posX, double posY, int cCId) {
        String sql = "INSERT INTO Components(componentId,className, axisX, axisY, cCId) VALUES(?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, componentId);
            pstmt.setString(2, className);
            pstmt.setDouble(3, posX);
            pstmt.setDouble(4, posY);
            pstmt.setInt(5, cCId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int createComponentClassEntry(GUIComponent guiComponent) {
        Class<GUIComponent> guiCompoentClass = (Class<GUIComponent>) guiComponent.getClass();
        Field[] fields = guiCompoentClass.getDeclaredFields();


        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(guiCompoentClass.getSimpleName());
        sql.append(" (");

        for (int i = 0; i < fields.length; i++) {
            if (i != 0) {
                sql.append(",");
            }
            sql.append(fields[i].getName());
        }

        sql.append(")");
        sql.append(" VALUES(");


        for (int i = 0; i < fields.length; i++) {
            if (i != 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");

        System.out.println(sql);

        HashMap<String, String> valueMap = guiComponent.getValues();

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < fields.length; i++) {
                String value = valueMap.getOrDefault(fields[i].getName(), "");
                pstmt.setString(i + 1, value);
            }
            int rt = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    private static void createComponentTable(Class<GUIComponent> guiComponent) {

        Field[] fields = guiComponent.getDeclaredFields();

        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(guiComponent.getSimpleName());
        sql.append(" (");
        sql.append(" id integer PRIMARY KEY");
        for (Field field : fields) {
            sql.append(", ").append(field.getName());
        }
        sql.append(");");

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clear() {
        ArrayList<String> tables = getTables();

        for (String table : tables) {
            String deleteTable = "DROP TABLE " + table + ";";
            try {
                Statement stmt = conn.createStatement();
                stmt.execute(deleteTable);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    }

    public static ArrayList<String> getTables() {
        ArrayList<String> tables = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = conn.getMetaData().getTables(null, null, null, null);

            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tables;
    }

    public static void initTables() {

        String sql = "CREATE TABLE Components (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	className text,\n"
                + " guiId text,\n"
                + " axisX integer,\n"
                + " axisY integer,\n"
                + " componentId integer,\n"
                + " cCId integer\n"
                + ");";

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static HashMap<String, String> getComponentValues(int componentId) {
        HashMap<String, String> values = new HashMap<>();
        String sql = "SELECT * FROM Components WHERE componentId = ?;";


        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, componentId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                values = new HashMap<>();
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    String keyName = rsmd.getColumnName(i);
                    if (rsmd.getColumnType(i) == 12) {
                        values.put(keyName, rs.getString(keyName));
                    }
                    if (rsmd.getColumnType(i) == 4) {
                        values.put(keyName, String.valueOf(rs.getDouble(keyName)));
                    }
                }
                String className = rs.getString("className");
                int cCid = rs.getInt("cCId");

                values.putAll(getClassComponentValues(cCid, className));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return values;
    }

    public static HashMap<String, String> getClassComponentValues(int cCid, String className) {
        HashMap<String, String> values = new HashMap<>();
        String sql = "SELECT * FROM " + className + " WHERE id = ?";


        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cCid);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                values = new HashMap<>();
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                    String keyName = rsmd.getColumnName(i);
                    if (rsmd.getColumnType(i) == 12) {
                        values.put(keyName, rs.getString(keyName));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return values;
    }
}

