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
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;

public class NewRetriver {

    public static void multiGet() throws IOException {
        // set-up
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        MultiGetRequest request = new MultiGetRequest();
        request.add(new MultiGetRequest.Item("index_articles", "6bccc6a15bb56c459cd9b24858f3d4db139912e5"));
        request.add(new MultiGetRequest.Item("index_articles", "a1bb2712c5cd51c6918bd19ac84eb1af88325189"));

        // request
        MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);

        // parsing
        for (MultiGetItemResponse firstItem :response.getResponses()) {
            GetResponse firstGet = firstItem.getResponse();
            String id = firstItem.getId();
            System.out.println(id);
            if (firstGet.isExists()) {
                String sourceAsString = firstGet.getSourceAsString();
                Map<String, Object> sourceAsMap = firstGet.getSourceAsMap();
                System.out.println(sourceAsString);
                System.out.println(sourceAsMap.get("inCitations"));
            } else {

            }
        }

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
