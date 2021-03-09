package org.openpaas.paasta.portal.common.api.repository.cc;

import org.openpaas.paasta.portal.common.api.entity.cc.OrganizationUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationUsersRepository extends JpaRepository<OrganizationUsers, Integer> {
    List<OrganizationUsers> findAllByUserId(int user_id);
}
