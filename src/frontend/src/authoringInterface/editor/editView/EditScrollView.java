package authoringInterface.editor.editView;

import api.DraggingCanvas;
import api.SubView;
import authoringInterface.sidebar.SideView;
import authoringInterface.sidebar.SideViewInterface;
import authoringInterface.sidebar.subEditors.AbstractObjectEditor;
import authoringInterface.sidebar.subEditors.EntityEditor;
import authoringInterface.sidebar.subEditors.ObjectEditor;
import authoringInterface.sidebar.treeItemEntries.EditTreeItem;
import authoringInterface.sidebar.treeItemEntries.Entity;
import authoringInterface.sidebar.treeItemEntries.TreeItemType;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * EditScrollView Class (ScrollPane)
 *      - Representation of the game's grid setting
 *      - It should support Zoom in and zoom out
 *
 * @author Amy Kim, Haotian Wang
 */
public class EditScrollView implements SubView<Pane>, DraggingCanvas {
    private Pane gridScrollView;
    private HBox contentBox;
    private SideViewInterface sideView;
    private Map<Node, TreeItem<String>> nodeToTreeItemMap;

    public EditScrollView(SideViewInterface sideView) {
        this.sideView = sideView;
        nodeToTreeItemMap = new HashMap<>();
        gridScrollView = new Pane();
        contentBox = new HBox();
        gridScrollView.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
            if (e.getGestureSource() instanceof TreeCell) {
                TreeItem<String> item = ((TreeCell<String>) e.getGestureSource()).getTreeItem();
                if (!item.isLeaf()) {
                    return;
                }
                if (item.getGraphic() != null) {
                    ImageView copy = new ImageView(((ImageView) item.getGraphic()).getImage());
                    copy.setX(e.getX());
                    copy.setY(e.getY());
                    gridScrollView.getChildren().add(copy);
                    nodeToTreeItemMap.put(copy, item);
                } else {
                    Text target = new Text(item.getValue());
                    target.setX(e.getX());
                    target.setY(e.getY());
                    gridScrollView.getChildren().add(target);
                    nodeToTreeItemMap.put(target, item);
                }
            }
        });
    }

    /**
     * This method opens an editor window if the user wishes to double click on an object already dropped on the grid.
     *
     * @param event: A MouseEvent event, which is a double click.
     * @param targetNode: The JavaFx Node where this double click occurs.
     */
    private void handleDoubleClick(MouseEvent event, Node targetNode) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            EditTreeItem userObject = sideView.getObject(nodeToTreeItemMap.get(targetNode).getValue());
            TreeItemType type = userObject.getType();
            Stage dialogStage = new Stage();
            if (type == TreeItemType.ENTITY) {
                EntityEditor editor = new EntityEditor();
                editor.readObject((Entity) userObject);
                dialogStage.setScene(new Scene(editor.getView(), 500, 500));
                dialogStage.show();
                dialogStage.setOnHidden(event1 -> {
                    if (editor.applied()) {
                        TreeItem<String> childItem = new TreeItem<>(editor.getObject().getName());
                        ImageView icon = new ImageView(editor.getObject().getSprite());
                        icon.setFitHeight(50);
                        icon.setFitWidth(50);
                        childItem.setGraphic(icon);
                        
                    }
                });
            } else if (type == TreeItemType.SOUND) {

            } else if (type == TreeItemType.CATEGORY) {

            } else if (type == TreeItemType.TILE) {

            }

        }
    }

    @Override
    public Pane getView() {
        return gridScrollView;
    }

    /**
     * Setup the dragging canvas event filters.
     */
    @Override
    public void setupDraggingCanvas() {

    }
}
