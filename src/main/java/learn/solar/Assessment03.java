package learn.solar;


import learn.solar.data.PanelFileRepository;
import learn.solar.domain.PanelService;
import learn.solar.ui.Controller;
import learn.solar.ui.View;

public class Assessment03 {

    public static void main(String[] args) {
        View view = new View();
        PanelService service = new PanelService(new PanelFileRepository("./data/panels.csv"));
        Controller controller = new Controller(view, service);

        controller.run();
    }
    
}