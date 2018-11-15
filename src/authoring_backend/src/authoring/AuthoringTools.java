package authoring;

import entities.EntitiesCRUDInterface;
import entities.SimpleEntitiesCRUD;
import groovy.api.GroovyFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import phase.api.PhaseDB;

/**
 *  This class contains all the tools to author a game;
 */
public class AuthoringTools {
    private GroovyFactory groovyFactory;
    private ObservableMap<String, Object> globalData;
    private EntitiesCRUDInterface entityDB;
    private PhaseDB phaseDB;

    public AuthoringTools() {
        groovyFactory = new GroovyFactory();
        globalData = FXCollections.observableHashMap();
        entityDB = new SimpleEntitiesCRUD();
        phaseDB = new PhaseDB();
    }

    public GroovyFactory groovyFactory() { return groovyFactory; }
    public ObservableMap globalData() { return globalData; }
    public EntitiesCRUDInterface entityDB() { return entityDB; }
    public PhaseDB phaseDB() { return phaseDB; }

    // ... Not exactly, but something like this
    public String toEngineXML() { return Serializers.forEngine().toXML(this); }
    public String toAuthoringXML() { return null; }
}
