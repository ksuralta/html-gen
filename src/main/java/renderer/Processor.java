package renderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.NavItem;
import model.Work;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import parser.WorksParser;
import util.HTMLUtil;

/**
 * Main processor to generate a HTML file for each camera make and model, and
 * an index HTML page.
 * 
 * @author Kenneth Suralta
 */
public class Processor {

    private String loadedTemplate;
    private WorksParser parser;

    /**
     * Constructor.
     */
    public Processor() {
        initTemplate();
    }

    /**
     * Initializes the template string. 
     */
    private void initTemplate() {
        try {
            final String templateFilename = "./src/main/resources/output-template.html";
            byte[] encoded = Files.readAllBytes(Paths.get(templateFilename));
            loadedTemplate = new String(encoded, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a HTML file for each camera make and model, and
     * an index HTML page. 
     * @param inputFile The input filename.
     * @param outputDirectory The output directory.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void process(String inputFile, String outputDirectory)
            throws IOException, SAXException, ParserConfigurationException {

        parser = new WorksParser();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(parser);
        xmlReader.parse(convertToFileURL(inputFile));

        // index.html
        generateIndexFile(outputDirectory);

        // render html for each camera make
        generateCameraMakeFiles(outputDirectory);

        // render html for each camera model
        generateCameraModelFiles(outputDirectory);
    }

    /**
     * Returns a file URL based on the filename. 
     */
    private static String convertToFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }

    /**
     * Generate an index HTML page.
     */
    private void generateIndexFile(String outputDirectory) throws FileNotFoundException,
            UnsupportedEncodingException {
        // render index html
        PrintWriter indexWriter = new PrintWriter(outputDirectory
                + "/index.html", "UTF-8");
        SimpleTemplateEngine templateEngine = new SimpleTemplateEngine(loadedTemplate);
        List<Work> workList = parser.getWorkList();
        Set<String> makeSet = parser.getMakeModelSetMap().keySet();
        // generate navigation map
        List<NavItem> navList = new ArrayList<NavItem>();
        for (String make : makeSet) {
            navList.add(new NavItem(make, getFilename(outputDirectory, make)));
        }
        templateEngine.fill("TITLE GOES HERE", "Index Page");
        String htmlNav = HTMLUtil.generateNavigation(navList);
        templateEngine.fill("NAVIGATION GOES HERE", htmlNav);
        String htmlThumbnails = HTMLUtil.generateThumbnails(workList, 10);
        templateEngine.fill("THUMBNAIL IMAGES GO HERE", htmlThumbnails);

        indexWriter.println(templateEngine.getOutput());
        indexWriter.close();
        System.out.println("Generated index.html");
    }

    /**
     * Generate a camera make HTML page for each camera make.
     */
    private void generateCameraMakeFiles(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {
        for (Map.Entry<String, List<Work>> entry : parser.getMakeWorkListMap().entrySet()) {
            SimpleTemplateEngine templateEngine = new SimpleTemplateEngine(loadedTemplate);
            String make = entry.getKey();
            List<Work> workList = entry.getValue();
            String makeFilename = getFilename(outputDirectory, make);
            PrintWriter makeWriter = new PrintWriter(makeFilename, "UTF-8");
            // generate navigation map
            List<NavItem> navList = new ArrayList<NavItem>();
            navList.add(new NavItem("[INDEX]", "index.html"));
            Set<String> modelSet = parser.getMakeModelSetMap().get(make);
            for (String model : modelSet) {
                navList.add(new NavItem(model, getFilename(outputDirectory, model)));
            }
            // fill template with values
            templateEngine.fill("TITLE GOES HERE", make);
            String htmlNav = HTMLUtil.generateNavigation(navList);
            templateEngine.fill("NAVIGATION GOES HERE", htmlNav);
            String htmlThumbnails = HTMLUtil.generateThumbnails(workList, 10);
            templateEngine.fill("THUMBNAIL IMAGES GO HERE", htmlThumbnails);
            // output to file
            makeWriter.println(templateEngine.getOutput());
            makeWriter.close();
            System.out.println("Generated " + makeFilename);
        }
    }

    /**
     * Generate a camera model HTML page for each camera model.
     */
    private void generateCameraModelFiles(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {
        for (Map.Entry<String, List<Work>> entry : parser.getModelWorkListMap().entrySet()) {
            SimpleTemplateEngine templateEngine = new SimpleTemplateEngine(loadedTemplate);
            List<Work> workList = entry.getValue();
            String model = entry.getKey();
            String make = parser.getModelMakeMap().get(model);
            String modelFilename = getFilename(outputDirectory, model);
            PrintWriter modelWriter = new PrintWriter(modelFilename, "UTF-8");
            // generate navigation map
            List<NavItem> navList = new ArrayList<NavItem>();
            navList.add(new NavItem("[INDEX]", "index.html"));
            navList.add(new NavItem(make, getFilename(outputDirectory, make)));
            // fill template with values
            templateEngine.fill("TITLE GOES HERE", model);
            String htmlNav = HTMLUtil.generateNavigation(navList);
            templateEngine.fill("NAVIGATION GOES HERE", htmlNav);
            String htmlThumbnails = HTMLUtil.generateThumbnails(workList, 10);
            templateEngine.fill("THUMBNAIL IMAGES GO HERE", htmlThumbnails);
            // output to file
            modelWriter.println(templateEngine.getOutput());
            modelWriter.close();
            System.out.println("Generated " + modelFilename);
        }
    }

    /**
     * Returns the full path and filename. The title parameter is slugified to
     * be the filename.
     * 
     * @param outputDirectory
     * @param title
     * @return Full path and filename.
     */
    private String getFilename(String outputDirectory, String title) {
        String filename = outputDirectory + "/" + HTMLUtil.toSlug(title) + ".html";
        return filename;
    }
}
