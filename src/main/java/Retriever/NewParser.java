package Retriever;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import javax.json.*;
import java.util.*;

public class NewParser {

    public static JsonObject newParser(SearchHits hits){
        JsonObjectBuilder resultJsonBuilder = Json.createObjectBuilder();
        JsonObjectBuilder hitJsonBuilder;

        // create a json with desired fields for each hit
        for (SearchHit hit : hits.getHits()) {
            hitJsonBuilder = Json.createObjectBuilder();

            // add the id (can be gotten from the key, but its there for completeness
            hitJsonBuilder.add("id", hit.getId());

            // add the score
            hitJsonBuilder.add("score", hit.getScore());

            // add all citations as an jsonarray
            JsonArray citations = citationsAsList(hit);
            hitJsonBuilder.add("citations", citations);

            JsonObject hitAsJson = hitJsonBuilder.build();
            resultJsonBuilder.add(hit.getId(), hitAsJson);
        }

        return resultJsonBuilder.build();
    }

    public static JsonArray citationsAsList(SearchHit hit){
        // get the fields from the search hit
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();

        // add the citing works
        List<String> citations = (List<String>) sourceAsMap.get("inCitations");

        // add the cited works
        citations.addAll((List<String>) sourceAsMap.get("outCitations"));

        // turn into json array
        JsonArrayBuilder jsArrBuilder = Json.createArrayBuilder();
        for (String s: citations) {
            // the citations come with extra citation marks
            s = removeCitationMarks(s);

            // add to the jsonarray
            jsArrBuilder.add(s);
        }

        return jsArrBuilder.build();
    }

    private static String removeCitationMarks(String str) {
        if(!(str.substring(0,1).equals("\"") && str.substring(str.length()-1).equals("\""))){
            System.out.println("Deleted something that wasn't citation marks ");
        }
        return str.substring(1,str.length()-1);
    }

}
