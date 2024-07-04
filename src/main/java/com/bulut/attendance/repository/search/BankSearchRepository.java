package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.Bank;
import com.bulut.attendance.repository.BankRepository;
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
 * Spring Data Elasticsearch repository for the {@link Bank} entity.
 */
public interface BankSearchRepository extends ElasticsearchRepository<Bank, UUID>, BankSearchRepositoryInternal {}

interface BankSearchRepositoryInternal {
    Page<Bank> search(String query, Pageable pageable);

    Page<Bank> search(Query query);

    @Async
    void index(Bank entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class BankSearchRepositoryInternalImpl implements BankSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BankRepository repository;

    BankSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, BankRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Bank> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Bank> search(Query query) {
        SearchHits<Bank> searchHits = elasticsearchTemplate.search(query, Bank.class);
        List<Bank> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Bank entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), Bank.class);
    }
}
