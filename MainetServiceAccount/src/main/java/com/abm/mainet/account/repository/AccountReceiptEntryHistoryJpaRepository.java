
package com.abm.mainet.account.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.abm.mainet.account.domain.AccountPaymentMasterEntityHist;

/**
 * @author dharmendra.chouhan
 *
 */
public interface AccountReceiptEntryHistoryJpaRepository extends PagingAndSortingRepository<AccountPaymentMasterEntityHist, Long> {}
