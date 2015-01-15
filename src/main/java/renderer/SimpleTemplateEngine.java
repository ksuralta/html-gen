package renderer;

/**
 * A very simple template engine. A method is provided to replace tags {{ }}
 * with a value.
 * 
 * @author Kenneth Suralta
 *
 */
class SimpleTemplateEngine {
    private String template;
    private String output;

    /**
     * Constructor.
     * @param template Template String.
     */
    public SimpleTemplateEngine(String template) {
        this.template = template;
        output = new String(this.template);
    }

    /**
     * Fills the placeholder with the value passed.
     * @param key the placeholder key.
     * @param value
     */
    public void fill(String key, String value) {
        output = output.replace("{{ " + key + " }}", value);
    }

    /**
     * Returns the template String.
     * @return Template String.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Returns the output String.
     * @return Output String.
     */
    public String getOutput() {
        return output;
    }

}
