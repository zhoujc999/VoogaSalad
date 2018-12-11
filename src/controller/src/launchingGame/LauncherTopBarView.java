package launchingGame;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import social.*;


public class LauncherTopBarView implements Subscriber {
    public static final String DIVIDER_PATH = "/graphics/gray-line.png";
    public static final double DIVIDER_RATIO = 0.7;
    public static final double SPACING = 25;

    private HBox myBox;
    private SearchBar mySearchBar;
    private ImageView myDivider;
    private TextOptions myTextOptions;
    private Pane mySpacer;
    private ControlOptions myControlOptions;
    private Stage myStage;
    private ProfileView myProfile;
    private User myUser;

    private double initHeight;
    private double xOffset;
    private double yOffset;


    public LauncherTopBarView(double height, Stage stage, BorderPane pane, Searchable searched) {
        myUser = null;
        initHeight = height;
        myStage = stage;

        initBox();
        initDivider();
        initSpacer();

        mySearchBar = new SearchBar(searched);

        myTextOptions = new TextOptions(pane);

        myControlOptions = new ControlOptions(stage);

        myProfile = new ProfileView();

        myBox.getChildren().add(mySearchBar.getView());
        myBox.getChildren().add(myDivider);
        myBox.getChildren().add(myTextOptions.getView());
        myBox.getChildren().add(mySpacer);
        myBox.getChildren().add(myProfile.getView());
        myBox.getChildren().add(myControlOptions.getView());
        EventBus.getInstance().register(EngineEvent.CHANGE_USER, this);
    }

    private void initBox() {
        myBox = new HBox();
        myBox.setPrefHeight(initHeight);
        myBox.getStyleClass().add("launcher-top-bar");

        myBox.setSpacing(SPACING);
        myBox.setAlignment(Pos.CENTER_LEFT);

        makeDraggable();
    }


    private void initDivider() {
        Image image = new Image(getClass().getResourceAsStream(DIVIDER_PATH));
        myDivider = new ImageView(image);

        myDivider.setFitHeight(initHeight * DIVIDER_RATIO);
        myDivider.setFitWidth(2);

    }

    private void initSpacer() {
        mySpacer = new Pane();
        myBox.setHgrow(mySpacer, Priority.ALWAYS);
    }

    private void makeDraggable() {
        myBox.setOnMousePressed(event -> {
            xOffset = myStage.getX() - event.getScreenX();
            yOffset = myStage.getY() - event.getScreenY();
        });

        myBox.setOnMouseDragged(event -> {
            myStage.setX(event.getScreenX() + xOffset);
            myStage.setY(event.getScreenY() + yOffset);
        });
    }


    public HBox getView() {
        return myBox;
    }

    @Override
    public void update(EngineEvent engineEvent, Object... args) {
        if (engineEvent.equals(EngineEvent.CHANGE_USER) && args[0].getClass().equals(User.class)) {
            myUser = (User) args[0];
        }
    }
}
