package learn.solar.ui;

import java.util.List;
import java.util.Scanner;

import learn.solar.data.DataException;
import learn.solar.domain.PanelResult;
import learn.solar.domain.PanelService;
import learn.solar.models.Panel;

public class Controller {
    private View view;
    private PanelService service;
    private Scanner console;
    public enum Options{
        EXIT(0), FIND(1), ADD(2), UPDATE(3), REMOVE(4);

        Options(int optionNumber){
            this.optionNumber = optionNumber;
        }

        private int optionNumber;
    }

    private final int EXIT = 0, FIND_PANEL = 1, ADD_PANEL = 2, UPDATE_PANEL = 3, REMOVE_PANEL = 4;

    public Controller(View view, PanelService service){
        this.view = view;
        this.service = service;
    }

    public void run(){ 
        System.out.println("Welcome to Solar Farm");
        System.out.println("===================");

        int option = view.chooseOptionFromMenu();

        while (option != EXIT) {
            switch (option) {
                case EXIT:
                    continue;
                case FIND_PANEL:
                    viewBySection();
                    break;
                case ADD_PANEL:
                    addPanel();
                    break;
                case UPDATE_PANEL:

                    break;
                case REMOVE_PANEL:

                    break;
            
                default:
                    System.out.println("Unrecognized menu option.");
                    break;
            }

            option = view.chooseOptionFromMenu();
        }

        System.out.println("Goodbye!");
    }

    private void viewBySection(){
        System.out.println("Find panels by section");
        System.out.println("======================");
        System.out.println();
        System.out.print("Section name: ");
        String sectionName = console.nextLine();

        try {
            List<Panel> panels = service.findBySection(sectionName);

            if (panels.size() == 0) {
                System.out.println("There are no panels belonging to section " + sectionName + ".");
            }else{
                System.out.println("\nPanels in \"" + sectionName + "\"");
                System.out.println("Row Col Year Material Tracking");
                for (Panel panel : panels) {
                    System.out.println(panel);
                }
            }
        } catch (DataException e) {
            e.printStackTrace();
        }
    }

    private void addPanel() throws DataException{
       Panel panel = view.makePanel();
       List<Panel> panels = service.findAllPanels();

       if (panels.size() > 0) {
            panel.setId(panels.get(panels.size() - 1).getId() + 1);
       }else{
            panel.setId(1);
       }

       PanelResult result = service.add(panel);

       if (result.isSuccess()) {
            System.out.printf("Panel %s-%s-%s added", panel.getSection(), panel.getRow(), panel.getColumn())
       }else{
            System.out.println(result);
       }
    }

    private void updatePanel(){
        
    }

    private void deletePanel(){

    }

    public String readRequiredString(String prompt){
        System.out.print(prompt);
        String requiredString = console.nextLine();

        while (requiredString.equals("") || requiredString.trim().length() == 0) {
            System.out.print(prompt);
            requiredString = console.nextLine();
        }

        return requiredString;
    }
}