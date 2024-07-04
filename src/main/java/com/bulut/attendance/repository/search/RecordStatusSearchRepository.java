package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.RecordStatus;
import com.bulut.attendance.repository.RecordStatusRepository;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link RecordStatus} entity.
 */
public interface RecordStatusSearchRepository extends ElasticsearchRepository<RecordStatus, UUID>, RecordStatusSearchRepositoryInternal {}

interface RecordStatusSearchRepositoryInternal {
    Stream<RecordStatus> search(String query);

    Stream<RecordStatus> search(Query query);

    @Async
    void index(RecordStatus entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class RecordStatusSearchRepositoryInternalImpl implements RecordStatusSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final RecordStatusRepository repository;

    RecordStatusSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, RecordStatusRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<RecordStatus> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<RecordStatus> search(Query query) {
        return elasticsearchTemplate.search(query, RecordStatus.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(RecordStatus entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), RecordStatus.class);
    }
}
