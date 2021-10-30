package de.nilswitt.sqlite;

public class Update {

    public static void main(String[] args) {
        DBHandler.connect();


        DemoComponent demoComponent = new DemoComponent(123312);

        demoComponent.loadFromValues(DBHandler.getComponentValues(demoComponent.getId()));

        demoComponent.setHeader("new Header");
        demoComponent.setFooter("mew Footer");
        System.out.println(demoComponent.getValues());

        DBHandler.saveGuiComponentMain(demoComponent);
        DBHandler.disconnect();
    }
}
