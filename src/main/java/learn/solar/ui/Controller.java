package learn.solar.ui;

import java.util.List;
import java.util.Scanner;

import learn.solar.data.DataException;
import learn.solar.domain.PanelResult;
import learn.solar.domain.PanelService;
import learn.solar.models.Material;
import learn.solar.models.Panel;

public class Controller {
    private View view;
    private PanelService service;
    private Scanner console;
    private final int EXIT = 0, FIND_PANEL = 1, ADD_PANEL = 2, UPDATE_PANEL = 3, REMOVE_PANEL = 4;

    public Controller(View view, PanelService service){
        this.view = view;
        this.service = service;
        console = new Scanner(System.in);
    }

    public void run() throws DataException{ 
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
                    updatePanel();
                    break;
                case REMOVE_PANEL:
                    deletePanel();
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
            System.out.printf("Panel %s-%s-%s added\n", panel.getSection(), panel.getRow(), panel.getColumn());
       }else{
            System.out.println(result);
       }
    }

    private void updatePanel() throws DataException{
        System.out.println("Update a Panel");
        System.out.println("==============");

        String sectionName = readRequiredString("Section: ");
        int row = view.readRequiredInt("Row: ");
        int column = view.readRequiredInt("Column: ");

        List<Panel> panels = service.findAllPanels();
        Panel panelToUpdate = null;

        for (Panel panel : panels) {
            if (panel.getSection().equals(sectionName) && panel.getRow() == row && panel.getColumn() == column) {
                panelToUpdate = panel;
                break;
            }
        }

        if (panelToUpdate == null) {
            System.out.printf("Panel %s-%s-%s does not exist.", sectionName, row, column);
        } else{
            System.out.printf("Editing %s-%s-%s", sectionName, row, column);
            System.out.println("Press [Enter] to keep original value.");

            System.out.print("Section (" + sectionName + "): ");
            String newSectionName = console.nextLine();

            System.out.print("Row (" + row + "): ");
            int newRow = console.nextInt();
            console.nextLine();

            System.out.print("Column (" + column + "): ");
            int newColumn = console.nextInt();
            console.nextLine();

            System.out.print("Material (" + panelToUpdate.getMaterial().toString() + "): ");
            Material newMaterial = Material.fromMaterialName(console.nextLine());

            System.out.print("Installation Year (" + panelToUpdate.getInstallationYear() + "): ");
            int newInstallationYear = console.nextInt();
            console.nextLine();

            System.out.print("Tracked (" + (panelToUpdate.isTracking() ? "yes" : "no") + ") [y/n]: ");
            String newTracking = console.nextLine();

            panelToUpdate.setSection(newSectionName.equals("") ? panelToUpdate.getSection() : newSectionName);
            panelToUpdate.setRow(newRow);
            panelToUpdate.setColumn(newColumn);
            panelToUpdate.setMaterial(newMaterial == null ? panelToUpdate.getMaterial() : newMaterial);
            panelToUpdate.setInstallationYear(newInstallationYear);
            panelToUpdate.setTracking(newTracking.equals("y"));

            PanelResult result = service.update(panelToUpdate);

            if (!result.isSuccess()) {
                System.out.println(result);
            }else{
                System.out.printf("Panel %s-%s-%s updated!\n", panelToUpdate.getSection(), panelToUpdate.getRow(), panelToUpdate.getColumn());
            }
        }   
    }

    private void deletePanel() throws DataException{
        System.out.println("Remove a Panel");
        System.out.println("==============");

        String sectionName = readRequiredString("Section: ");
        int row = view.readRequiredInt("Row: ");
        int column = view.readRequiredInt("Column: ");

        List<Panel> panels = service.findAllPanels();
        Panel panelToDelete = null;

        for (Panel panel : panels) {
            if (panel.getSection().equals(sectionName) && panel.getRow() == row && panel.getColumn() == column) {
                panelToDelete = panel;
                break;
            }
        }

        if (panelToDelete == null) {
            System.out.printf("Panel %s-%s-%s does not exist.", sectionName, row, column);
        }else{
            PanelResult result = service.deleteById(panelToDelete.getId());

            if (!result.isSuccess()) {
                System.out.println(result);
            }else{
                System.out.printf("Panel %s-%s-%s removed!\n", sectionName, row, column);
            }
        }
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