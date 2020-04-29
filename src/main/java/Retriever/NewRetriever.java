package Retriever;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.WeightBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.*;


import java.io.IOException;

public class NewRetriever {

    // method for retrieving docs related to list of ids
    public static AbstractMap<String, List<String>> multiGetList(List<String> ids) throws IOException {
        AbstractMap<String, List<String>> allIdsAndLinked = new HashMap<>();

        if (ids.isEmpty()){
            return allIdsAndLinked;
        }

        // set-up
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        // add ids to request
        MultiGetRequest request = new MultiGetRequest();
        for (String id : ids) {
            request.add(new MultiGetRequest.Item("index_articles", id));
        }

        // request
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);

        // parsing
        for (MultiGetItemResponse item :response.getResponses()) {
            List<String> docAndLinked = new ArrayList<>();

            String id = item.getId();
            docAndLinked.add(id);

            // if the item existed
            GetResponse itemResponse = item.getResponse();
            if (itemResponse.isExists()) {
                Map<String, Object> sourceAsMap = itemResponse.getSourceAsMap();
                docAndLinked.addAll((List<String>)  sourceAsMap.get("inCitations"));
                docAndLinked.addAll((List<String>)  sourceAsMap.get("outCitations"));
            } else {
                //System.out.println("No item response in new retriever");
            }

            allIdsAndLinked.put(id, docAndLinked);
        }

        // terminate call
        client.close();

        return allIdsAndLinked;
    }

    // method for querying elastic using a list of search terms
    public static SearchHits queryElastic(List<String> queryList, int sizeOfRetrievedList) throws IOException {
        // create the query string
        StringBuilder sb = new StringBuilder();
        for (String s: queryList) {
            sb.append(s);
            sb.append(" ");
        }
        String queryStr = sb.toString();

        // create the query
        QueryBuilder query =  new MatchQueryBuilder("title", queryStr);
        WeightBuilder scorer = new WeightBuilder().setWeight(2);
        QueryBuilder filter1 = QueryBuilders.existsQuery("inCitations");
        QueryBuilder filter2 = QueryBuilders.existsQuery("outCitations");
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(filter1, scorer),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(filter2, scorer)
        };
        CombineFunction combineWithOriginalQuery = CombineFunction.MULTIPLY;
        QueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(query,filterFunctionBuilders)
                .boostMode(combineWithOriginalQuery);

        // return results from elastic
        return retriveThroughQuery(functionScoreQueryBuilder, sizeOfRetrievedList);
    }


    // method for retriving docs using a query
    private static SearchHits retriveThroughQuery(QueryBuilder queryBuilder, int sizeOfRetrievedList) throws IOException {

        // set-up
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(sizeOfRetrievedList);

        // retrieving
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // terminating call
        client.close();

        return searchResponse.getHits();
    }
}
