package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.OrgPosition;
import com.bulut.attendance.repository.OrgPositionRepository;
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
 * Spring Data Elasticsearch repository for the {@link OrgPosition} entity.
 */
public interface OrgPositionSearchRepository extends ElasticsearchRepository<OrgPosition, UUID>, OrgPositionSearchRepositoryInternal {}

interface OrgPositionSearchRepositoryInternal {
    Page<OrgPosition> search(String query, Pageable pageable);

    Page<OrgPosition> search(Query query);

    @Async
    void index(OrgPosition entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class OrgPositionSearchRepositoryInternalImpl implements OrgPositionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OrgPositionRepository repository;

    OrgPositionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OrgPositionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<OrgPosition> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<OrgPosition> search(Query query) {
        SearchHits<OrgPosition> searchHits = elasticsearchTemplate.search(query, OrgPosition.class);
        List<OrgPosition> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(OrgPosition entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), OrgPosition.class);
    }
}
