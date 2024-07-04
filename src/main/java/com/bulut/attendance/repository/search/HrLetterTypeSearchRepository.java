package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.HrLetterType;
import com.bulut.attendance.repository.HrLetterTypeRepository;
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
 * Spring Data Elasticsearch repository for the {@link HrLetterType} entity.
 */
public interface HrLetterTypeSearchRepository extends ElasticsearchRepository<HrLetterType, UUID>, HrLetterTypeSearchRepositoryInternal {}

interface HrLetterTypeSearchRepositoryInternal {
    Page<HrLetterType> search(String query, Pageable pageable);

    Page<HrLetterType> search(Query query);

    @Async
    void index(HrLetterType entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class HrLetterTypeSearchRepositoryInternalImpl implements HrLetterTypeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final HrLetterTypeRepository repository;

    HrLetterTypeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, HrLetterTypeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<HrLetterType> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<HrLetterType> search(Query query) {
        SearchHits<HrLetterType> searchHits = elasticsearchTemplate.search(query, HrLetterType.class);
        List<HrLetterType> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(HrLetterType entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), HrLetterType.class);
    }
}
