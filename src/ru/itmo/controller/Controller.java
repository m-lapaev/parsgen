package ru.itmo.controller;

import ru.itmo.db.DBEngine;
import ru.itmo.gui.screens.DBSettings;
import ru.itmo.model.Field;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Администратор on 15.10.15.
 */
public class Controller {
    private static Controller controller;
    private Field[] fields = null;
    private ArrayList<String> parser;

    private Controller() {

    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private String tableName;

    public String getConnectionSettings() {
        return connectionSettings;
    }

    public void setConnectionSettings(String connectionSettings) {
        this.connectionSettings = connectionSettings;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String connectionSettings;
    private String user;
    private String password;

    public static synchronized Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = new Field[fields.size()];
        this.fields = fields.toArray(this.fields);
    }

    public Field[] getFields() {
        return this.fields;
    }

    private ArrayList<String> generateItem() {
        ArrayList<String> itemClass = new ArrayList<String>();

        itemClass.add("public class Item {");
        for (Field field : fields) {
            String dataType;
            switch (field.getDataType()) {
                case INTEGER:
                    dataType = "Integer";
                    break;
                case FLOAT:
                    dataType = "Float";
                    break;
                case STRING:
                    dataType = "String";
                    break;
                case BOOLEAN:
                    dataType = "Boolean";
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            itemClass.add("private " + dataType + " " + field.getName() + ";");
            itemClass.add("public " + dataType + " get" + field.getName() + "(){");
            itemClass.add("return this." +
                    ((field.getDataType() == DBSettings.DataType.STRING) && (!field.getLength().equals("DEFAULT SIZE"))
                            ? (field.getName() + ".substring(0," + (Integer.valueOf(field.getLength()) - 1) + ")") : field.getName())
                    + ";");
            itemClass.add("}");
            itemClass.add("public void set" + field.getName() + "(" + "String " + field.getName() + "){");
            itemClass.add("if (" + field.getName() + "!= null && " + field.getName() + ".length() >0) {");
            itemClass.add("this." + field.getName() + "=" + dataType + ".valueOf(" +
                    (dataType.equals("Float") ||
                            dataType.equals("Integer") ? ("removeAllNonDigits(" + field.getName() + ".replaceAll(\",\",\".\"))") :
                            field.getName()) + ");");
            itemClass.add("}");
            itemClass.add("}");
        }
        itemClass.add("private static String removeAllNonDigits(String value) {");
        itemClass.add("String result = \"\";");
        itemClass.add("char ch;");
        itemClass.add("for (int i = 0; i < value.length(); i++) {");
        itemClass.add("ch = value.charAt(i);");
        itemClass.add("if (ch >= '0' && ch <= '9' || ch == '.' || ch == ',') {");
        itemClass.add("result += ch;");
        itemClass.add("}");
        itemClass.add("}");
        itemClass.add("return result;");
        itemClass.add("}");
        itemClass.add("}");
        return itemClass;
    }

    private ArrayList<String> generateParser() {
        ArrayList<String> parserClass = new ArrayList<String>();
        parserClass.add("import java.io.IOException;");
        parserClass.add("import java.util.regex.Pattern;");
        parserClass.add("import java.util.ArrayList;");
        parserClass.add("import java.util.regex.Matcher;");
        parserClass.add("import org.jsoup.nodes.Element;");
        parserClass.add("import org.jsoup.Jsoup;");
        parserClass.add("import org.jsoup.nodes.Document;");
        parserClass.add("import org.jsoup.select.Elements;");
        parserClass.add("public class Parser {");
        parserClass.add("static DBEngine db = null;");
        parserClass.addAll(parser);
        parserClass.add("public static Item parseItem(String url){");
        parserClass.add("Item item = null;");
        parserClass.add("try {");
        parserClass.add("Document doc = Jsoup.connect(url).get();");
        parserClass.add("if(doc!=null){");
        parserClass.add("item = new Item();");
        parserClass.add("Elements el = null;");
        for (Field field : fields) {
            parserClass.addAll(field.getCodeSnippet());
        }
        parserClass.add("}");
        parserClass.add("} catch (IOException e) {");
        parserClass.add("e.printStackTrace();");
        parserClass.add("}");
        parserClass.add("return item;");
        parserClass.add("}");
        parserClass.addAll(generateSQLQuery());
        parserClass.add("}");
        return parserClass;
    }

    private ArrayList<String> generateSQLQuery() {
        ArrayList<String> query = new ArrayList<String>();
        String sql = "\"INSERT INTO \\\"" + tableName + "\\\"(";
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            sql += "\\\"" + field.getName() + "\\\"";
            if (i != fields.length - 1) {
                sql += ",";
            }
        }
        sql += ") VALUES (";
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            sql += "'\"+" + "item." + "get" + field.getName() + "()" + "+\"'";
            if (i != fields.length - 1) {
                sql += ",";
            }
        }
        sql += ")\"";
        query.add("private static boolean addToDB(Item item){");
        query.add("return db.processQuery(" + sql + ");");
        query.add("}");
        return query;
    }

    public void generateClasses() {
        writeClass("output\\Item.java", generateItem());
        writeClass("output\\Parser.java", generateParser());
    }

    private void writeClass(String filename, ArrayList<String> classLines) {
        try {
            FileWriter out = new FileWriter(filename);
            int offset = 0;
            for (String line : classLines) {
                if (line.contains("}")) {
                    offset -= 4;
                }
                for (int i = 0; i < offset; i++) {
                    out.write(" ");
                }
                out.write(line + "\n");
                if (line.contains("{")) {
                    offset += 4;
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setParser(ArrayList code) {
        this.parser = code;
    }
}
