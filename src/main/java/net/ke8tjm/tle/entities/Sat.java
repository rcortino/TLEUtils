package net.ke8tjm.tle.entities;

import com.github.amsacode.predict4java.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sat {

    private String satName;
    private String tleString;
    private List<String> categoryList = new ArrayList<>();
    private List<String> subCategoryList = new ArrayList<>();
    private Satellite satellite;
    private TLE tle;

    public String getSatName() {
        return satName;
    }

    public void setSatName(String satName) {
        this.satName = satName;
    }

    public String getTle() {
        return tleString;
    }

    public TLE getTLE() {
        return tle;
    }

    public void setTle(String tle) {
        this.tleString = tle;
        this.tle = new TLE(tle.split("\n"));
        satellite = SatelliteFactory.createSatellite(this.tle);
    }

    public List<String> getCategories() {
        return categoryList;
    }

    public void addCategory(String category) {
        this.categoryList.add(category);
    }

    public List<String> getSubCategories() {
        return subCategoryList;
    }

    public void addSubCategory(String subCategory) {
        this.subCategoryList.add(subCategory);
    }

    public SatPos getCurrentSatellitePosition(GroundStationPosition groundStationPosition) {
        return satellite.getPosition(groundStationPosition, new Date());
    }

    public SatPos getPredictedSatellitePosition(GroundStationPosition gsp, Date date) {
        if (date.after(new Date())){
            return satellite.getPosition(gsp, date);
        } else {
            return getCurrentSatellitePosition(gsp);
        }
    }

    public boolean willBeVisible(GroundStationPosition gsp) {
        return satellite.willBeSeen(gsp);
    }

    public SatPassTime getNextPass(GroundStationPosition gsp) throws SatNotFoundException {
        return getNextPass(gsp, new Date());
    }

    public SatPassTime getNextPass(GroundStationPosition gsp, Date from) throws SatNotFoundException {
        PassPredictor passPredictor = new PassPredictor(tle, gsp);
        return passPredictor.nextSatPass(from);
    }


}
