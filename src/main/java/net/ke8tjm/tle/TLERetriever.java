package net.ke8tjm.tle;

import net.ke8tjm.tle.entities.Category;
import net.ke8tjm.tle.entities.Sat;
import net.ke8tjm.tle.entities.TLESet;
import net.ke8tjm.tle.utils.TLEMapBuilder;

import java.util.List;

public class TLERetriever {

    public static void main(String[] args) {
        String html = TLEMapBuilder.getCelestrakHtml();
        List<String> names = TLEMapBuilder.getCategoryNames(html);
        List<Category> categories = TLEMapBuilder.getCategories(names, html);

        List<Sat> satList = TLEMapBuilder.GetSatellites(categories);
        for (Category c : categories) {
            System.out.println(c.getCategoryName());
            for (TLESet set : c.getTleSetList()) {
                System.out.println("\t\t" + set.getTleSetName());
            }
        }

        /*for (String n : names) {
            System.out.println(n);
        }*/
    }
}
