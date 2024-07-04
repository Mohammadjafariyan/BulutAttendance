package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.HrLetter;
import com.bulut.attendance.repository.HrLetterRepository;
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
 * Spring Data Elasticsearch repository for the {@link HrLetter} entity.
 */
public interface HrLetterSearchRepository extends ElasticsearchRepository<HrLetter, UUID>, HrLetterSearchRepositoryInternal {}

interface HrLetterSearchRepositoryInternal {
    Page<HrLetter> search(String query, Pageable pageable);

    Page<HrLetter> search(Query query);

    @Async
    void index(HrLetter entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class HrLetterSearchRepositoryInternalImpl implements HrLetterSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final HrLetterRepository repository;

    HrLetterSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, HrLetterRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<HrLetter> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<HrLetter> search(Query query) {
        SearchHits<HrLetter> searchHits = elasticsearchTemplate.search(query, HrLetter.class);
        List<HrLetter> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(HrLetter entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), HrLetter.class);
    }
}
