package net.ke8tjm.tle.utils;

import net.ke8tjm.tle.entities.Category;
import net.ke8tjm.tle.entities.Sat;
import net.ke8tjm.tle.entities.TLESet;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TLEMapBuilder {

    private static final String CELESTRAK_URL = "https://celestrak.com/NORAD/elements/";


    public static String getCelestrakHtml() {
       return getHtmlContent(CELESTRAK_URL);
    }


    public static List<String> getCategoryNames(String html) {
        List<String> categoryNames = new ArrayList<>();
        String[] lines = html.split("\n");
        for (String line : lines) {
            if (line.contains("<tr class=header>") && line.contains("align=center>")){
                String categoryName = line.substring(line.indexOf("center>") + 7);
                categoryName = categoryName.substring(0, categoryName.indexOf("<"));
                categoryNames.add(categoryName);
            }
        }
        return categoryNames;
    }

    public static List<Category> getCategories(List<String> catNames, String html){
        List<Category> categories = new ArrayList<>();
        for (String n : catNames) {
            categories.add(buildCategory(n, html));
        }

        return categories;
    }

    private static Category buildCategory(String catName, String html) {
        String[] lines = html.split("\n");
        Category category = new Category(catName);

        int lineStart = 0;
        int lineEnd = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(catName)) {
                lineStart = i;
            }
            if (lineStart != 0 && lines[i].contains("</tbody>")) {
                lineEnd = i;
                break;
            }
        }

        for (int i = lineStart; i < lineEnd; i++) {
            if (lines[i].contains("TLE Data")){
                TLESet tleSet = new TLESet();
                String[] lineParts = lines[i].split(">");
                for (int x = 0; x < lineParts.length; x++) {
                    if (lineParts[x].contains("TLE Data")){
                        String url = lineParts[x].substring(lineParts[x].indexOf("href=") + 5);
                        url = url.replace("\"", "");
                        String name = lineParts[x+1].replace("</a", "");
                        tleSet.setTleSetName(name);
                        tleSet.setTleUrl(url);
                        tleSet.setTleText(getHtmlContent(CELESTRAK_URL + url));
                        category.getTleSetList().add(tleSet);
                    }
                }
            }
        }

         return category;
    }

    public static List<Sat> GetSatellites(List<Category> categories) {
        List<Sat> satList = new ArrayList<>();
        String catName = "";
        String subCatName = "";
        try {
            for (Category category : categories) {
                catName = category.getCategoryName();
                for (TLESet set : category.getTleSetList()) {
                    subCatName = set.getTleSetName();
                    String[] lines = set.getTleText().split("\r\n");
                    for (int i = 0; i <= lines.length - 1; i++) {
                        if (!lines[i].contains("Format invalid")) {
                            Sat sat = new Sat();
                            sat.setSatName(lines[i]);
                            StringBuilder sb = new StringBuilder();
                            sb.append(lines[i]).append("\n").append(lines[i + 1]).append("\n").append(lines[i + 2]);
                            i = i + 2;
                            sat.setTle(sb.toString());
                            boolean satExists = false;
                            for (Sat s : satList) {
                                if (s.getSatName().equals(sat.getSatName())){
                                    satExists = true;
                                    s.addCategory(catName);
                                    s.addSubCategory(subCatName);
                                    break;
                                }
                            }
                            if (!satExists) {
                                sat.addCategory(catName);
                                sat.addSubCategory(subCatName);
                                satList.add(sat);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(catName + " " + subCatName);
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return satList;
    }

    private static String getHtmlContent(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            String htmlContent = scanner.next();
            scanner.close();
            return htmlContent;
        } catch (Exception ex) {
            return null;
        }
    }

}
