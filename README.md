# Spring Boot ELK
Run ElasticSearch, Logstash and Kibana with the default configurations in localhost.
For Logstash, you need to provide a conf file describing where to listen for log entries
and how to parse and filter them. There's a `logstach.conf` expecting entries from
`application.log` file, all in this same dir to keep things simple.

```
$ELASTIC_SEARCH_DIR/bin/elasticsearch
$LOGSTASH_DIR/bin/logstash -f logstash.conf
$KIBANA_DIR/bin/kibana
```

Next, we need to tell Kibana to connect to an ES index.

You can open Kibana at http://localhost:5601 and find "Connect your Elastisearch index"
in the home screen. Since Logstash creates an index with its own name and version, use
`logstash*` as index pattern, then next set `@timestamp` as the time field, then hit
the create index pattern button.

Now you can open the browser or use curl to hit the application a few times
```
# run the Spring Boot app
./gradlew bootRun

# in another tab
curl http://localhost:8080/articles
```

Back to Kibana, find the "discover" button at the left nav and you should a graph with
log counts over time, and a table with the list of logs from the selected period in the
filter bar.