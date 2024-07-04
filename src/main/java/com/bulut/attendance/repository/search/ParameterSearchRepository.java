package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.Parameter;
import com.bulut.attendance.repository.ParameterRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Parameter} entity.
 */
public interface ParameterSearchRepository extends ElasticsearchRepository<Parameter, Long>, ParameterSearchRepositoryInternal {}

interface ParameterSearchRepositoryInternal {
    Page<Parameter> search(String query, Pageable pageable);

    Page<Parameter> search(Query query);

    @Async
    void index(Parameter entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ParameterSearchRepositoryInternalImpl implements ParameterSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ParameterRepository repository;

    ParameterSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ParameterRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Parameter> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Parameter> search(Query query) {
        SearchHits<Parameter> searchHits = elasticsearchTemplate.search(query, Parameter.class);
        List<Parameter> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Parameter entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Parameter.class);
    }
}
