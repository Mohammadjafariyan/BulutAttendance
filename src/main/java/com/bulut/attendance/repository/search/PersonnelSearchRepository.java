package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.Personnel;
import com.bulut.attendance.repository.PersonnelRepository;
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
 * Spring Data Elasticsearch repository for the {@link Personnel} entity.
 */
public interface PersonnelSearchRepository extends ElasticsearchRepository<Personnel, UUID>, PersonnelSearchRepositoryInternal {}

interface PersonnelSearchRepositoryInternal {
    Page<Personnel> search(String query, Pageable pageable);

    Page<Personnel> search(Query query);

    @Async
    void index(Personnel entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class PersonnelSearchRepositoryInternalImpl implements PersonnelSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PersonnelRepository repository;

    PersonnelSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PersonnelRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Personnel> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Personnel> search(Query query) {
        SearchHits<Personnel> searchHits = elasticsearchTemplate.search(query, Personnel.class);
        List<Personnel> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Personnel entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Personnel.class);
    }
}
