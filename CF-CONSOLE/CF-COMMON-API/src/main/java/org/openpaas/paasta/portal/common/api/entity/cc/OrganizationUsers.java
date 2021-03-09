package org.openpaas.paasta.portal.common.api.entity.cc;


import javax.persistence.*;

@Entity
@Table(name = "organizations_users")
public class OrganizationUsers {

    @Column(name = "organization_id", nullable = false)
    private int organizationId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organizations_users_pk", nullable = false)
    private int organizationsUsersPk;

    public int getOrganizationId() {
        return organizationId;
    }

    public int getUserId() {
        return userId;
    }

    public int getOrganizationsUsersPk() {
        return organizationsUsersPk;
    }
}
