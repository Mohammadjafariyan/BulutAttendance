package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.Company;
import com.bulut.attendance.repository.CompanyRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Company} entity.
 */
public interface CompanySearchRepository extends ElasticsearchRepository<Company, Long>, CompanySearchRepositoryInternal {}

interface CompanySearchRepositoryInternal {
    Stream<Company> search(String query);

    Stream<Company> search(Query query);

    @Async
    void index(Company entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CompanySearchRepositoryInternalImpl implements CompanySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CompanyRepository repository;

    CompanySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CompanyRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Company> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Company> search(Query query) {
        return elasticsearchTemplate.search(query, Company.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Company entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Company.class);
    }
}
