package commands.inhouse;

import java.util.ArrayList;
import java.util.List;

public class GroupStruct {
    private String CategoryID = "";
    private List<Group> groups;

    List<Group> getGroups() {
        if(groups == null)
            return new ArrayList<>();
        return groups;
    }

    void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    String getCategoryID() {
        return CategoryID;
    }

    void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }
}
