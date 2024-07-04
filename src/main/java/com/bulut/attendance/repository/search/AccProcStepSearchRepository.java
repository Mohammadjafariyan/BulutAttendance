package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.AccProcStep;
import com.bulut.attendance.repository.AccProcStepRepository;
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
 * Spring Data Elasticsearch repository for the {@link AccProcStep} entity.
 */
public interface AccProcStepSearchRepository extends ElasticsearchRepository<AccProcStep, Long>, AccProcStepSearchRepositoryInternal {}

interface AccProcStepSearchRepositoryInternal {
    Page<AccProcStep> search(String query, Pageable pageable);

    Page<AccProcStep> search(Query query);

    @Async
    void index(AccProcStep entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AccProcStepSearchRepositoryInternalImpl implements AccProcStepSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AccProcStepRepository repository;

    AccProcStepSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AccProcStepRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AccProcStep> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AccProcStep> search(Query query) {
        SearchHits<AccProcStep> searchHits = elasticsearchTemplate.search(query, AccProcStep.class);
        List<AccProcStep> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AccProcStep entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AccProcStep.class);
    }
}
