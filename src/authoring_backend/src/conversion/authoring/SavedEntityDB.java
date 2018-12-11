package conversion.authoring;

import gameObjects.gameObject.GameObjectClass;
import gameObjects.gameObject.GameObjectInstance;

import java.util.HashSet;

public class SavedEntityDB {
    private int numCols;
    private int numRows;
    private HashSet<GameObjectClass> classes;
    private HashSet<GameObjectInstance> instances;

    SavedEntityDB(
        int numCols,
        int numRows,
        HashSet<GameObjectClass> classes,
        HashSet<GameObjectInstance> instances
    ) {
        this.numCols = numCols;
        this.numRows = numRows;
        this.classes = classes;
        this.instances = instances;
    }

    public int numCols() { return numCols; }
    public int numRows() { return numRows; }
    public HashSet<GameObjectClass> classes() { return classes; }
    public HashSet<GameObjectInstance> instances() { return instances; }
}
