package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.AccountingProcedureExecution;
import com.bulut.attendance.repository.AccountingProcedureExecutionRepository;
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
 * Spring Data Elasticsearch repository for the {@link AccountingProcedureExecution} entity.
 */
public interface AccountingProcedureExecutionSearchRepository
    extends ElasticsearchRepository<AccountingProcedureExecution, Long>, AccountingProcedureExecutionSearchRepositoryInternal {}

interface AccountingProcedureExecutionSearchRepositoryInternal {
    Page<AccountingProcedureExecution> search(String query, Pageable pageable);

    Page<AccountingProcedureExecution> search(Query query);

    @Async
    void index(AccountingProcedureExecution entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AccountingProcedureExecutionSearchRepositoryInternalImpl implements AccountingProcedureExecutionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AccountingProcedureExecutionRepository repository;

    AccountingProcedureExecutionSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        AccountingProcedureExecutionRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AccountingProcedureExecution> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AccountingProcedureExecution> search(Query query) {
        SearchHits<AccountingProcedureExecution> searchHits = elasticsearchTemplate.search(query, AccountingProcedureExecution.class);
        List<AccountingProcedureExecution> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AccountingProcedureExecution entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AccountingProcedureExecution.class);
    }
}
