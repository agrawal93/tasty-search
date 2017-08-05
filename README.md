# tasty-search
Tasty Search - A search engine to search gourmet food reviews data and return the top K reviews that have the highest overlap with the input query.

## Requirements
1. [Use the data set available here](http://snap.stanford.edu/data/web-FineFoods.html)
2. Randomly sample 100K reviews from the above set for the index
3. Build an API that returns top K highest scoring documents for any given query, where score is defined by number of query tokens present in the review (irrespective of the frequency). In case of a tie, we use the "review/score" attribute of the review.
4. Queries are input as a set of tokens. It can be assumed that queries are between 1-10.
5. The text to be considered for computing score come from the attributes: [ "review/summary", "review/text" ]
6. Set K to 20
7. Expose a simple REST interface for the search. A simple web form to perform search on the server itself would be great.

Your server can be inmemory only so don’t worry about persistence etc. Typically, your server should read in the
review data and build its internal data structures to allow it to answer queries quickly thereafter.

## Technologies used
1. Spring Boot Framework
2. Tomcat (embedded)
3. Java 8
4. Maven Build Framework

## Configurations (Can be changed in src/main/resources/application.properties)
- Base Path: /tasty-search
- Server Port: 8080

## Defaults
- REST Endpoint to fetch top 20 reviews: `` http://<host>:<port>/<base-path>/search?query=... ``
- REST Endpoint to fetch top K reviews: `` http://<host>:<port>/<base-path>/search/{K}?query=... ``
- THREADS: 50 [ modify before building: src/main/java/com/agrawal/tasty/search/main/MainApplication.java ]
- SAMPLED_DATA_LIMIT: 100000 [ modify before building: src/main/java/com/agrawal/tasty/search/main/MainApplication.java ]

## How to run
1. Clone the repository: `` git clone https://github.com/agrawal93/tasty-search.git ``
2. Build the project: `` mvn clean install ``
3. Run the project: `` java -jar target\tasty-search-1.0.0.jar ``
4. Access the search web form at `` http://localhost:8080/tasty-search ``

## Architecture
![Tasty Search](Tasty%20Search.jpg)

## Approach
1. The file contains roughly 500K reviews. Need to sample it to 100K, but randomly. Since, each review starts with "product/productId", I indexed the particular review (unique id assigned) with the line number. Then shuffled the index to get `SAMPLED_DATA_LIMIT` reviews and read them using RandomAccessFile.
2. Once the reviews were read, we need to store it in some kind of structure. Trie would be a better suited data structure since the complexities are as follows:
   - inserting would be O(N) where N is the length of a given token.
   - querying would be O(N) where N is the length of a given token.
3. But, we don't use Trie for each review. Instead, we make a customized Trie, which would be common to all reviews. At the end of each token, we store a set of all the review ids (generated in step 1) in which that particular token exists. Takes roughly about 25 minutes to read and load 100K sampled reviews into the structure.
4. Once loaded, any query can be searched through the Trie node and all the reviews that contain the structure. The Review class can sort the list of reviews using the Comparable interface implemented (which sorts based on queryHits and reviewScore). For sorting the same, we leverage the TreeSet class provided by Java Collections Framework.
5. Output the top K reviews from the TreeSet.

## Suggested Improvement
1. Sort the query tokens lexicographically.
2. For each token in tokens
   - search for current token
   - if next token starts with more than half of the current token, then traverse back on the Trie to reach the common node earlier (maybe a few steps less). Would help if the tokens are quite long.
   - else start from the root of Trie again
