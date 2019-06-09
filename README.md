# Spring Boot ELK

## Run the app with Docker

```
docker build -t kotlin-app:1.0.0 .
docker run --rm --name=kotlin -p 8080:8080 kotlin-app:1.0.0
```

## Run the app, ElasticSearch, Kibana and Fluentd with Minikube

```bash
minikube start --cpus 4 --memory 8192
eval $(minikube docker-env)             # use docker daemon from minikube VM
```

```bash
kubectl apply -f manifests/fluentd-demo-namespace.yaml
kubectl apply -f manifests/elasticsearch-singlenode.yaml
kubectl apply -f manifests/kibana.yaml

kubectl apply -f manifests/fluentd-rbac.yaml
kubectl apply -f manifests/fluentd-daemonset.yaml
```

In Kibana, connect to the Elastic Search index `logstash*` created by Fluentd.

```
eval $(minikube docker-env)
docker build -t kotlin-app:1.0.0 .

# no image registry is used, pull local image
kubectl apply -f manifests/kotlin-app-minikube-deployment.yaml
```

Clean up:
```
minikube delete
eval $(minikube docker-env -u)         # back to docker daemon from localhost
```

Make sure you hit the app service at `/article` once or more.
Then, in Kibana, you can browser the recent log entries or filter by `kubernetes.container_name : "kotlin"`.

## Run the old (classic) way

Without container support, things should be installed on the local machine first. Usually, installation instructions
is provided on the website of the product, where popular options include download a binary or zip/tar file with
everything ready to run.

Alternatively, you can use the package manager for your OS system to install things for you,
like Homebrew (Mac and Linux) or APT (Ubuntu/Debian Linux). Package managers may install other dependencies, create
specific system users to run and Systemd (Linux) or Launchd (Mac) scripts to run the package as a local service. A
few simple Google searches should provide easy instructions! :)

Run ElasticSearch, Logstash and Kibana with the default configurations in localhost.
For Logstash, you need to provide a conf file describing where to listen for log entries
and how to parse and filter them. There's a `logstash.conf` expecting entries from
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
