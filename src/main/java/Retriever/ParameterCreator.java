package Retriever;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ParameterCreator {
    URL url;
    String postData;
    String headerKey;
    String headerData;

    public ParameterCreator() throws MalformedURLException {
        url = new URL("http://localhost:9200/index_articles/_search");
        headerKey = "Content-Type";
        headerData = "application/json";
    }

    public void setRestParamsForRandomQueries(int size, int seed){
        String seedStr;
        if(seed <= 0){
            seedStr = "";
        }else {
            seedStr = "\"seed\":" + seed + ",";
        }
        postData = "{\"query\":{\"function_score\":"+ "{\"random_score\":{" +
                seedStr +  "\"field\":\"_seq_no\"}}}," +
                "\"size\":" + size + "}";
    }

    public void setRestParamsForStandardQuery(List<String> queryList, int size){
        String queryStr = formatQueryListForRest(queryList);
        postData = "{\"query\":{\"match\":{\"title\":\""  +
                queryStr + "\"}}," +
                "\"size\":" + size + "}";
    }

    public void setRestParamsForClusters(List<String> queryList, int size){
        String queryStr = formatQueryListClusterForRest(queryList);
        postData = "{" +
                        "\"query\":" +
                    "{" +
                        "\"bool\":" + "" +
                    "{" +
                        "\"must\":" +
                    "[" +
                    "{" +
                        "\"bool\":" +
                    "{" +
                        "\"should\":" +
                    "["  +
                queryStr +
                "]" +
                "}" +
                "}" +
                "]" +
                "}" +
                "}" +
                "}";
        System.out.println(postData);
    }

    private String formatQueryListClusterForRest(List<String> query) throws NullPointerException{
        if(query.size() == 0){
            throw new NullPointerException();
        }

        query = query.subList(0, 3);
        StringBuilder sb = new StringBuilder();
        sb.append("{\"term\": {\"_id\": ");
        sb.append(query.get(0));
        sb.append("}},");
        for (int i = 1; i < query.size()-1; i++) {
            sb.append("{\"term\": {\"_id\": ");
            sb.append(query.get(i));
            sb.append("}},");
        }
        sb.append("{\"term\": {\"_id\": ");
        sb.append(query.get(query.size()-1));
        sb.append("}}");

        return sb.toString();
    }

    public void setRestParamsForSingleId(String queryStr) {
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
