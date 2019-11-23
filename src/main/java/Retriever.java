import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

class Retriever {
    static JsonObject searchResultRetriever(RetrieverParameterCreator params) throws IOException {
        HttpURLConnection httpCon = sendPostRequest(params);
        httpCon.connect();
        String receivedData = receiveDataFromConnection(httpCon);
        httpCon.disconnect();

        // read the string as a JSON
        JsonReader jsonRdr = Json.createReader(new StringReader(receivedData));
        JsonObject entireRes = jsonRdr.readObject(); // assume that it was a json object (and not a json array)
        jsonRdr.close();

        return entireRes;
    }

    private static HttpURLConnection sendPostRequest(RetrieverParameterCreator params) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) params.url.openConnection();

        // set
        httpConn.setDoInput(true); // true indicates the server returns response
        httpConn.setDoOutput(true); // true indicates POST request
        httpConn.setRequestProperty(params.headerKey, params.headerData);

        // send POST data
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write(params.postData);
        writer.flush();
        writer.close();

        return httpConn;
    }

    private static String receiveDataFromConnection(HttpURLConnection httpCon){
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), StandardCharsets.UTF_8))) {
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
