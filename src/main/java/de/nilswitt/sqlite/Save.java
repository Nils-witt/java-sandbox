package de.nilswitt.sqlite;

public class Save {


    public static void main(String[] args) {
        DBHandler.connect();
        DBHandler.clear();
        DBHandler.initTables();


        DemoComponent demoComponent = new DemoComponent("TestHeader", "TestFooter", 123312);

        DBHandler.saveGuiComponentMain(demoComponent);
        System.out.println(demoComponent.getValues());

        DBHandler.disconnect();
    }
}
