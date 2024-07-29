package learn.solar.data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PanelFileRepositoryTest {
    PanelFileRepository repository = new PanelFileRepository("./data/testPanels.csv");
    int numInitialPanels;

    @BeforeEach
    void setup() throws DataException {
        numInitialPanels = repository.findAllPanels().size();
    }

    //Change findAllPanels() to public to test
    @Test
    void shouldFindAllPanels() throws DataException{
        assertEquals(numInitialPanels, repository.findAllPanels().size());
    }

    @Test
    void shouldAddPanel() throws DataException{
        Panel p = new Panel(20, "New One's", 3, 10, 2010, Material.CIGS, false);
        Panel p2 = new Panel(21, "New One's", 5, 10, 2012, Material.CdTe, true);
        Panel p3 = new Panel(22, "New One's", 2, 13, 2024, Material.ASi, true);
        Panel p4 = new Panel(23, "High,Tech", 105, 20, 2001, Material.MonoSi, false);

        repository.add(p);
        repository.add(p2);
        repository.add(p3);
        repository.add(p4);

        assertEquals(numInitialPanels + 4, repository.findAllPanels().size());

        //Rest initial state.
        assertTrue(repository.deleteById(20));
        assertTrue(repository.deleteById(21));
        assertTrue(repository.deleteById(22));
        assertTrue(repository.deleteById(23));
    }

    @Test
    void shouldDeletePanel() throws DataException{
        assertFalse(repository.deleteById(999));
        assertTrue(repository.deleteById(repository.findAllPanels().get(0).getId()));

        assertEquals(numInitialPanels - 1, repository.findAllPanels().size());

        //Reset initial state.
        repository.add(new Panel(23, "High,Tech", 105, 20, 2001, Material.MonoSi, false));
        assertEquals(numInitialPanels, repository.findAllPanels().size());
    }

    @Test
    void shouldUpdatePanel() throws DataException{
        assertFalse(repository.update(new Panel(-23, "High,Tech", 105, 20, 2001, Material.MonoSi, false)));
        assertTrue(repository.update(new Panel(23, "High,Tech", 105, 20, 2001, Material.CIGS, false)));
    }
}
