package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.AccProcStepExecution;
import com.bulut.attendance.repository.AccProcStepExecutionRepository;
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
 * Spring Data Elasticsearch repository for the {@link AccProcStepExecution} entity.
 */
public interface AccProcStepExecutionSearchRepository
    extends ElasticsearchRepository<AccProcStepExecution, Long>, AccProcStepExecutionSearchRepositoryInternal {}

interface AccProcStepExecutionSearchRepositoryInternal {
    Page<AccProcStepExecution> search(String query, Pageable pageable);

    Page<AccProcStepExecution> search(Query query);

    @Async
    void index(AccProcStepExecution entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AccProcStepExecutionSearchRepositoryInternalImpl implements AccProcStepExecutionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AccProcStepExecutionRepository repository;

    AccProcStepExecutionSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        AccProcStepExecutionRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AccProcStepExecution> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AccProcStepExecution> search(Query query) {
        SearchHits<AccProcStepExecution> searchHits = elasticsearchTemplate.search(query, AccProcStepExecution.class);
        List<AccProcStepExecution> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AccProcStepExecution entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AccProcStepExecution.class);
    }
}
