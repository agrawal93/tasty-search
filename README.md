# tasty-search
Tasty Search - A search engine to search gourmet food reviews data and return the top K reviews that have the highest overlap with the input query.

## Problem Statement Requirements
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
| Name | Description | Default |
|---|---|---|
| server.contextPath | The base URL to be allocated on deployment | /tasty-search |
| server.port | The port on which the application needs to be deployed | 8080 |
| load.threads | Number of threads to concurrently load reviews into the structure | 25 |
| sample.data.limit | Number of reviews to be sampled from the review file | 100000 |
| review.file | Absolute Path of the file to be considered for loading reviews | ./foods.txt |

## Defaults
- REST Endpoint to fetch top 20 reviews: `` http://<host>:<port>/<base-path>/search?query=... ``
- REST Endpoint to fetch top K reviews: `` http://<host>:<port>/<base-path>/search/{K}?query=... ``

## How to run
1. Clone the repository: `` git clone https://github.com/agrawal93/tasty-search.git && cd tasty-search ``
2. Download the data-set: `` wget http://snap.stanford.edu/data/finefoods.txt.gz && gzip -d finefoods.txt.gz && mv finefoods.txt foods.txt ``
3. Build the project: `` mvn clean install ``
4. Run the project: `` java -jar target/tasty-search-1.0.0.jar ``
5. Access the search web form at `` http://localhost:8080/tasty-search ``

## Approach
1. The file contains roughly 500K reviews. Need to sample it to 100K, but randomly. Read the file sequentially and generate random boolean at each review to decide whether to consider that review or not.
2. Once the reviews were read, we need to store it in some kind of structure. Trie would be a better suited data structure since it can be used later for partial searches and the complexities are as follows:
   - inserting would be O(N) where N is the length of a given token.
   - querying would be O(N) where N is the length of a given token.
3. But, we don't use Trie for each review. Instead, we make a customized Trie, which would be common to all reviews. At the end of each token, we store a set of all the review ids (generated in step 1) in which that particular token exists. Takes roughly about 8 seconds to read and load 100K sampled reviews into the structure.
4. Once loaded, any query can be searched through the Trie node and all the reviews that contain the structure. The Review class can sort the list of reviews using the Comparable interface implemented (which sorts based on queryHits and reviewScore). For sorting the same, we leverage the TreeSet class provided by Java Collections Framework.
5. Output the top K reviews from the TreeSet.
