package gameObjects.sound;

import gameObjects.exception.InvalidClassException;
import gameObjects.gameObject.GameObjectInstance;
import gameObjects.gameObject.GameObjectType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SoundInstanceFactory {
    private Consumer<GameObjectInstance> requestInstanceIdFunc;
    private Consumer<GameObjectInstance> addInstanceToMapFunc;


    public SoundInstanceFactory(
            Consumer<GameObjectInstance> requestInstanceIdFunc,
            Consumer<GameObjectInstance> addInstanceToMapFunc) {

        this.requestInstanceIdFunc = requestInstanceIdFunc;
        this.addInstanceToMapFunc = addInstanceToMapFunc;
    }

    public SoundInstance createInstance(SoundClass soundPrototype) {

        if (soundPrototype.getType() != GameObjectType.SOUND) {
            throw new InvalidClassException();
        }
        SimpleStringProperty mediaFilePathCopy = new SimpleStringProperty();
        ObservableMap propertiesMapCopy = FXCollections.observableHashMap();
        mediaFilePathCopy.setValue(soundPrototype.getMediaFilePath().getValue());
        propertiesMapCopy.putAll(soundPrototype.getPropertiesMap());
        Supplier<SoundClass> getCategoryClassFunc = () -> soundPrototype;
        SoundInstance categoryInstance = new SimpleSoundInstance(soundPrototype.getClassName().getValue(), mediaFilePathCopy, propertiesMapCopy, getCategoryClassFunc);
        requestInstanceIdFunc.accept(categoryInstance);
        addInstanceToMapFunc.accept(categoryInstance);
        return categoryInstance;

    }
}