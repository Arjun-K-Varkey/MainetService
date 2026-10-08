/**
 *
 */
package com.abm.mainet.common.master.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.abm.mainet.common.integration.dms.domain.CFCAttachment;

/**
 * @author umashanker.kanaujiya
 *
 */
@Repository
public interface TbScrutinyDocJpaRepository extends CrudRepository<CFCAttachment, Long> {

}
