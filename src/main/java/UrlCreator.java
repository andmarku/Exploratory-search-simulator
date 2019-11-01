import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

// private URL url = new URL("http://10.10.6.160:8090/rest/apps/json2/searchers/json2?q=*");
// URL url = new URL("http://10.10.6.160:8090/rest/apps/json2/searchers/json2?q=\"title\":\"study\"");
// URL url = new URL("http://10.10.6.160:9200/my_index2/_search?q=title:*&size=100");
// URL url = new URL("http://10.10.6.160:9200/my_index/_search?q=title:study&size=10000");
// private URL url = new URL("http://10.10.6.160:8090/rest/apps/json2/searchers/json2?q=*");
// URL url = new URL("http://10.10.6.160:8090/rest/apps/json2/searchers/json2?q=\"title\":\"study\"");
// URL url = new URL("http://10.10.6.160:9200/my_index2/_search?q=title:*&size=100");
// URL url = new URL("http://10.10.6.160:9200/my_index/_search?q=title:study&size=10000");
public class UrlCreator {

    public static URL createSearchUrlFromListOfStrings(List<String> query, int size) throws MalformedURLException, NullPointerException {

        String address = "http://10.10.6.160:9200/my_index/_search?q=title:(" + formatQueryForUrl(query) + ")&size=" + size;
        URL url = new URL(address);
        return url;
    } // end of createUrl

    public static URL createSearchUrlFromString(String query, int size) throws MalformedURLException, NullPointerException {


        String address = "http://10.10.6.160:9200/my_index/_search?q=title:(" + query + ")&size=" + size;
        URL url = new URL(address);
        return url;
    } // end of createUrl

    /*
    * Has to be tested when the complete index is used, since the references are probably not in the current index
    * some code for testing
    *   String query = UrlFactory.createQueryForSpecificDocs((List<String>) expandedRes.get(1));
        URL url = UrlFactory.createUrlForSpecificDocs(query);
        System.out.println(query);
        System.out.println(url.toString());
        UtilityFunctions.printAllDocsInJson(Retriever.searchResultRetriever(url));
    * */
    public static URL createUrlForSpecificDocs(String query) throws MalformedURLException {
        String address = "http://10.10.6.160:9200/my_index/_search?q=_id:(" + query + ")&size=10000";
        URL url = new URL(address);
        return url;
    }

    public static String formatQueryForUrl(List<String> query) throws NullPointerException{
        if(query.size() == 0){
            throw new NullPointerException();
        }

        StringBuilder sb = new StringBuilder();

        sb.append(query.get(0));
        for (int i = 1; i < query.size(); i++) {
            sb.append("%20OR%20");
            sb.append(query.get(i));
        }
        return sb.toString();
    }

}// end of class
