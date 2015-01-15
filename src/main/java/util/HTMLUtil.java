package util;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;

import model.NavItem;
import model.Work;

/**
 * Utility class to generate HTML tags.
 * 
 * @author Kenneth Suralta
 */
public class HTMLUtil {

    /**
     * Slugifies the string passed.
     * @param s The input String.
     * @return The slugified String.
     */
    public static String toSlug(String s) {
        String slug = Normalizer.normalize(s.toLowerCase(), Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}|[^\\w\\s]", "")
                .replaceAll("[\\s-]+", " ")
                .trim()
                .replaceAll("\\s", "-");
        return slug;
    }

    /**
     * Returns the HTML tags for navigation using the list of NavItem parameter.
     * @param navList List of NavItem.
     * @return String of HTML navigation tags.
     */
    public static String generateNavigation(List<NavItem> navList) {
        String s = "<ul>";
        for (NavItem item : navList) {
            s += "<li><a href='" + item.getFilename() + "'>"
                    + item.getName() + "</a></li>";
        }
        s += "</ul>";
        return s;
    }

    /**
     * Returns the HTML tags of thumbnail images using the list of Work parameter.
     * The number of thumbnails generated is capped with the limit parameter. 
     * @param workList The list of Work.
     * @param limit Cap of thumbnails to generate.
     * @return String of HTML thumbnail tags.
     */
    public static String generateThumbnails(List<Work> workList, int limit) {
        String s = "";
        for (Work work : workList) {
            s += "<img src='" + work.getUrlSmall() + "' alt='"
                    + work.getFilename() + "'>";
            limit--;
            // if limit is reached
            if (limit == 0) {
                break;
            }
        }
        return s;
    }
}
