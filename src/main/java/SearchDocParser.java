import java.io.StringReader;
import java.util.*;
import javax.json.*;

public class SearchDocParser {

    public static List<List> docAndLinksScoreParser(JsonObject searchRes){
        // initialize list to be returned from the method
        AbstractMap<String, Double> retrievedDocs = new HashMap<String, Double>();
        AbstractMap<String, Double> linkedDocs = new HashMap<String, Double>();

        // pick out the list of retrieved documents
        List results = (List) searchRes.getJsonObject("hits").get("hits");

        double totalScore = 0;
        // go through all retrieved documents and collect the docs id and the id of the linked docs
        for (Object nextObject: results) {
            // assume that all entries are json objects (and not arrays)
            JsonObject nextDoc = (JsonObject) nextObject;

            // add the document from the ranked list to the retrievedDoc list
            String id = nextDoc.getString("_id");

            double docScore = nextDoc.getJsonNumber("_score").doubleValue();
            totalScore += docScore;

            // no duplicate test - not going to be any duplicates in final res from elastic
            retrievedDocs.put(id, docScore);


            // add all incitations to the linkedDocs list
            if(nextDoc.getJsonObject("_source").containsKey("inCitations")) {
                addCitationsForDocAndLinksScoreParser(linkedDocs, nextDoc, "inCitations", docScore);
            }

            // add all outcitations to the linkedDocs list.
            if(nextDoc.getJsonObject("_source").containsKey("outCitations")){
                addCitationsForDocAndLinksScoreParser(linkedDocs, nextDoc, "outCitations", docScore);
            }
        }// end of loop through all retrieved documents

        // packing before returning
        List toReturn  = new ArrayList();
        toReturn.add(retrievedDocs);
        toReturn.add(linkedDocs);
        toReturn.add(totalScore);
        return toReturn;
    }// end of parser

    private static void addCitationsForDocAndLinksScoreParser
            (AbstractMap<String, Double> citationList, JsonObject nextDoc, String key, double origDocScore){
        String docId;
        if (nextDoc.getJsonObject("_source").get(key) instanceof JsonArray) {
            JsonArray inCitations = nextDoc.getJsonObject("_source").getJsonArray(key);
            for (int i = 0; i < inCitations.size(); i++) {
                docId = inCitations.getString(i);
                if (citationList.containsKey(docId)){
                    Double oldScore = citationList.get(docId);
                    citationList.replace(docId, oldScore + origDocScore);
                }else{
                    citationList.put(docId, origDocScore);
                }
            }
        } else {
            docId = nextDoc.getJsonObject("_source").getString(key);
            if (citationList.containsKey(docId)){
                Double oldScore = citationList.get(docId);
                citationList.replace(docId, oldScore + origDocScore);
            }else{
                citationList.put(docId, origDocScore);
            }
        }
    }
    /*
        Collects only ids and linked ids.
         - The former should not contain any duplicates, thanks to elastic (unless there are in the data)
         - Entries in the latter can occur multiple times in the linked array and may also occur in the ids array
         . The linked ids makes no difference between in and out citations
         OBS! The method makes a lot of assumptions about how the data is formatted.
         @ returns List with the ids and the linked ids lists.
    */
    public static List<List> docScoreAndJustLinksParser(JsonObject searchRes){
        // initialize list to be returned from the method
        AbstractMap<String, Double> retrievedDocs = new HashMap<String, Double>();
        List<String> linkedDocs = new LinkedList<String>();

        // pick out the list of retrieved documents
        List results = (List) searchRes.getJsonObject("hits").get("hits");

        double totalScore = 0;
        // go through all retrieved documents and collect the docs id and the id of the linked docs
        for (Object nextObject: results) {
            // assume that all entries are json objects (and not arrays)
            JsonObject nextDoc = (JsonObject) nextObject;

            // add the document from the ranked list to the retrievedDoc list
            String id = nextDoc.getString("_id");

            double docScore = nextDoc.getJsonNumber("_score").doubleValue();
            totalScore += docScore;

            // no duplicate test - not going to be any duplicates in final res from elastic
            retrievedDocs.put(id, docScore);


            // add all incitations to the linkedDocs list
            if(nextDoc.getJsonObject("_source").containsKey("inCitations")) {
                addCitationsForDocScoreAndJustLinksParser(linkedDocs, nextDoc, "inCitations");
            }

            // add all outcitations to the linkedDocs list.
            if(nextDoc.getJsonObject("_source").containsKey("outCitations")){
                addCitationsForDocScoreAndJustLinksParser(linkedDocs, nextDoc, "outCitations");
            }
        }// end of loop through all retrieved documents

        // packing before returning
        List toReturn  = new LinkedList();
        toReturn.add(retrievedDocs);
        toReturn.add(linkedDocs);
        toReturn.add(totalScore);
        return toReturn;
    }// end of parser

    private static void addCitationsForDocScoreAndJustLinksParser
            (List<String> citationList, JsonObject nextDoc, String key){
        if (nextDoc.getJsonObject("_source").get(key) instanceof JsonArray) {
            JsonArray inCitations = nextDoc.getJsonObject("_source").getJsonArray(key);
            for (int i = 0; i < inCitations.size(); i++) {
                citationList.add(inCitations.getString(i));
            }
        } else {
            citationList.add(nextDoc.getJsonObject("_source").getString(key));
        }
    }
    /*
        Collects only ids and linked ids.
         - The former should not contain any duplicates, thanks to elastic (unless there are in the data)
         - Entries in the latter can occur multiple times in the linked array and may also occur in the ids array
         . The linked ids makes no difference between in and out citations
         OBS! The method makes a lot of assumptions about how the data is formatted.
         @ returns List with the ids and the linked ids lists.
         OBS! position in search results (and matching score) is not included for citation links.
    */
    public static List<List> simpleParser(JsonObject searchRes){
        // initialize list to be returned from the method
        List<String> retrievedDocs = new LinkedList<String>();
        List<String> linkedDocs = new LinkedList<String>();

        // pick out the list of retrieved documents
        JsonArray results = ((JsonObject) searchRes.get("documentList")).getJsonArray("documents");

        // go through all retrieved documents and collect the docs id and the id of the linked docs
        for (JsonValue nextJsonValue: results) {
            // assume that all entries are json objects (and not arrays)
            JsonObject nextDoc = (JsonObject) nextJsonValue;

            // add the document from the ranked list to the retrievedDoc list
            String id = nextDoc.getString("_id");
            // no duplicate test - not going to be any duplicates in final res from elastic
            retrievedDocs.add(id);

            // add all incitations to the linkedDocs list
            if(nextDoc.containsKey("inCitations")) {
                Object inCitationObject = nextDoc.get("inCitations");
                if (inCitationObject instanceof List) {
                    List inCitations = (List) inCitationObject;
                    for (Object nextInCitation: inCitations) {
                        linkedDocs.add((String) nextInCitation);
                    }
                } else {
                    linkedDocs.add((String) inCitationObject);
                }
            }

            // add all outcitations to the linkedDocs list.
            if(nextDoc.containsKey("outCitations")){
                Object outCitationObject = nextDoc.get("outCitations");
                if (outCitationObject instanceof List) {
                    List outCitations = (List) outCitationObject;
                    for (Object nextOutCitation: outCitations) {
                        linkedDocs.add((String) nextOutCitation);
                    }
                } else {
                    linkedDocs.add((String) outCitationObject);
                }
            }
        }// end of loop through all retrieved documents

        // packing before returning
        List<List> toReturn  = new LinkedList<List>();
        toReturn.add(retrievedDocs);
        toReturn.add(linkedDocs);
        return toReturn;
    }// end of simpleParser
}// end of class