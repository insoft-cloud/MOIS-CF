package org.openpaas.paasta.portal.common.api.repository.cc;

import org.openpaas.paasta.portal.common.api.entity.cc.EventCc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCcRepository  extends JpaRepository<EventCc, Integer> {
    EventCc findFirstByActeeAndType(String actee, String type);

}
