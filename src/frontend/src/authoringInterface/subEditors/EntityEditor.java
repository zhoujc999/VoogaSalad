package authoringInterface.subEditors;

import authoringUtils.exception.DuplicateGameObjectClassException;
import authoringUtils.exception.GameObjectClassNotFoundException;
import authoringUtils.exception.GameObjectInstanceNotFoundException;
import authoringUtils.exception.InvalidOperationException;
import gameObjects.crud.GameObjectsCRUDInterface;
import gameObjects.entity.EntityClass;
import gameObjects.entity.EntityInstance;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.ErrorWindow;
import utils.exception.PreviewUnavailableException;
import utils.imageManipulation.JavaFxOperation;
import utils.imageManipulation.ImageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * This is the editor for an "Entity" object that is opened when the user clicks on an existing entity or tries to add an entity to the game authoring library.
 *
 * @author Haotian Wang
 * @author Amy
 */
public class EntityEditor extends AbstractGameObjectEditor<EntityClass, EntityInstance> {
    private Label imageText;
    private Button chooseImage;
    private HBox imagePanel;
    private static final double ICON_WIDTH = 50;
    private static final double ICON_HEIGHT = 50;
    private Label propLabel = new Label("Properties");
    private Button addProperties = new Button("Add");
    private Dialog<Pair<String, String>> dialog = new Dialog<>();
    private GridPane grid = new GridPane();
    private GridPane listProp;
    private VBox listview;
    private ObservableMap<String, String> list;
    private TextField name;
    private TextField value;
    private Button delete;

    EntityEditor(GameObjectsCRUDInterface manager) {
        super(manager);
        nameLabel.setText("Your entity name:");
        imageText = new Label("Add an image to your entity");
        chooseImage = new Button("Choose image");
        chooseImage.setStyle("-fx-text-fill: white;"
                            + "-fx-background-color: #343a40;");
        imagePanel = new HBox(10);
        listProp = new GridPane();
        listview = new VBox();
        listview.setSpacing(10);
        list = FXCollections.observableHashMap();
        listProp.getChildren().add(listview);
        nameField.setPromptText("Your entity name");
        imagePaths = FXCollections.observableArrayList();
        imagePaths.addListener((ListChangeListener<String>) c -> presentImages());
        chooseImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                String imagePath = file.toURI().toString();
                imagePaths.add(imagePath);
            }
        });
        setupProp();
        addProperties.setStyle("-fx-text-fill: white;"
                                + "-fx-background-color: #343a40;");
        addProperties.setOnAction(e -> {
            dialog.showAndWait().ifPresent(prop -> manageList(prop.getKey(), prop.getValue()));
        });

        confirm.setStyle("-fx-text-fill: white;"
                        + "-fx-background-color: #343a40;");
        confirm.setOnAction(e -> {
            if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
                new ErrorWindow("Empty name", "You must give your entity a non-empty name").showAndWait();
            } else {
                ((Stage) rootPane.getScene().getWindow()).close();
                switch (editingMode) {
                    case ADD_TREEITEM:
                        try {
                            gameObjectManager.createEntityClass(nameField.getText().trim());
                        } catch (DuplicateGameObjectClassException e1) {
                            // TODO
                            e1.printStackTrace();
                        }
                        EntityClass entityClass = null;
                        try {
                            entityClass = gameObjectManager.getEntityClass(nameField.getText().trim());
                        } catch (GameObjectClassNotFoundException e1) {
                            // TODO
                            e1.printStackTrace();
                        }
                        TreeItem<String> newItem = new TreeItem<>(entityClass.getClassName().getValue());
                        entityClass.getImagePathList().addAll(imagePaths);
                        for(var key : list.keySet()){
                            entityClass.getPropertiesMap().put(key, list.get(key));
                        }
                        ImageView icon = null;
                        try {
                            icon = new ImageView(ImageManager.getPreview(entityClass));
                        } catch (PreviewUnavailableException e1) {
                            // TODO: proper error handling
                            e1.printStackTrace();
                        }
                        JavaFxOperation.setWidthAndHeight(icon, ICON_WIDTH, ICON_HEIGHT);
                        newItem.setGraphic(icon);
                        treeItem.getChildren().add(newItem);
                        break;
                    case NONE:
                        return;
                    case EDIT_NODE:
                        try { ImageManager.removeInstanceImage(gameObjectInstance); } catch (GameObjectInstanceNotFoundException e1) {}
                        gameObjectInstance.setInstanceName(nameField.getText());
                        gameObjectInstance.getImagePathList().clear();
                        gameObjectInstance.getImagePathList().addAll(imagePaths);
                        try {
                            ((ImageView) nodeEdited).setImage(ImageManager.getPreview(gameObjectInstance));
                        } catch (PreviewUnavailableException e1) {
                            // TODO: proper error handling
                            e1.printStackTrace();
                        }
                        break;
                    case EDIT_TREEITEM:
                        try { ImageManager.removeClassImage(gameObjectClass); } catch (GameObjectClassNotFoundException e1) {}
                        gameObjectClass.getImagePathList().clear();
                        gameObjectClass.getImagePathList().addAll(imagePaths);
                        try {
                            gameObjectManager.changeGameObjectClassName(gameObjectClass.getClassName().getValue(), nameField.getText());
                        } catch (InvalidOperationException e1) {
                            // TODO
                            e1.printStackTrace();
                        }
                        ImageView icon2 = null;
                        try {
                            icon2 = new ImageView(ImageManager.getPreview(gameObjectClass));
                        } catch (PreviewUnavailableException e1) {
                            // TODO: proper error handling
                            e1.printStackTrace();
                        }
                        JavaFxOperation.setWidthAndHeight(icon2, ICON_WIDTH, ICON_HEIGHT);
                        treeItem.setValue(nameField.getText());
                        treeItem.setGraphic(icon2);
                        break;
                }
            }
        });
        setupLayout();
        rootPane.getChildren().addAll(propLabel, addProperties, imageText, chooseImage, imagePanel, listProp);
    }

    /**
     * Present the ImageViews contained in the imagePanel according to the ObservableList of ImagePaths.
     */
    private void presentImages() {
        List<Node> images = new ArrayList<>();
        imagePaths.forEach(path -> {
            ImageView preview = new ImageView(path);
            preview.setFitWidth(50);
            preview.setFitHeight(50);
            images.add(preview);
        });
        imagePanel.getChildren().clear();
        imagePanel.getChildren().addAll(images);
    }

    /**
     * This method brings up an editor that contains the data of an existing object that is already created.
     */
    @Override
    public void readGameObjectInstance() {
        nameField.setText(gameObjectInstance.getInstanceName().getValue());
        imagePaths.addAll(gameObjectInstance.getImagePathList());
    }

    /**
     * Read the GameObjectClass represented by this editor.
     */
    @Override
    public void readGameObjectClass() {
        nameField.setText(gameObjectClass.getClassName().getValue());
        imagePaths.addAll(gameObjectClass.getImagePathList());
    }

    private void setupLayout() {
        imageText.setLayoutX(50);
        imageText.setLayoutY(106);
        chooseImage.setLayoutX(261);
        chooseImage.setLayoutY(106);
        imagePanel.setLayoutX(45);
        imagePanel.setLayoutY(156);
        propLabel.setLayoutX(50);
        propLabel.setLayoutY(230);
        addProperties.setLayoutX(261);
        addProperties.setLayoutY(230);
        listProp.setLayoutX(50);
        listProp.setLayoutY(250);
    }

    private void setupProp() {
        dialog.setTitle("Property of Entity");
        dialog.setHeaderText("This action will add new property of the entity.");
        ButtonType propConfirm = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(propConfirm, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(setPropkeys());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == propConfirm) {
                return new Pair<>(name.getText(), value.getText());
            }
            return null;
        });
    }

    private GridPane setPropkeys() {
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        name = new TextField();
        value = new TextField();
        name.setPromptText("i.e) hp");
        value.setPromptText("i.e) 5");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Value:"), 0, 1);
        grid.add(value, 1, 1);

        return grid;
    }

    private void manageList(String key, String value) {
        HBox prop = new HBox();
        delete = new Button("X");
        delete.setStyle("-fx-font-size: 8px;"
                + "-fx-text-fill: white;"
                + "-fx-background-color: black;");
        Text keyText = new Text(key);
        Text valueText = new Text(value);
        prop.setSpacing(20);
        prop.setStyle("-fx-padding: 5;"
                    +"-fx-border-style: dashed");
        if (list.keySet().contains(key)){
            new ErrorWindow("Error", "This is already existed name of property.").showAndWait();
        }else {
            prop.getChildren().addAll(delete, keyText, valueText);
            listview.getChildren().add(prop);
            list.put(key, value);
        }
        delete.setOnMouseClicked(e -> {
            listview.getChildren().remove(prop);
            list.remove(key, value);
        });
    }
}
