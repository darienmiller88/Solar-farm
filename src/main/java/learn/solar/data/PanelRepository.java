package main.java.learn.solar.data;

import java.util.List;

import main.java.learn.solar.models.Panel;

/**
 * PanelRepository
 */
public interface PanelRepository {
    List<Panel> findBySection(String section) throws DataException;

    void add(Panel panel);

    boolean update(Panel panel) throws DataException;

    boolean deleteById(int id) throws DataException;
} 