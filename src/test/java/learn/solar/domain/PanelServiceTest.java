package learn.solar.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import learn.solar.data.DataException;
import learn.solar.data.PanelFileRepository;
import learn.solar.models.Material;
import learn.solar.models.Panel;

public class PanelServiceTest {
    PanelFileRepository repository = new PanelFileRepository("./data/testPanels.csv");
    PanelService service = new PanelService(repository);
    int numInitialPanels;

    @BeforeEach
    void setup() throws DataException {
        numInitialPanels = repository.findAllPanels().size();
    }
 
    @Test
    void shouldAddPanel() throws DataException{
        PanelResult result = new PanelResult();
        Panel panel = new Panel(1, "TestSection", 12, 12, 2021,  Material.ASi, false);

        assertEquals(result.isSuccess(), service.add(panel).isSuccess());
        assertEquals(numInitialPanels + 1, repository.findAllPanels().size());
        assertTrue(service.deleteById(1).isSuccess());
    }

    @Test
    void shouldNotAddPanelWhenNull() throws DataException{
        PanelResult result = new PanelResult();

        result.addErrorMessage("Panel cannot be null.");
        assertEquals(result, service.add(null)); 
    }

    @Test
    void shouldNotAddEmptyOrNullSection() throws DataException{
        PanelResult result = new PanelResult();
        Panel panel = new Panel(1, null, 12, 12, 2021,  Material.ASi, false);

        result.addErrorMessage("Section is required.");
        assertEquals(result, service.add(panel)); 

        Panel panel2 = new Panel(1, "", 12, 12, 2021,  Material.ASi, false);
        PanelResult result2 = new PanelResult();
        
        result2.addErrorMessage("Section is required.");
        assertEquals(result2, service.add(panel2)); 
    }

    @Test
    void shouldNotAddInvalidRowOrColumn() throws DataException{
        //Testing invalid row.
        PanelResult result = new PanelResult();
        Panel panel = new Panel(1, "TestSection", 1200, -12, 2021,  Material.ASi, false);

        result.addErrorMessage("Row and column must be between 0 and 250.");
        assertEquals(result.isSuccess(), service.add(panel).isSuccess());

        //Testing invalid column.
        Panel panel2 = new Panel(1, "TestSection", -120, 1200, 2021,  Material.ASi, false);
        PanelResult result2 = new PanelResult();

        result2.addErrorMessage("Row and column must be between 0 and 250.");
        assertEquals(result2, service.add(panel2));

        //Testing invalid row and column.
        Panel panel3 = new Panel(1, "TestSection", 1200, 1200, 2021,  Material.ASi, false);
        PanelResult result3 = new PanelResult();

        result3.addErrorMessage("Row and column must be between 0 and 250.");
        assertEquals(result3, service.add(panel3));
    }

    @Test
    void shouldNotAddNegativeId() throws DataException{
        Panel panel = new Panel(-1, "TestSection", 120, 100, 2021,  Material.ASi, false);
        PanelResult result = new PanelResult();

        result.addErrorMessage("Panel id cannot be negative.");
        assertEquals(result, service.add(panel));
    }

    @Test
    void shouldNotAddInvalidYear() throws DataException{
        Panel panel = new Panel(-1, "TestSection", 120, 120, 3021,  Material.ASi, false);
        PanelResult result = new PanelResult();

        result.addErrorMessage("Installation year must be between 1950 and 2024.");
        assertEquals(result, service.add(panel));
    }
    
    @Test
    void shouldNotAddWhenRowColumnTaken() throws DataException{
        Panel panel = new Panel(1, "Section", 12, 12, 2021,  Material.ASi, false);

        assertEquals(new PanelResult().isSuccess(), service.add(panel).isSuccess());

        Panel panel2 = new Panel(2, "Section", 12, 12, 2021,  Material.ASi, false);
        PanelResult result = new PanelResult();
        
        result.addErrorMessage("Row and column are currently taken by another Panel.");
        assertEquals(result, service.add(panel2)); 
    }
    
    @Test
    void shouldNotFindPanelsBySectionIfNullOrEmpty()throws DataException{
        assertEquals(null, service.findBySection(null)); 
        assertEquals(null, service.findBySection("")); 
    }  

    @Test
    void shouldNotFindPanelsWhenSectionNotFound() throws DataException{
        Panel panel = new Panel(11, "TestSection", 124, 212, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(22, "TestSection", 145, 140, 2021,  Material.ASi, false);
        assertEquals(new PanelResult().isSuccess(), service.add(panel).isSuccess());         

        assertEquals(new PanelResult().isSuccess(), service.add(panel2).isSuccess());    
        assertEquals(new ArrayList<>(), service.findBySection("DoesNotExistSection")); 

        assertTrue(service.deleteById(11).isSuccess());
        assertTrue(service.deleteById(22).isSuccess());
    }

    @Test
    void shouldFindPanelsWithValidSection() throws DataException{
        Panel panel = new Panel(1, "TestSection", 99, 12, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(2, "TestSection", 133, 13, 2021,  Material.ASi, false);
        Panel panel3 = new Panel(3, "Lower hill", 14, 144, 2021,  Material.ASi, false);

        assertEquals(new PanelResult().isSuccess(), service.add(panel).isSuccess());         
        assertEquals(new PanelResult().isSuccess(), service.add(panel2).isSuccess());         
        assertEquals(new PanelResult().isSuccess(), service.add(panel3).isSuccess());         

        List<Panel> panels = Arrays.asList(
            new Panel(1, "TestSection", 99, 12, 2021, Material.ASi, false),
            new Panel(2, "TestSection", 133, 13, 2021, Material.ASi, false)
        );

        assertEquals(panels.size(), service.findBySection("TestSection").size());
        assertTrue(service.deleteById(1).isSuccess());
        assertTrue(service.deleteById(2).isSuccess());
        assertTrue(service.deleteById(3).isSuccess());
    }

    @Test
    void shouldNotDeleteIfIdDoesNotExist() throws DataException{
        Panel panel = new Panel(1, "TestSection", 12, 12, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(2, "TestSection", 13, 13, 2021,  Material.ASi, false);
       
        service.add(panel);
        service.add(panel2);

        PanelResult result = new PanelResult();

        result.addErrorMessage("id does not belong to any solar panel in solar farm.");
        assertEquals(result, service.deleteById(99));
        
        //Reset csv to default.
        assertTrue(service.deleteById(1).isSuccess());
        assertTrue(service.deleteById(2).isSuccess());
    }

    @Test
    void shouldNotDeleteIfIdIsNegative() throws DataException{
        Panel panel = new Panel(1, "TestSection", 12, 12, 2021,  Material.ASi, false);
        PanelResult result = new PanelResult();

        service.add(panel);
        
        result.addErrorMessage("id cannot be negative.");
        assertEquals(result, service.deleteById(-1));
        
        //Reset csv to default.
        assertTrue(service.deleteById(1).isSuccess());
    }

    @Test
    void shouldDeletePanelIfIdExists() throws DataException{
        Panel panel = new Panel(1, "TestSection", 12, 12, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(2, "TestSection", 13, 13, 2021,  Material.ASi, false);
       
        service.add(panel);
        service.add(panel2);
        
        //Reset csv to default.
        assertTrue(service.deleteById(1).isSuccess());
        assertTrue(service.deleteById(2).isSuccess());   
    }

    @Test
    void shouldUpdateIfIdExist() throws DataException{
        Panel panel = new Panel(1, "TestSection", 12, 12, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(2, "TestSection", 13, 13, 2021,  Material.ASi, false);
       
        service.add(panel);
        service.add(panel2);

        Panel updatedPanel = new Panel(2, "TestSection", 130, 234, 2000,  Material.CdTe, true);

        //Update successful
        assertEquals(new PanelResult(), service.update(updatedPanel));

        List<Panel> panels = repository.findAllPanels();
        
        //Check to see if the updated panel exists in the csv.
        assertTrue(panels.contains(updatedPanel));

        //Reset csv to default.
        assertTrue(service.deleteById(1).isSuccess());
        assertTrue(service.deleteById(2).isSuccess()); 
    }

    @Test
    void shouldNotUpdateIfIdDoesNotExist() throws DataException{
        Panel panel = new Panel(1, "TestSection", 12, 12, 2021,  Material.ASi, false);
       
        service.add(panel);

        Panel updatedPanel = new Panel(32, "TestSection", 130, 234, 2000,  Material.CdTe, true);
        PanelResult result = new PanelResult();

        result.addErrorMessage("Id of given panel does not exist in Solar Panel Farm.");
        assertEquals(result, service.update(updatedPanel));

        List<Panel> panels = repository.findAllPanels();
        
        assertFalse(panels.contains(updatedPanel));

        //Reset csv to default.
        assertTrue(service.deleteById(1).isSuccess());
    }

    @Test
    void shouldNotUpdateIfInvalidYear() throws DataException{
        Panel panel = new Panel(-1, "TestSection", 120, 120, 3021,  Material.ASi, false);
        PanelResult result = new PanelResult();

        result.addErrorMessage("Installation year must be between 1950 and 2024.");
        assertEquals(result, service.update(panel));
    }

    @Test
    void shouldNotUpdateInvalidRowOrColumn() throws DataException{
        //Testing invalid row.
        PanelResult result = new PanelResult();
        Panel panel = new Panel(1, "TestSection", 1200, -12, 2021,  Material.ASi, false);

        result.addErrorMessage("Row and column must be between 0 and 250.");
        assertEquals(result, service.update(panel));

        //Testing invalid column.
        Panel panel2 = new Panel(1, "TestSection", -120, 1200, 2021,  Material.ASi, false);
        PanelResult result2 = new PanelResult();

        result2.addErrorMessage("Row and column must be between 0 and 250.");
        assertEquals(result2, service.add(panel2));

        //Testing invalid row and column.
        Panel panel3 = new Panel(1, "TestSection", 1200, 1200, 2021,  Material.ASi, false);
        PanelResult result3 = new PanelResult();

        result3.addErrorMessage("Row and column must be between 0 and 250.");
        assertEquals(result3, service.add(panel3));
    }

    @Test
    void shouldNotUpdateNegativeId() throws DataException{
        Panel panel = new Panel(-1, "TestSection", 120, 100, 2021,  Material.ASi, false);
        PanelResult result = new PanelResult();

        result.addErrorMessage("Panel id cannot be negative.");
        assertEquals(result, service.update(panel));
    }

    @Test
    void shouldNotUpdatePanelWhenNull() throws DataException{
        PanelResult result = new PanelResult();

        result.addErrorMessage("Panel cannot be null.");
        assertEquals(result, service.update(null)); 
    }

    @Test
    void shouldNotUpdateEmptyOrNullSection() throws DataException{
        PanelResult result = new PanelResult();
        Panel panel = new Panel(1, null, 12, 12, 2021,  Material.ASi, false);

        result.addErrorMessage("Section is required.");
        assertEquals(result, service.update(panel)); 

        Panel panel2 = new Panel(1, "", 12, 12, 2021,  Material.ASi, false);
        PanelResult result2 = new PanelResult();
        
        result2.addErrorMessage("Section is required.");
        assertEquals(result2, service.update(panel2)); 
    }
}
