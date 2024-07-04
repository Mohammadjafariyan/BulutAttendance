package com.bulut.attendance.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.bulut.attendance.domain.AccountTemplate;
import com.bulut.attendance.repository.AccountTemplateRepository;
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
 * Spring Data Elasticsearch repository for the {@link AccountTemplate} entity.
 */
public interface AccountTemplateSearchRepository
    extends ElasticsearchRepository<AccountTemplate, UUID>, AccountTemplateSearchRepositoryInternal {}

interface AccountTemplateSearchRepositoryInternal {
    Page<AccountTemplate> search(String query, Pageable pageable);

    Page<AccountTemplate> search(Query query);

    @Async
    void index(AccountTemplate entity);

    @Async
    void deleteFromIndexById(UUID id);
}

class AccountTemplateSearchRepositoryInternalImpl implements AccountTemplateSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AccountTemplateRepository repository;

    AccountTemplateSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AccountTemplateRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AccountTemplate> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AccountTemplate> search(Query query) {
        SearchHits<AccountTemplate> searchHits = elasticsearchTemplate.search(query, AccountTemplate.class);
        List<AccountTemplate> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AccountTemplate entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(UUID id) {
        elasticsearchTemplate.delete(String.valueOf(id), AccountTemplate.class);
    }
}
