import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;

public class NewRetriever {
    private static HttpURLConnection httpConn;

    public static HttpURLConnection sendPostRequest(RestParameterCreator params) throws IOException {
        try{
            httpConn = (HttpURLConnection) params.url.openConnection();

            // set
            httpConn.setDoInput(true); // true indicates the server returns response
            httpConn.setDoOutput(true); // true indicates POST request
            httpConn.setRequestProperty(params.headerKey, params.headerData);

            // sends POST data
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(params.postData);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpConn;
    }

    public static JsonObject searchResultRetriever(RestParameterCreator params) throws IOException {
        HttpURLConnection httpCon = sendPostRequest(params);
        httpCon.connect();

        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"))) {
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpCon.disconnect();

        // read the string as a JSON
        JsonReader jsonRdr = Json.createReader(new StringReader(sb.toString()));
        // assume that it was a json object (and not a json array)
        JsonObject entireRes = jsonRdr.readObject();
        jsonRdr.close();

        /*// TODO: 2019-11-01
        // testing
        System.out.println("Total: \t" + ((JsonObject) entireRes.get("hits")).get("total"));
        List arr = (List) ((JsonObject) entireRes.get("hits")).get("hits");
        for ( Object entry:arr ) {
            System.out.println("New doc \t" + ((JsonObject) entry).get("_source").toString());
        }*/
        
        return entireRes;
    }// end of searchResultRetriever

    /*
    public static HttpURLConnection sendPostRequest(String requestURL,
                                                    Map<String, String> params) throws IOException {
        try{
            String data = URLEncoder.encode("{\"query\":{\"function_score\":{\"random_score\":{\"seed\":10,\"field\":\"_seq_no\"}}},\"size\":2}", "UTF-8");
            data = "{\"query\":{\"function_score\":{\"random_score\":{\"seed\":10,\"field\":\"_seq_no\"}}},\"size\":2}";
            data = "{\"query\":{\"match\":{\"title\":\"Study\"}}}";
            String header = URLEncoder.encode("Content-Type: application/json", "UTF-8");
            requestURL = "http://10.10.6.160:9200/my_index2/_search";
            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();

            // set
            httpConn.setDoInput(true); // true indicates the server returns response
            httpConn.setDoOutput(true); // true indicates POST request
            httpConn.setRequestProperty("Content-Type", "application/json");

            // sends POST data
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpConn;
    }
    */
}// end of class


