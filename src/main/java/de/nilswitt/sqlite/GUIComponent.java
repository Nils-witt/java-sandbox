package de.nilswitt.sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GUIComponent {

    private final double posX = 30;
    private final double posY = 220;
    private final int id;

    public GUIComponent(int id) {
        this.id = id;
        setUpHandlers();
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return posX;
    }

    public double getY() {
        return posY;
    }


    public HashMap<String, String> getValues() {
        HashMap<String, String> hs = new HashMap<>();

        Field[] fields = getAllFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {

                if (field.getType() == String.class) {
                    hs.put(field.getName(), (String) field.get(this));
                } else if (field.getType() == int.class || field.getType() == Double.class) {
                    hs.put(field.getName(), String.valueOf(field.get(this)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return hs;
    }


    public void loadFromValues(HashMap<String, String> values) {
        values.forEach((key, value) -> {
            Field field = null;
            try {
                field = this.getClass().getDeclaredField(key);
            } catch (NoSuchFieldException e) {
                // e.printStackTrace();
            }
            try {
                field = GUIComponent.class.getDeclaredField(key);
            } catch (NoSuchFieldException e) {
                // e.printStackTrace();
            }
            if (key.equals("id")) {
                field = null;
            }
            if (field != null) {
                try {
                    field.setAccessible(true);
                    if (field.getType() == String.class) {
                        field.set(this, value);
                    } else if (field.getType() == int.class) {
                        field.set(this, Double.valueOf(value).intValue());
                    } else if (field.getType() == Double.class) {
                        field.set(this, Double.valueOf(value));
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(key + ": N/A");
            }
        });
    }

    public Field[] getAllFields() {
        ArrayList<Field> fieldArrayList = new ArrayList<>();

        Class current = this.getClass();
        while (current != Object.class) {
            fieldArrayList.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fieldArrayList.toArray(new Field[0]);
    }


    public void setUpHandlers() {

    }
}
