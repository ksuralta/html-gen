package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Work;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Parser for the works XML
 * 
 * @author Kenneth Suralta
 */
public class WorksParser extends DefaultHandler {
    private static final Logger logger = LogManager.getLogger("Renderer");

    private boolean inID = false;
    private boolean inFilename = false;
    private boolean inURLSmall = false;
    private boolean inURLMedium = false;
    private boolean inURLLarge = false;
    private boolean inMake = false;
    private boolean inModel = false;

    private Work work = null;

    private List<Work> workList = null;

    /**
     * String - Camera make name
     * List<Work> - List of Work
     */
    private Map<String, List<Work>> makeWorkListMap = null;

    /**
     * String - Camera model name
     * List<Work> - List of work
     */
    private Map<String, List<Work>> modelWorkListMap = null;

    /**
     * String - Camera make name
     * Set<String> - Set of camera model name
     */
    private Map<String, Set<String>> makeModelSetMap = null;

    /**
     * String - Camera model name
     * String - Camera make name
     */
    private Map<String, String> modelMakeMap = null;

    /**
     * Constructor.
     */
    public WorksParser() {
        workList = new ArrayList<Work>();
        makeWorkListMap = new HashMap<String, List<Work>>();
        modelWorkListMap = new HashMap<String, List<Work>>();
        makeModelSetMap = new HashMap<String, Set<String>>();
        modelMakeMap = new HashMap<String, String>();
    }

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {

        logger.debug("qName:" + qName);

        if (qName.equals("work")) {
            work = new Work();
        }
        else if (qName.equals("id")) {
            inID = true;
        }
        else if (qName.equals("filename")) {
            inFilename = true;
        }
        else if (qName.equals("url")) {
            String urlTypeValue = atts.getValue("type");
            if (urlTypeValue.equals("small")) {
                inURLSmall = true;
            }
            else if (urlTypeValue.equals("medium")) {
                inURLMedium = true;
            }
            else if (urlTypeValue.equals("large")) {
                inURLLarge = true;
            }
        }
        else if (qName.equals("make")) {
            inMake = true;
        }
        else if (qName.equals("model")) {
            inModel = true;
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {

        String value = new String(ch, start, length);
        logger.debug("Value:" + value);
        if (inID) {
            work.setId(value);
        }
        else if (inFilename) {
            work.setFilename(value);
        }
        else if (inURLSmall) {
            work.setUrlSmall(value);
        }
        else if (inURLMedium) {
            work.setUrlMedium(value);
        }
        else if (inURLLarge) {
            work.setUrlLarge(value);
        }
        else if (inMake) {
            work.setCameraMake(value);
        }
        else if (inModel) {
            work.setCameraModel(value);
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (qName.equals("id")) {
            inID = false;
        }
        else if (qName.equals("filename")) {
            inFilename = false;
        }
        else if (qName.equals("url")) {
            inURLSmall = inURLMedium = inURLLarge = false;
        }
        else if (qName.equals("make")) {
            inMake = false;
        }
        else if (qName.equals("model")) {
            inModel = false;
        }
        else if (qName.equals("work")) {

            // add to list
            workList.add(work);

            if (work != null && !work.getCameraMake().isEmpty()
                    && !work.getCameraModel().isEmpty()) {
                // add work to make map
                List<Work> workList = makeWorkListMap.get(work.getCameraMake());
                if (workList == null) {
                    workList = new ArrayList<Work>();
                }
                workList.add(work);
                makeWorkListMap.put(work.getCameraMake(), workList);

                // add work to make map
                workList = modelWorkListMap.get(work.getCameraModel());
                if (workList == null) {
                    workList = new ArrayList<Work>();
                }
                workList.add(work);
                modelWorkListMap.put(work.getCameraModel(), workList);

                // add model to make-model map
                Set<String> modelSet = makeModelSetMap
                        .get(work.getCameraMake());
                if (modelSet == null) {
                    modelSet = new HashSet<String>();
                }
                modelSet.add(work.getCameraModel());
                makeModelSetMap.put(work.getCameraMake(), modelSet);

                // add to model to make map
                modelMakeMap.put(work.getCameraModel(), work.getCameraMake());
            }
        }
    }

    public void endDocument() throws SAXException {
        logResult();
    }

    /**
     * Returns workList.
     * @return workList
     */
    public List<Work> getWorkList() {
        return workList;
    }

    /**
     * Returns makeWorkListMap.
     * @return makeWorkListMap
     */
    public Map<String, List<Work>> getMakeWorkListMap() {
        return makeWorkListMap;
    }

    /**
     * Returns modelWorkListMap.
     * @return modelWorkListMap
     */
    public Map<String, List<Work>> getModelWorkListMap() {
        return modelWorkListMap;
    }

    /**
     * Returns makeModelSetMap.
     * @return makeModelSetMap
     */
    public Map<String, Set<String>> getMakeModelSetMap() {
        return makeModelSetMap;
    }

    /**
     * Returns modelMakeMap.
     * @return modelMakeMap
     */
    public Map<String, String> getModelMakeMap() {
        return modelMakeMap;
    }

    /**
     * Logs the result.
     */
    private void logResult() {
        logger.info("Work by Make");
        for (Map.Entry<String, List<Work>> entry : makeWorkListMap.entrySet()) {
            logger.info("MAKE:" + entry.getKey());
            outputWorkList(entry.getValue());
        }

        logger.info("Work by Model");
        for (Map.Entry<String, List<Work>> entry : modelWorkListMap.entrySet()) {
            logger.info("MODEL:" + entry.getKey());
            outputWorkList(entry.getValue());
        }

        logger.info("Model by Make");
        for (Map.Entry<String, Set<String>> entry : makeModelSetMap.entrySet()) {
            logger.info("MAKE:" + entry.getKey());
            for (String model : entry.getValue()) {
                logger.info("Model:" + model);
            }
        }
    }

    /**
     * Logs the list of Work.
     * @param workList
     */
    private void outputWorkList(List<Work> workList) {
        for (Work work : workList) {
            logger.info("ID:" + work.getId());
            logger.info("File Name:" + work.getFilename());
            logger.info("URL Small:" + work.getUrlSmall());
            logger.info("URL Medium:" + work.getUrlMedium());
            logger.info("URL Large:" + work.getUrlLarge());
            logger.info("Make:" + work.getCameraMake());
            logger.info("Model:" + work.getCameraModel());
            logger.info("-----------------------");
        }
    }
}
