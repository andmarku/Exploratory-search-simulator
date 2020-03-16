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
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.query.functionscore.WeightBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.*;

import static org.elasticsearch.client.Requests.searchRequest;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.search.builder.SearchSourceBuilder.*;


import java.io.IOException;

public class NewRetriver {

    public static List<List<String>> multiGetList(List<String> ids) throws IOException {
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

        return allIdsAndLinked;
    }

    public static void queryTest() throws IOException {

        // shouldn't I have which index to query somewhere???
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", "america sweden examining strange cluster regression linear information retrieval storing and");
        // SearchHits hits0 = newRetriver(matchQueryBuilder);
      /*  for (SearchHit hit : hits0) {
            String title = hit.getSourceAsMap().get("title").toString();
            System.out.println("title " + title + ", score " + hit.getScore());
        }*/


        // Function Score Query
        QueryBuilder query =  new MatchQueryBuilder("title", "regression linear information");
        WeightBuilder scorer = new WeightBuilder().setWeight(5);
        QueryBuilder filter1 = QueryBuilders.existsQuery("inCitations");
        QueryBuilder filter2 = QueryBuilders.existsQuery("outCitations");
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(filter1, scorer),
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(filter2, scorer)
        };
        CombineFunction combineWithOriginalQuery = CombineFunction.MULTIPLY;
        QueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(query,filterFunctionBuilders)
                .boostMode(combineWithOriginalQuery);

        SearchHits hits2 = newRetriver(functionScoreQueryBuilder);

        newParser(hits2);

    }


    public static List<Map> newParser(SearchHits hits){
        List<Map> parsedResult = new ArrayList<>();
        Map<String, Double> retrievedDocs = new TreeMap<>();
        Map<String, List<Double>> linkedDocs = new HashMap<>();

        System.out.println(hits.getTotalHits());
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            retrievedDocs.put(hit.getId(), (double) hit.getScore());
            List<Double> citations = citationsAsList(hit);
            linkedDocs.put(hit.getId(), citations);

            //System.out.println(hit.getSourceAsMap().get("inCitations"));
            //System.out.println(hit.getSourceAsMap().get("outCitations"));
            System.out.println(hit);
        }

        parsedResult.add(retrievedDocs);
        parsedResult.add(linkedDocs);
        return parsedResult;
    }

    public static List<Double> citationsAsList(SearchHit hit){
        List<Double> linkedDocsInList = new ArrayList<>();
      /*  List<Double> inCitations = new ArrayList(Arrays.asList((Double[]) hit.getSourceAsMap().get("inCitations")));
        List<Double> outCitations = new ArrayList(Arrays.asList((Double[]) hit.getSourceAsMap().get("outCitations")));
        linkedDocsInList.addAll(inCitations);
        linkedDocsInList.addAll(outCitations);*/
        return linkedDocsInList;
    }


    public static SearchHits newRetriver(QueryBuilder queryBuilder) throws IOException {

        // set-up
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(3);

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
        System.out.println("\n number of hits " + numHits);
        System.out.println("number of returned hits " + searchHits.length);
        for (SearchHit hit : searchHits) {
            String id = hit.getId();
            float score = hit.getScore();
            String title = hit.getSourceAsMap().get("title").toString();

            System.out.println("title " + title + ", score " + score);
            //System.out.println(hit);
        }

        // terminating call
        client.close();
        return searchResponse.getHits();
    }
}
