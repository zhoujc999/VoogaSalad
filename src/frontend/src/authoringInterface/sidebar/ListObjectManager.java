package authoringInterface.sidebar;

import java.util.ArrayList;

/**
 * A utility class that manages all the items in the SideView
 *
 * @author jl729
 */

public class ListObjectManager extends ArrayList<SideView.ListObject> {

    public ListObjectManager() {
        add(new SideView.ListObject("O", "Entity", 0));
        add(new SideView.ListObject("X", "Entity", 1));
        add(new SideView.ListObject("Default Grid", "Tile", 0));
    }

    @Override
    public boolean add(SideView.ListObject listObject) {
        return super.add(listObject);
    }
}