package de.nilswitt.sqlite;

public class Load {

    public static void main(String[] args) {
        DBHandler.connect();
        DBHandler.initTables();


        DemoComponent demoComponent = new DemoComponent(123312);

        demoComponent.loadFromValues(DBHandler.getComponentValues(demoComponent.getId()));

        DBHandler.disconnect();


    }
}
