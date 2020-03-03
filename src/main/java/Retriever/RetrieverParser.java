package Retriever;

import java.util.*;
import javax.json.*;

public class RetrieverParser {

    public static List<List> docAndLinksScoreParser(JsonObject searchRes) throws Exception {
        // initialize list to be returned from the method
        AbstractMap<String, Double> retrievedDocs = new HashMap<>();
        AbstractMap<String, Double> linkedDocs = new HashMap<>();

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
            (AbstractMap<String, Double> citationList, JsonObject nextDoc, String key, double origDocScore) throws Exception {
        String docId;
        if (nextDoc.getJsonObject("_source").get(key) instanceof JsonArray) {
            JsonArray citations = nextDoc.getJsonObject("_source").getJsonArray(key);
            for (int i = 0; i < citations.size(); i++) {
                docId = citations.getString(i);
                docId = removeCitationMarks(docId);
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

    private static String removeCitationMarks(String str) throws Exception {
        if(!(str.substring(0,1).equals("\"") && str.substring(str.length()-1).equals("\""))){
            throw new Exception("Deleted something that wasn't citation marks ");
        }
        return str.substring(1,str.length()-1);
    }

}// end of class