import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import renderer.Processor;

/**
 * Main class for rendering HTML file.
 * 
 * @author Kenneth Suralta
 */
class App {
    private static final Logger logger = LogManager.getLogger("RendererMain");

    public static void main(String[] args) {
        if (args.length < 2) {
            usage();
            return;
        }
        System.out.println("Input File: " + args[0]);
        System.out.println("Output Directory: " + args[1]);

        try {
            System.out.println("Generating HTML files");
            new Processor().process(args[0], args[1]);
            System.out.println("Done.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("Usage: app <input_file> <output_directory");
    }
}
