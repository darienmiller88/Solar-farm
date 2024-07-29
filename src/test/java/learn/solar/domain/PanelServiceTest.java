package learn.solar.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Result;

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

        assertEquals(result, service.add(panel));
        assertEquals(numInitialPanels + 1, repository.findAllPanels().size());
    }

    @Test
    void shouldNotAddPanelWhenNull() throws DataException{
        PanelResult result = new PanelResult();

        result.addErrorMessage("Panel cannot be null.");
        assertEquals(result, service.add(null)); 
    }

    @Test
    void shouldNotEmptyOrNullSection() throws DataException{
        PanelResult result = new PanelResult();
        Panel panel = new Panel(1, null, 12, 12, 2021,  Material.ASi, false);

        result.addErrorMessage("Section is required.");
        assertEquals(result, service.add(panel)); 

        Panel panel2 = new Panel(1, "", 12, 12, 2021,  Material.ASi, false);
        PanelResult result2 = new PanelResult();
        
        result.addErrorMessage("Section is required.");
        assertEquals(result2, service.add(panel2)); 
    }

    @Test
    void shouldNotAddInvalidRowOrColumn() throws DataException{
        //Testing invalid row.
        PanelResult result = new PanelResult();
        Panel panel = new Panel(1, "TestSection", 1200, 12, 2021,  Material.ASi, false);

        result.addErrorMessage("Row and column must be between 0 and 250");
        assertEquals(result, service.add(panel));

        //Testing invalid column.
        Panel panel2 = new Panel(1, "TestSection", 120, 1200, 2021,  Material.ASi, false);
        PanelResult result2 = new PanelResult();

        result2.addErrorMessage("Row and column must be between 0 and 250");
        assertEquals(result2, service.add(panel2));

        //Testing invalid row and column.
        Panel panel3 = new Panel(1, "TestSection", 1200, 1200, 2021,  Material.ASi, false);
        PanelResult result3 = new PanelResult();

        result3.addErrorMessage("Row and column must be between 0 and 250");
        assertEquals(result3, service.add(panel3));
    }

    @Test
    void shouldNotAddNegativeId() throws DataException{
        Panel panel = new Panel(-1, "TestSection", 120, 1200, 2021,  Material.ASi, false);
        PanelResult result = new PanelResult();

        result.addErrorMessage("Panel id cannot be negative.");
        assertEquals(result, service.add(panel));
    }

    @Test
    void shouldNotAddInvalidYear() throws DataException{
        Panel panel = new Panel(-1, "TestSection", 120, 1200, 3021,  Material.ASi, false);
        PanelResult result = new PanelResult();

        result.addErrorMessage("Installation year must be between 1950 and 2024.");
        assertEquals(result, service.add(panel));
    }

    
    @Test
    void shouldNotAddWhenRowColumnTaken() throws DataException{
        Panel panel = new Panel(1, null, 12, 12, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(2, null, 12, 12, 2021,  Material.ASi, false);
        
        assertEquals(new PanelResult(), service.add(panel));         
        
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
        Panel panel = new Panel(1, "TestSection", 12, 12, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(2, "TestSection", 14, 14, 2021,  Material.ASi, false);

        assertEquals(new PanelResult(), service.add(panel));         
        assertEquals(new PanelResult(), service.add(panel2));    
        assertEquals(new ArrayList<>(), service.findBySection("DoesNotExistSection")); 

        assertTrue(service.deleteById(1).isSuccess());
        assertTrue(service.deleteById(2).isSuccess());
    }

    @Test
    void shouldFindPanelsWithValidSection() throws DataException{
        Panel panel = new Panel(1, "TestSection", 12, 12, 2021,  Material.ASi, false);
        Panel panel2 = new Panel(2, "TestSection", 13, 13, 2021,  Material.ASi, false);
        Panel panel3 = new Panel(3, "Lower hill", 14, 14, 2021,  Material.ASi, false);

        assertEquals(new PanelResult(), service.add(panel));         
        assertEquals(new PanelResult(), service.add(panel2));         
        assertEquals(new PanelResult(), service.add(panel3));         

        List<Panel> panels = Arrays.asList(
            new Panel(1, "TestSection", 12, 12, 2021, Material.ASi, false),
            new Panel(2, "TestSection", 13, 13, 2021, Material.ASi, false)
        );

        assertEquals(panels, service.findBySection("TestSection"));
        assertTrue(service.deleteById(1).isSuccess());
        assertTrue(service.deleteById(2).isSuccess());
        assertTrue(service.deleteById(3).isSuccess());
    }
}
