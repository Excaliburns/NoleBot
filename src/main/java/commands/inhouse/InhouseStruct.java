package commands.inhouse;

import java.util.ArrayList;
import java.util.List;

public class InhouseStruct {
    private String CategoryID = "";
    private List<Inhouse> inhouses;

    List<Inhouse> getInhouses() {
        if(inhouses == null)
            return new ArrayList<>();
        return inhouses;
    }

    void setInhouses(List<Inhouse> inhouses) {
        this.inhouses = inhouses;
    }

    String getCategoryID() {
        return CategoryID;
    }

    void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }
}
