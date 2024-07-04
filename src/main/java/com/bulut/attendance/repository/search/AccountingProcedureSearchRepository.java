package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.AccountingProcedure;
import com.bulut.attendance.repository.AccountingProcedureRepository;
import java.util.List;
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
 * Spring Data Elasticsearch repository for the {@link AccountingProcedure} entity.
 */
public interface AccountingProcedureSearchRepository
    extends ElasticsearchRepository<AccountingProcedure, Long>, AccountingProcedureSearchRepositoryInternal {}

interface AccountingProcedureSearchRepositoryInternal {
    Page<AccountingProcedure> search(String query, Pageable pageable);

    Page<AccountingProcedure> search(Query query);

    @Async
    void index(AccountingProcedure entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AccountingProcedureSearchRepositoryInternalImpl implements AccountingProcedureSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AccountingProcedureRepository repository;

    AccountingProcedureSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AccountingProcedureRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AccountingProcedure> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AccountingProcedure> search(Query query) {
        SearchHits<AccountingProcedure> searchHits = elasticsearchTemplate.search(query, AccountingProcedure.class);
        List<AccountingProcedure> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AccountingProcedure entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AccountingProcedure.class);
    }
}
