package learn.solar.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import learn.solar.models.Panel;

public class PanelResult {
    private ArrayList<String> messages = new ArrayList<>();
    private Panel panel;

    public PanelResult(){
        
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

    public void addErrorMessage(String message) {
        messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PanelResult that = (PanelResult) o;
        return Objects.equals(messages, that.messages) &&
                Objects.equals(panel, that.panel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages, panel);
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();

        for (String message : messages) {
            s.append(message);
            s.append(" ");
        }

        return s.toString();
    }
}
