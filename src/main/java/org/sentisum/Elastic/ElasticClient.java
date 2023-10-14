package org.sentisum.Elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;


public class ElasticClient {
    // URL and API key
    // final String serverUrl = "http://localhost:9200";
    //final String apiKey = "VnVhQ2ZHY0JDZGJrU...";
   String serverUrl;
   String apiKey;

    public ElasticClient(final String url, final String apiKey){
        this.serverUrl = url;
        this.apiKey = apiKey;
    }


    public ElasticsearchClient getClient() {
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        return esClient;
    }


}
