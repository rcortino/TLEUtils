package net.ke8tjm.tle.entities;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String categoryName;
    private List<TLESet> tleSetList = new ArrayList<>();

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<TLESet> getTleSetList() {
        return tleSetList;
    }

    public void setTleSetList(List<TLESet> tleSetList) {
        this.tleSetList = tleSetList;
    }
}
