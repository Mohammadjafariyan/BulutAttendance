package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.HrLetterParameter;
import com.bulut.attendance.repository.HrLetterParameterRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link HrLetterParameter} entity.
 */
public interface HrLetterParameterSearchRepository
    extends ElasticsearchRepository<HrLetterParameter, Long>, HrLetterParameterSearchRepositoryInternal {}

interface HrLetterParameterSearchRepositoryInternal {
    Stream<HrLetterParameter> search(String query);

    Stream<HrLetterParameter> search(Query query);

    @Async
    void index(HrLetterParameter entity);

    @Async
    void deleteFromIndexById(Long id);
}

class HrLetterParameterSearchRepositoryInternalImpl implements HrLetterParameterSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final HrLetterParameterRepository repository;

    HrLetterParameterSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, HrLetterParameterRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<HrLetterParameter> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<HrLetterParameter> search(Query query) {
        return elasticsearchTemplate.search(query, HrLetterParameter.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(HrLetterParameter entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), HrLetterParameter.class);
    }
}
