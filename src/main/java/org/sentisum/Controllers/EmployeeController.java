package org.sentisum.Controllers;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.sentisum.Model.Employee;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RestController
@RequestMapping("/")
public class EmployeeController {




    private ElasticsearchOperations elasticsearchOperations;

    EmployeeController(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @GetMapping("/compensation_data")
    public List<Employee> processQueries(
            @RequestParam Map<String, Object> params)
    {
        BoolQueryBuilder queryBuilder =
                QueryBuilders.boolQuery();

        List<SortBuilder<?>> sortBuilders = new ArrayList<>();

        Collection<String> prjctFields = new ArrayList<>();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            System.out.println(k + " " + v);

            if (v.toString().equals("desc")) {
                SortBuilder sortBuilder = SortBuilders.fieldSort(k.trim()).order(SortOrder.DESC);
                sortBuilders.add(sortBuilder);
                continue;
            }
            else if (v.toString().equals("asc")) {
                SortBuilder sortBuilder = SortBuilders.fieldSort(k.trim()).order(SortOrder.ASC);
                sortBuilders.add(sortBuilder);
                continue;
            }

            String[] fieldList = k.trim().split("[()]");
            if (fieldList[0].equals("fields")) {
                prjctFields.addAll(Arrays.asList(v.toString().trim().split(",")));
            } else if (fieldList.length == 1) {
                queryBuilder.filter((QueryBuilders.matchQuery(k, v)));
            } else {
                switch (fieldList[1]) {
                    case "gt":
                        queryBuilder.filter(QueryBuilders.rangeQuery(fieldList[0]).gt(v));
                        break;
                    case "gte":
                        queryBuilder.filter(QueryBuilders.rangeQuery(fieldList[0]).gte(v));
                        break;
                    case "lte":
                        queryBuilder.filter(QueryBuilders.rangeQuery(fieldList[0]).lte(v));
                        break;
                }
            }

        }

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(queryBuilder);

        if(!sortBuilders.isEmpty()){
            nativeSearchQueryBuilder.withSorts(sortBuilders);
        }

        if(!prjctFields.isEmpty()){
            System.out.println(prjctFields);
//            String[] includeFields = {"name", "time"};
            SourceFilter sourceFilter = new FetchSourceFilter(prjctFields.toArray(new String[0]), null);
//            FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includeFields, null);
//            sourceBuilder.fetchSource(fetchSourceContext);
            nativeSearchQueryBuilder.withSourceFilter(sourceFilter);
        }

        Query searchQuery = nativeSearchQueryBuilder
                .build();

        SearchHits<Employee> employeeSearchHits =
                elasticsearchOperations
                        .search(searchQuery, Employee.class,
                                IndexCoordinates.of("work"));

        List<Employee> matches = new ArrayList<Employee>();
        employeeSearchHits.forEach(srchHit->{
            matches.add(srchHit.getContent());
        });
        return matches;
    }

}
