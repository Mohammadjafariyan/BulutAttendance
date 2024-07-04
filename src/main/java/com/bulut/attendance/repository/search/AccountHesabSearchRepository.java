package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.AccountHesab;
import com.bulut.attendance.repository.AccountHesabRepository;
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
 * Spring Data Elasticsearch repository for the {@link AccountHesab} entity.
 */
public interface AccountHesabSearchRepository extends ElasticsearchRepository<AccountHesab, UUID>, AccountHesabSearchRepositoryInternal {}

interface AccountHesabSearchRepositoryInternal {
    Page<AccountHesab> search(String query, Pageable pageable);

    Page<AccountHesab> search(Query query);

    @Async
    void index(AccountHesab entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class AccountHesabSearchRepositoryInternalImpl implements AccountHesabSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AccountHesabRepository repository;

    AccountHesabSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AccountHesabRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AccountHesab> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AccountHesab> search(Query query) {
        SearchHits<AccountHesab> searchHits = elasticsearchTemplate.search(query, AccountHesab.class);
        List<AccountHesab> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AccountHesab entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), AccountHesab.class);
    }
}
