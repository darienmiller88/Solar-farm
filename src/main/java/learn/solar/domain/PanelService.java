package learn.solar.domain;

import java.util.List;
import java.util.Objects;

import learn.solar.data.DataException;
import learn.solar.data.PanelFileRepository;
import learn.solar.models.Material;
import learn.solar.models.Panel;

public class PanelService {
    private PanelFileRepository repository;

    public PanelService(PanelFileRepository repository){
        this.repository = repository;
    }

    /**
     * 
     * Accepts a section, performs basic validation, and returns Solar Panels with that section.
     * 
     * @param section
     * @return Will return a list of Panels containing solar panels belonging to a certain section.
     * @throws DataException
     */
    public List<Panel> findBySection(String section) throws DataException{
        if (section == null || section.equals("")) {
            return null;
        }

        return repository.findBySection(section);
    }

    /**
     * Will validate user input before allowing the PanelRepository to insert the panel.
     * 
     * @param panelToAdd
     * @return Will return a PanelResult signifying either success, or failure with a list of error messages.
     * @throws DataException
     */
    public PanelResult add(Panel panelToAdd) throws DataException {
        PanelResult result = validate(panelToAdd);

        if (!result.isSuccess()) {
            return result;
        }

        List<Panel> panels = repository.findAllPanels();
        
        for (Panel panel : panels) {
            if (panel.getRow() == panelToAdd.getRow() && panel.getColumn() == panelToAdd.getColumn()) {
                result.addErrorMessage("Row and column are currently taken by another Panel.");

                return result;
            }
        }

        repository.add(panelToAdd);
        result.setPanel(panelToAdd);

        return result;
    }

    /**
     * Update will accept a Panel with a given id, with represents the id of the Solar Panel in the farm to update.
     * 
     * @param panelToAdd
     * @return Will return a PanelResult signifying either success, or failure with a list of error messages. 
     * @throws DataException
     */
    public PanelResult update(Panel panelToAdd) throws DataException {
        PanelResult result = validate(panelToAdd);

        if (!result.isSuccess()) {
            return result;
        }

        if (!repository.update(panelToAdd)) {
            result.addErrorMessage("Id of given panel does not exist in Solar Panel Farm.");

            return result;
        }

        return result;
    }

    //-- pass-through to repository
    public PanelResult deleteById(int id) throws DataException {
        PanelResult result = new PanelResult();

        if (id < 0) {
            result.addErrorMessage("id cannot be negative.");

            return result;
        }

        if (!repository.deleteById(id)) {
            result.addErrorMessage("id does not belong to any solar panel in solar farm.");
        } 

        return result;
    }

    private PanelResult validate(Panel panel){
        PanelResult result = new PanelResult();

        if (panel == null) {
            result.addErrorMessage("Panel cannot be null.");
            return result;
        }

        if ((panel.getColumn() > 250 || panel.getColumn() < 0) || (panel.getRow() > 250 || panel.getRow() < 0)) {
            result.addErrorMessage("Row and column must be between 0 and 250.");
            return result;
        }

        if (panel.getInstallationYear() > 2024 || panel.getInstallationYear() < 1950) {
            result.addErrorMessage("Installation year must be between 1950 and 2024.");
            return result;
        }

        if (panel.getSection() == null || panel.getSection().equals("") ) {
            result.addErrorMessage("Section is required.");

            return result;
        }

        if (panel.getId() < 0) {
            result.addErrorMessage("Panel id cannot be negative.");
            return result;
        }

        return result;
    }

    public static void main(String[] args) throws DataException {
        PanelFileRepository repository = new PanelFileRepository("./data/testPanels.csv");
        PanelService service = new PanelService(repository);
        // Panel panel = new Panel(1, "Section", 12, 12, 2021,  Material.ASi, false);

        // service.add(panel);

        // Panel panel2 = new Panel(1, "Section", 13, 12, 2021,  Material.ASi, false);

        // PanelResult expected = new PanelResult();
        // PanelResult actual = service.add(panel2);

        // expected.addErrorMessage("Row and column are currently taken by another Panel."); 

        // System.out.println("Expected: " + expected + " actual: " + actual );
        // System.out.println(Objects.equals(expected, actual));

        Panel panel = new Panel(11, "TestSection", 12, 12, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(22, "TestSection", 14, 14, 2021,  Material.ASi, false);
        
        service.add(panel);
        service.add(panel2);

        Panel updatedPanel = new Panel(22, "TestSection", 130, 234, 2000,  Material.CdTe, true);

        PanelResult result = service.update(updatedPanel);

        System.out.println(result);

        List<Panel> panels = repository.findAllPanels();

        for (Panel p : panels) {
            System.out.println(p);
        }

        // System.out.println("contains updated panel: " + panels.contains(updatedPanel));
    }
}
