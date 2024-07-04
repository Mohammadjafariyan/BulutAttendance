package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.PersonnelStatus;
import com.bulut.attendance.repository.PersonnelStatusRepository;
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
 * Spring Data Elasticsearch repository for the {@link PersonnelStatus} entity.
 */
public interface PersonnelStatusSearchRepository
    extends ElasticsearchRepository<PersonnelStatus, UUID>, PersonnelStatusSearchRepositoryInternal {}

interface PersonnelStatusSearchRepositoryInternal {
    Page<PersonnelStatus> search(String query, Pageable pageable);

    Page<PersonnelStatus> search(Query query);

    @Async
    void index(PersonnelStatus entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class PersonnelStatusSearchRepositoryInternalImpl implements PersonnelStatusSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PersonnelStatusRepository repository;

    PersonnelStatusSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PersonnelStatusRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<PersonnelStatus> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<PersonnelStatus> search(Query query) {
        SearchHits<PersonnelStatus> searchHits = elasticsearchTemplate.search(query, PersonnelStatus.class);
        List<PersonnelStatus> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(PersonnelStatus entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), PersonnelStatus.class);
    }
}
