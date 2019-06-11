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
                                        # to skip container image registries
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
kubectl apply -f manifests/kotlin-app-deployment.yaml
```

List the services running on the cluster.

```
$ minikube service list --namespace fluentd-demo
|--------------|---------------|-----------------------------|
|  NAMESPACE   |     NAME      |             URL             |
|--------------|---------------|-----------------------------|
| fluentd-demo | elasticsearch | http://192.168.99.100:30643 |
| fluentd-demo | kibana        | http://192.168.99.100:31616 |
| fluentd-demo | kotlin        | http://192.168.99.100:31953 |
|--------------|---------------|-----------------------------|
```

First, hit the kotlin service at `/articles` a few times, maybe 10 or 15 hits,
to generate some logs. In my case, based on the output table above, it's
`http://192.168.99.100:31953/articles`.

Then, open the Kibana UI, also using the URL listing in your case, to configure
the a data source from ElasticSearch.

1. In the Home screen, after "explore on my own", go to "Connect to your
   Elasticsearch index".
2. In the Create Index Patter screen, step 1 of 2, enter `logstash*` as you index
   pattern. Fluentd creates this index in ES (one per day) when using Logstash
   data input patterns (the default one).
3. In the next step, step 2 of 2, select `@timestamp` as your Time filter field
   name to instruct Kibana to use this field to order entries. Then finally,
   hit the button to "Create index pattern".

Now you're ready to explore some data. Find the "Discover" on the left
navigation bar. It will open a graph and a table with all sorts of logs from
different components of the cluster. You can filter your own app logs using the
"Filters" field with `kubernetes.container_name : "kotlin"`. After hitting
"update" or "refresh" button, the results are updated to list one the app logs.

You may want to generate more logs at this momment and update the dashboard to
see them populaing the graph.

You can select the time frame in the graph or change the range (15 minutes by
default). You can also open the log entries from the list to see the structure
of each entry. The default config instructs Fluentd to create a few fields with
information from your Kubernetes nodes, namespaces and service names that
generated the log entries. Custom config may be used to parse the `log` field
to generete new extra indexable fields (e.g. search by user, by api path, by
remote IP of the request etc, or whatever data you may have in your logs).


Clean up:
```
minikube delete
eval $(minikube docker-env -u)         # back to docker daemon from localhost
```
