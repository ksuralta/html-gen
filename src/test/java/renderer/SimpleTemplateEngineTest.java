package renderer;

import org.junit.Assert;

import org.junit.Test;

public class SimpleTemplateEngineTest {
    @Test
    public void testFill() {
        SimpleTemplateEngine templateEngine = new SimpleTemplateEngine("hello {{ name }}");
        templateEngine.fill("name", "Tony Stark");
        Assert.assertEquals("hello Tony Stark", templateEngine.getOutput());
    }
}
