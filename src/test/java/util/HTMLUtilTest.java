package util;

import java.util.Arrays;

import model.NavItem;
import model.Work;

import org.junit.Assert;
import org.junit.Test;

public class HTMLUtilTest {
    @Test
    public void testToSlug() {
        String slug = HTMLUtil.toSlug("Nikon.Camera  D7000");
        Assert.assertEquals("nikoncamera-d7000", slug);
    }

    @Test
    public void testGenerateNavigation() {
        NavItem itemArray[] = new NavItem[] {
            new NavItem("Link 1", "link1.html"),
            new NavItem("Link 2", "link2.html")
        };
        String htmlNavigation = HTMLUtil.generateNavigation(Arrays.asList(itemArray));
        Assert.assertEquals("<ul><li><a href='link1.html'>Link 1</a></li><li><a href='link2.html'>Link 2</a></li></ul>", htmlNavigation);
    }

    @Test
    public void testGenerateThumbnails() {
        Work workArray[] = new Work[] {
            new Work(),
            new Work()
        };
        workArray[0].setUrlSmall("url_small_1.jpg");
        workArray[0].setFilename("file1");
        workArray[1].setUrlSmall("url_small_2.jpg");
        workArray[1].setFilename("file2");
        
        String htmlThumbnails = HTMLUtil.generateThumbnails(Arrays.asList(workArray), 5);
        Assert.assertEquals("<img src='url_small_1.jpg' alt='file1'><img src='url_small_2.jpg' alt='file2'>", htmlThumbnails);
    }

    @Test
    public void testGenerateThumbnailsLimitOne() {
        Work workArray[] = new Work[] {
            new Work(),
            new Work()
        };
        workArray[0].setUrlSmall("url_small_1.jpg");
        workArray[0].setFilename("file1");
        workArray[1].setUrlSmall("url_small_2.jpg");
        workArray[1].setFilename("file2");
        
        String htmlThumbnails = HTMLUtil.generateThumbnails(Arrays.asList(workArray), 1);
        Assert.assertEquals("<img src='url_small_1.jpg' alt='file1'>", htmlThumbnails);
    }
}
