About the thesis
===
In the thesis I analyse the effect of two components in a search engine on exploratory academic search using statistical methods (glmm and ANOVA). The components are added on top of my implementation of the open source search engine Elasticsearch and will modify the search result using citation links between the documents and though combining different queries. The data used in the thesis is a rouhgly 200 gb data set constisting of scholarly documents (represented by their titles, references and citations). For those interested in using similar data, the data can very generously be downloaded from SemanticScholar (http://api.semanticscholar.org/corpus/download/).

About the repository
===
This repository contains the code I have written for testing my hypothesis. In more detail it has code for:
- generating random queries by sampling the database,
- retrieving search results from my local Elasticsearch server corresponding to queries,
- running simulations over the various combinations of components studied in the thesis
- saving results, settings, and queries to files.
