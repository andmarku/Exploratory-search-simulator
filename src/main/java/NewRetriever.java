import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NewRetriever {
    private static HttpURLConnection httpConn;
    //curl -XGET "http://10.10.6.160:9200/_search" -H 'Content-Type: application/json' -d'{"query":{"function_score":{"random_score":{"seed":10,"field":"_seq_no"}}},"size":2}'
            /*
            -XGET "http://10.10.6.160:9200/_search"
            -H 'Content-Type: application/json'
            -d'{"query":{"function_score":{"random_score":{"seed":10,"field":"_seq_no"}}},"size":2}'
            */

    /**
     * Makes an HTTP request using POST method to the specified URL.
     *
     * @param requestURL
     *            the URL of the remote server
     * @param params
     *            A map containing POST data in form of key-value pairs
     * @return An HttpURLConnection object
     * @throws IOException
     *             thrown if any I/O error occurred
     */
    public static HttpURLConnection sendPostRequest(String requestURL,
                                                    Map<String, String> params) throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);

        httpConn.setDoInput(true); // true indicates the server returns response

        StringBuffer requestParams = new StringBuffer();

        if (params != null && params.size() > 0) {

            httpConn.setDoOutput(true); // true indicates POST request

            // creates the params string, encode them using URLEncoder
            Iterator<String> paramIterator = params.keySet().iterator();
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(
                        URLEncoder.encode(value, "UTF-8"));
                requestParams.append("&");
            }

            // sends POST data
            OutputStreamWriter writer = new OutputStreamWriter(
                    httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();
        }

        return httpConn;
    }
        public static void searchResultRetriever() throws IOException {
            URL url = new URL("http://10.10.6.160:9200");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            OutputStream outputStream = httpCon.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            String str = "This is String";
            writer.print(str);

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

            // testing
            System.out.println("Total: \t" + ((JsonObject) entireRes.get("hits")).get("total"));
            List arr = (List) ((JsonObject) entireRes.get("hits")).get("hits");
            for ( Object entry:arr ) {
                System.out.println("New doc \t" + entry.toString());
            }

        }// end of searchResultRetriever



    }// end of class


