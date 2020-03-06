package Retriever;

import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewRetriver {

    public static void multiGet(List<String> ids) throws IOException {
        List<List<String>> allIdsAndLinked = new ArrayList<>();

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

            }

            allIdsAndLinked.add(docAndLinked);
        }

        // System.out.println(allIdsAndLinked);

        // terminate call
        client.close();
    }

    public static void newRetriver() throws IOException {

        // set-up
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // shouldn't I have which index to query somewhere???
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", "regression linear");


        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);



        // retrieving
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // parsing
        SearchHits hits = searchResponse.getHits();

        TotalHits totalHits = hits.getTotalHits();
        // the total number of hits, must be interpreted in the context of totalHits.relation
        long numHits = totalHits.value;
        // whether the number of hits is accurate (EQUAL_TO) or a lower bound of the total (GREATER_THAN_OR_EQUAL_TO)
        TotalHits.Relation relation = totalHits.relation;
        float maxScore = hits.getMaxScore();

        SearchHit[] searchHits = hits.getHits();
        System.out.println("number of hits " + numHits);
        for (SearchHit hit : searchHits) {
            String id = hit.getId();
            float score = hit.getScore();

            System.out.println("id " + id + ", score " + score);
            System.out.println(hit);
        }

        // terminating call
        client.close();
    }
}
