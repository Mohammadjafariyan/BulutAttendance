package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.AccProccParameter;
import com.bulut.attendance.repository.AccProccParameterRepository;
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
 * Spring Data Elasticsearch repository for the {@link AccProccParameter} entity.
 */
public interface AccProccParameterSearchRepository
    extends ElasticsearchRepository<AccProccParameter, Long>, AccProccParameterSearchRepositoryInternal {}

interface AccProccParameterSearchRepositoryInternal {
    Page<AccProccParameter> search(String query, Pageable pageable);

    Page<AccProccParameter> search(Query query);

    @Async
    void index(AccProccParameter entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AccProccParameterSearchRepositoryInternalImpl implements AccProccParameterSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AccProccParameterRepository repository;

    AccProccParameterSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AccProccParameterRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AccProccParameter> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AccProccParameter> search(Query query) {
        SearchHits<AccProccParameter> searchHits = elasticsearchTemplate.search(query, AccProccParameter.class);
        List<AccProccParameter> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AccProccParameter entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AccProccParameter.class);
    }
}
