package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.TaxTemplate;
import com.bulut.attendance.repository.TaxTemplateRepository;
import java.util.List;
import java.util.UUID;
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
 * Spring Data Elasticsearch repository for the {@link TaxTemplate} entity.
 */
public interface TaxTemplateSearchRepository extends ElasticsearchRepository<TaxTemplate, UUID>, TaxTemplateSearchRepositoryInternal {}

interface TaxTemplateSearchRepositoryInternal {
    Page<TaxTemplate> search(String query, Pageable pageable);

    Page<TaxTemplate> search(Query query);

    @Async
    void index(TaxTemplate entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class TaxTemplateSearchRepositoryInternalImpl implements TaxTemplateSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TaxTemplateRepository repository;

    TaxTemplateSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TaxTemplateRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TaxTemplate> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TaxTemplate> search(Query query) {
        SearchHits<TaxTemplate> searchHits = elasticsearchTemplate.search(query, TaxTemplate.class);
        List<TaxTemplate> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TaxTemplate entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), TaxTemplate.class);
    }
}
