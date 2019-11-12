import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

class RetrieverParameterCreator {
    URL url;
    String postData;
    String headerKey;
    String headerData;

    RetrieverParameterCreator() throws MalformedURLException {
        url = new URL("http://localhost:9200/index_articles/_search");
        url = new URL("http://10.10.6.160:9200/my_index2/_search");
        headerKey = "Content-Type";
        headerData = "application/json";
    }

    void setRestParamsForRandomQueries(int size, int seed){
        String seedStr;
        if(seed <= 0){
            seedStr = "";
        }else {
            seedStr = "\"seed\":" + seed;
        }
        /*postData = "{\"query\":{\"function_score\":"+ "{\"random_score\":{" +
                seedStr +  "\"field\":\"_seq_no\"}}}," +
                "\"size\":" + size + "}";*/
        postData = "{\"query\":{\"function_score\":"+ "{\"random_score\":{" +
                seedStr +  "}}}," +
                "\"size\":" + size + "}";
    }

    void setRestParamsForStandardQuery(List<String> queryList, int size){
        String queryStr = formatQueryListForRest(queryList);
        postData = "{\"query\":{\"match\":{\"title\":\""  +
                queryStr + "\"}}," +
                "\"size\":" + size + "}";
    }

    void setRestParamsForSingleId(String queryStr) {
        postData = "{\"query\":{\"match\":{\"_id\":\""  +
                queryStr + "\"}}," +
                "\"size\":" + 1 + "}";
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
}
