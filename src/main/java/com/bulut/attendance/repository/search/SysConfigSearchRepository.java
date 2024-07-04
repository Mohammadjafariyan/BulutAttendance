package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.SysConfig;
import com.bulut.attendance.repository.SysConfigRepository;
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
 * Spring Data Elasticsearch repository for the {@link SysConfig} entity.
 */
public interface SysConfigSearchRepository extends ElasticsearchRepository<SysConfig, UUID>, SysConfigSearchRepositoryInternal {}

interface SysConfigSearchRepositoryInternal {
    Page<SysConfig> search(String query, Pageable pageable);

    Page<SysConfig> search(Query query);

    @Async
    void index(SysConfig entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class SysConfigSearchRepositoryInternalImpl implements SysConfigSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SysConfigRepository repository;

    SysConfigSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SysConfigRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SysConfig> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SysConfig> search(Query query) {
        SearchHits<SysConfig> searchHits = elasticsearchTemplate.search(query, SysConfig.class);
        List<SysConfig> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SysConfig entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), SysConfig.class);
    }
}
