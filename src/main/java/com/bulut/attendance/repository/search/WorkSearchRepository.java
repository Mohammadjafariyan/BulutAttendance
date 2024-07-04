package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.Work;
import com.bulut.attendance.repository.WorkRepository;
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
 * Spring Data Elasticsearch repository for the {@link Work} entity.
 */
public interface WorkSearchRepository extends ElasticsearchRepository<Work, UUID>, WorkSearchRepositoryInternal {}

interface WorkSearchRepositoryInternal {
    Page<Work> search(String query, Pageable pageable);

    Page<Work> search(Query query);

    @Async
    void index(Work entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class WorkSearchRepositoryInternalImpl implements WorkSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WorkRepository repository;

    WorkSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, WorkRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Work> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Work> search(Query query) {
        SearchHits<Work> searchHits = elasticsearchTemplate.search(query, Work.class);
        List<Work> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Work entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Work.class);
    }
}
