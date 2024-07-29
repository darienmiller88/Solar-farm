package learn.solar.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// import main.java.learn.solar.domain.PanelResult;
import learn.solar.models.Material;
import learn.solar.models.Panel;

public class PanelFileRepository implements PanelRepository{
    private String filePath;
    private static final String HEADER = "id,section,row,column,installation_year,material,tracking";

    public PanelFileRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Given a certain section, return a list of panels belonging to that section.
     * 
     * @param section
     * @return a list of panels belonging to a certian section
     * @throws DataException
     */
    public List<Panel> findBySection(String section) throws DataException{
        List<Panel> panelsBySection = new ArrayList<>();
        List<Panel> allPanels = findAll();
        
        for (Panel panel : allPanels) {
            if (panel.getSection().equals(section)) {
                panelsBySection.add(panel);
            }
        }

        return panelsBySection;
    }

    /**
     * Will append a panel to the end of the csv.
     * 
     * @param panel
     */
    public void add(Panel panel){
        String panelStringified = serialize(panel);
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(filePath, true));

            writer.println(panelStringified);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            writer.close();
        }
    }

    /**
     * Will take a panel, and overwrite an existing one with a given id.
     * 
     * @param panelToUpdate
     * @return true or false depending on whether or not the id of the panel exists
     * @throws DataException
     */
    public boolean update(Panel panelToUpdate) throws DataException{
        List<Panel> panels = findAll();

        for (int i = 0; i < panels.size(); i++) {
            if (panels.get(i).getId() == panelToUpdate.getId()) {
                panels.set(i, panelToUpdate);
                overwriteCsvWithNewData(panels);

                return true;
            }
        }

        return false;
    }

    /**
     * Will overwrite the csv with one less panel provided the id of the panel to be deleted exists.
     * 
     * @param id
     * @return true or false if id is valid
     * @throws DataException
     */
    public boolean deleteById(int id) throws DataException{
        List<Panel> panels = findAll();

        for (int i = 0; i < panels.size(); i++) {
            if (panels.get(i).getId() == id) {
                panels.remove(id);
                overwriteCsvWithNewData(panels);

                return  true;
            }
        }

        return false;
    }

    /**
     * Description: Will take in a list of panels to overwrite the csv file with new data.
     * 
     * @param panels
     */
    private void overwriteCsvWithNewData(List<Panel> panels){
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(filePath);

            writer.println(HEADER);
            for (Panel panel : panels) {
                writer.println(serialize(panel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            writer.close();
        }
    }

    // -- finds all Panels in the data source (file), does not need to be public
    private List<Panel> findAll() throws DataException{
        ArrayList<Panel> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            //Skip the header in the csv file.
            reader.readLine(); 

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Panel panel = deserialize(line);

                if (panel != null) {
                    result.add(panel);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new DataException(ex.getMessage(), ex);
        }

        return result;
    }

    // -- convert a Panel into a String (a line) in the file
    private String serialize(Panel panel) {
        return String.format("%s,%s,%s,%s,%s,%s,%s", 
                panel.getId(),
                removeCommas(panel.getSection()),
                panel.getRow(),
                panel.getColumn(),
                panel.getInstallationYear(),
                panel.getMaterial().toString(),
                panel.isTracking() ? 1 : 0
        );
    }

    private Panel deserialize(String panelString){
        String[] elements = panelString.split(",");
        Panel panel = new Panel();
        
        //The first item in a line read from the csv file will be id of the panel, which should be parsed into an int.
        panel.setId(Integer.parseInt(elements[0]));

        //The second item should be the section, which will be read as a string.
        panel.setSection(elements[1]);

        //The third item should be the row in the the panel is in.
        panel.setRow(Integer.parseInt(elements[2]));

        //The fourth item should be the column in the the panel is in.
        panel.setColumn(Integer.parseInt(elements[3]));

        //The fifth item should be the row in the the panel is in.
        panel.setInstallationYear(Integer.parseInt(elements[4]));

        //The sixth item should be the material to be used for the solar panel.
        panel.setMaterial(Material.fromMaterialName(elements[5]));

        //The seventh and final item should be whether or not the solar panel is tracking.
        panel.setTracking(Integer.parseInt(elements[6]) == 1);

        return panel;
    }

    private String removeCommas(String line){
        if (line.contains(",")) {
            System.out.println("line: " + line);
            return line.replace(",", "");
        }

        return line;
    }

    public static void main(String[] args) throws DataException {
        PanelFileRepository repository = new PanelFileRepository("./data/panels.csv");

        List<Panel> panels = repository.findAll();
        
        System.out.println("initial list of panels");
        for (Panel panel : panels) {
            System.out.println(panel);
        }

        Panel p = new Panel(12, "New One's", 3, 10, 2010, Material.CIGS, false);
        Panel p2 = new Panel(13, "New One's", 5, 10, 2012, Material.CdTe, true);
        Panel p3 = new Panel(14, "New One's", 2, 13, 2024, Material.ASi, true);
        Panel p4 = new Panel(15, "High,Tech", 105, 20, 2001, Material.MonoSi, false);

        repository.add(p);
        repository.add(p2);
        repository.add(p3);
        repository.add(p4);
        panels = repository.findAll();

        System.out.println("\nPanels after adding two");
        for (Panel panel : panels) {
            System.out.println(panel);
        }   
    }
}
