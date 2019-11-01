import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
/*url = new URL("http://10.10.6.160:9200/my_index2/_search");

        String data = URLEncoder.encode("{\"query\":{\"function_score\":{\"random_score\":{\"seed\":10,\"field\":\"_seq_no\"}}},\"size\":2}", "UTF-8");
        data = "{\"query\":{\"function_score\":{\"random_score\":{\"seed\":10,\"field\":\"_seq_no\"}}},\"size\":2}";
        data = "{\"query\":{\"match\":{\"title\":\"Study\"}}}";
        postData = "{\"query\":{\"match\":{\"title\":\"Study\"}}}";;*/

public class RestParameterCreator {
    URL url;
    String postData;
    String headerKey;
    String headerData;

    public RestParameterCreator() throws MalformedURLException {
        url = new URL("http://10.10.6.160:9200/my_index2/_search");
        headerKey = "Content-Type";
        headerData = "application/json";
    }

    public void setRestParamsForRandomQueries(int size, int seed) throws MalformedURLException {
        String seedStr;
        if(seed <= 0){
            seedStr = "";
        }else {
            seedStr = "\"seed\":" + seed + ",";
        }
        postData = "{\"query\":{\"function_score\":"+ "{\"random_score\":{" +
                    seedStr +  "\"field\":\"_seq_no\"}}}," +
                    "\"size\":" + size + "}";
        System.out.println(postData);
    }

    public void setRestParamsForStandardQuery(List<String> queryList, int size) throws MalformedURLException {
        String queryStr = formatQueryListForRest(queryList);

        postData = "{\"query\":{\"match\":{\"title\":\""  +
                queryStr + "\"}}," +
                "\"size\":" + size + "}";
    }

    private String formatQueryListForRest(List<String> query) throws NullPointerException{
        if(query.size() == 0){
            throw new NullPointerException();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(query.get(0));
        for (int i = 1; i < query.size(); i++) {
            sb.append(" ");
            sb.append(query.get(i));
        }
        return sb.toString();
    }

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

}// end of class
