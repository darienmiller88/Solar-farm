package learn.solar.ui;

import java.util.List;
import java.util.Scanner;

import learn.solar.domain.PanelResult;
import learn.solar.models.Material;
import learn.solar.models.Panel;

public class View {
    private Scanner console;

    public int chooseOptionFromMenu(){
        System.out.println("Main Menu");
        System.out.println("==============");
        System.out.println("0. Exit");
        System.out.println("1. Exit");
        System.out.println("2. Exit");
        System.out.println("3. Exit");
        System.out.println("4. Exit");


        return 0;
    }

    public void printHeader(String menuHeader){
        System.out.println(menuHeader);
        System.out.println("================");
    }

    public void printResult(PanelResult result){
        System.out.println(result + "success: " + result.isSuccess());
    }

    public void printPanels(String sectionName, List<Panel> panels){
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
        return null;
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

    private String readRequiredString(String requiredString){
        return "";
    }

    private int readInt(String num) {
        return 0;
    }

    private int readInt(String num, int min, int max){
        return 0;
    }

    private Material readMaterial(){
        return null;
    }
}
