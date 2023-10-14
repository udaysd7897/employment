package org.sentisum;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import org.apache.logging.log4j.Logger;
import org.sentisum.Elastic.ElasticClient;
import org.sentisum.Model.Employee;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class App {
    public static void main(String args[]) {
        SpringApplication.run(App.class, args);
    }
}

//        ElasticsearchClient esClient = new ElasticClient("http://localhost:9200", "random").getClient();
////        esClien
//        String searchText = "IBM";
//
//        SearchResponse<Employee> response = esClient.search(s -> s
//                        .index("work")
//                        .query(q -> q
//                                .match(t -> t
//                                        .field("employer")
//                                        .query(searchText)
//                                )
//                        ),
//                Employee.class
//        );
//
//        TotalHits total = response.hits().total();
//        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
//
//        List<Hit<Employee>> hits = response.hits().hits();
//        for (Hit<Employee> hit: hits) {
//            Employee e = hit.source();
//            System.out.println("Found employee " + e.getTitle() + ", score " + hit.score());
