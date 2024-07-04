package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.OrgUnit;
import com.bulut.attendance.repository.OrgUnitRepository;
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
 * Spring Data Elasticsearch repository for the {@link OrgUnit} entity.
 */
public interface OrgUnitSearchRepository extends ElasticsearchRepository<OrgUnit, UUID>, OrgUnitSearchRepositoryInternal {}

interface OrgUnitSearchRepositoryInternal {
    Page<OrgUnit> search(String query, Pageable pageable);

    Page<OrgUnit> search(Query query);

    @Async
    void index(OrgUnit entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class OrgUnitSearchRepositoryInternalImpl implements OrgUnitSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OrgUnitRepository repository;

    OrgUnitSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OrgUnitRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<OrgUnit> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<OrgUnit> search(Query query) {
        SearchHits<OrgUnit> searchHits = elasticsearchTemplate.search(query, OrgUnit.class);
        List<OrgUnit> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(OrgUnit entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), OrgUnit.class);
    }
}
