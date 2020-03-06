Maven High lvl Rest
===
https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-getting-started-maven.html

- Use query builder:
https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-java-builders.html


Multisearch
===
https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/_search_apis.html



How to build a query
===
https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-query-dsl.html


- terms query: Find documents which contain any of the exact terms specified in the field specified. https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-term-level-queries.html
(Kanske ngt för combining subqueries?)

-  bool query: The default query for combining multiple leaf or compound query clauses, as must, should, must_not, or filter clauses. The must and should clauses have their scores combined — the more matching clauses, the better — while the must_not and filter clauses are executed in filter context.
https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-compound-queries.html
(Om jag använder den här kanske jag kommer runt att elastic kör boolean AND på vanliga sökningar.)


Related to subqueries
===
https://www.elastic.co/guide/en/elasticsearch/reference/7.6/query-dsl-dis-max-query.html#query-dsl-dis-max-query

Multi-get
===
https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.6/java-rest-high-document-multi-get.html
