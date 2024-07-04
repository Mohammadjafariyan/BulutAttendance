package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.WorkItem;
import com.bulut.attendance.repository.WorkItemRepository;
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
 * Spring Data Elasticsearch repository for the {@link WorkItem} entity.
 */
public interface WorkItemSearchRepository extends ElasticsearchRepository<WorkItem, UUID>, WorkItemSearchRepositoryInternal {}

interface WorkItemSearchRepositoryInternal {
    Page<WorkItem> search(String query, Pageable pageable);

    Page<WorkItem> search(Query query);

    @Async
    void index(WorkItem entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class WorkItemSearchRepositoryInternalImpl implements WorkItemSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WorkItemRepository repository;

    WorkItemSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, WorkItemRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<WorkItem> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<WorkItem> search(Query query) {
        SearchHits<WorkItem> searchHits = elasticsearchTemplate.search(query, WorkItem.class);
        List<WorkItem> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(WorkItem entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), WorkItem.class);
    }
}
