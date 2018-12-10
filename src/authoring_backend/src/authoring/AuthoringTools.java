package authoring;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import gameObjects.crud.GameObjectsCRUDInterface;
import gameObjects.crud.SimpleGameObjectsCRUD;
import groovy.api.GroovyFactory;
import phase.api.PhaseDB;

/**
 * This class contains all the tools to author a game;
 *
 * @author Inchan Hwang
 */
public class AuthoringTools {
    private GameObjectsCRUDInterface entityDB;
    private PhaseDB phaseDB;

    @XStreamOmitField
    private transient GroovyFactory factory;

    public AuthoringTools(int gridWidth, int gridHeight) {
        entityDB = new SimpleGameObjectsCRUD(gridWidth, gridHeight);

        factory = new GroovyFactory(entityDB);

        phaseDB = new PhaseDB(factory);
    }

    /**
     *  Initialize with a XML string
     */
    public AuthoringTools(String xml) {
        System.out.println("--------Whole  xml-------");
        System.out.println(xml);
        var xstream = new XStream(new DomDriver());
        var p = (SavedAuthoringTools) xstream.fromXML(xml);
        System.out.println("--------Entity xml-------");
        System.out.println(p.entityDBXML());
        System.out.println("--------Phase  xml-------");
        System.out.println(p.phaseDBXML());
        entityDB = new SimpleGameObjectsCRUD(p.entityDBXML());
        factory = new GroovyFactory(entityDB);
        phaseDB = new PhaseDB(factory, p.phaseDBXML());
    }

    public void setGridDimension(int width, int height) {
        entityDB.setDimension(width, height);
    }

    public GroovyFactory factory() { return factory; }
    public GameObjectsCRUDInterface entityDB() { return entityDB; }
    public PhaseDB phaseDB() { return phaseDB; }
    public String toEngineXML() { return Serializers.forEngine().toXML(this); }
    public String toAuthoringXML() {
        var xstream = new XStream(new DomDriver());
        return xstream.toXML(new SavedAuthoringTools(entityDB.toXML(), phaseDB.toXML()));
    }
}
