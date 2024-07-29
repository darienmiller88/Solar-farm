package learn.solar.ui;

import java.util.List;
import java.util.Scanner;

import learn.solar.domain.PanelResult;
import learn.solar.models.Material;
import learn.solar.models.Panel;

public class View {
    private Scanner console;

    public enum Options{
        EXIT("0"), FIND("1"), ADD("2"), UPDATE("3"), REMOVE("4");

        Options(String optionNumber){
            this.optionNumber = optionNumber;
        }

        @Override
        public String toString(){
            return optionNumber;
        }

        private String optionNumber;
    }

    public int chooseOptionFromMenu(){
        System.out.println("Main Menu");
        System.out.println("==============");
        System.out.println("0. Exit");
        System.out.println("1. Find Panels by Section");
        System.out.println("2. Add a Panel");
        System.out.println("3. Update a Panel");
        System.out.println("4. Remove a Panel");
        System.out.print("Select [0-4]: ");
        String response = console.nextLine();

        while (response != "1" && response != "2" && response != "3" && response != "4") {
            System.out.println("Please select [0-4]");
            response = console.nextLine();
        }

        return Integer.parseInt(response);
    }

    public void printHeader(String menuHeader){
        System.out.println(menuHeader);
        System.out.println("================");
    }

    public void printResult(PanelResult result){
        System.out.println(result + "success: " + result.isSuccess());
    }

    public void printPanels(String sectionName, List<Panel> panels){
        System.out.println("Find Panels by Section");
        System.out.println("======================");
        for (Panel panel : panels) {
            if (panel.getSection().equals(sectionName)) {
                System.out.println(panel);
            }
        }
    }

    public Panel choosePanel(String sectionName, List<Panel> panels){
        return null;
    }

    public Panel makePanel(){
        System.out.println("Add a Panel");
        System.out.println("============");
        String sectionName = readRequiredString("Section: ");
        int row = readRequiredInt("Row: ");

        // while (row > 250 || row < 1) {
        //     System.out.print("Row must be between 1 and 250");
        //     row = readRequiredInt("Row: ");
        // }

        int column = readRequiredInt("Column: ");

        // while (column > 250 || column < 0) {
        //     System.out.print("Column must be between 0 and 250");
        //     row = readRequiredInt("Column: ");
        // }

        Material material = Material.fromMaterialName(console.nextLine());

        while (material == null) {
            System.out.println("Please type in appropriate material name ->");
            for (Material m : Material.values()) {
                System.out.println(m.toString());
            }

            material = Material.fromMaterialName(console.nextLine());
        }

        int installationYear = readRequiredInt("Installation year: ");

        while (installationYear > 2024 || installationYear < 1950) {
            System.out.println("Please enter an installation year between 1950 and 2024");
            installationYear = readRequiredInt("Installation year: ");
        }

        String tracked = readRequiredString("Tracked? [y/n]: ");
        Panel panel = new Panel(1, sectionName, row, column, installationYear, material, tracked.equals("y"));

        return panel;
    }

    public Panel update(Panel panel){
        return null;
    }
     
    public String readSection(){
        return "";
    }

    private String readString(String stringToRead) {
        return "";
    }

    public String readRequiredString(String requiredString){
        System.out.print(requiredString);
        String s = console.nextLine();

        while (s.equals("") || s.trim().length() == 0) {
            System.out.print(requiredString);
            s = console.nextLine();
        }

        return s;
    }

    public int readRequiredInt(String prompt){
        System.out.print(prompt);
        Integer num = readInt(console.nextLine());

        while (num == null || num < 1 || num > 250) {
            System.out.print("Please enter a number between 1 and 250: ");
            num = readInt(console.nextLine());
        }

        return num;
    }

    private Integer readInt(String num) {
        try {
            return Integer.parseInt(num);   
        } catch (NumberFormatException e) {
            return null;
        } 
    }

    private int readInt(String num, int min, int max){
        return 0;
    }

    private Material readMaterial(){
        return null;
    }
}
