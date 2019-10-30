import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

// TODO: 2019-10-24 parse immediately to json?
public class Retriever {

    public static JsonObject searchResultRetriever(URL url) throws IOException {
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.connect();

        String results = "";
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"))) {
            while((line = reader.readLine()) != null) {
                results = results.concat(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read the string as a JSON
        JsonReader jsonRdr = Json.createReader(new StringReader(results));
        // assume that it was a json object (and not a json array)
        JsonObject entireRes = jsonRdr.readObject();
        jsonRdr.close();

        /*
        // testing
        System.out.println("Total: \t" + ((JsonObject) entireRes.get("hits")).get("total"));
        List arr = (List) ((JsonObject) entireRes.get("hits")).get("hits");
        for ( Object entry:arr ) {
            System.out.println("New doc \t" + entry.toString());
        }*/

        return entireRes;
    }// end of searchResultRetriever


    public static JsonObject simpleSearchResultRetriever(URL url){
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read the string as a JSON
        JsonReader jsonRdr = Json.createReader(new StringReader(sb.toString()));
        // assume that it was a json object (and not a json array)
        JsonObject entireRes = jsonRdr.readObject();
        jsonRdr.close();
        return entireRes;
    } // end of simpleSearchResultRetriever
}// end of class


