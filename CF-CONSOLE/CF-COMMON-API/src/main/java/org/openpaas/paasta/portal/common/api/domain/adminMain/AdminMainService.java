package org.openpaas.paasta.portal.common.api.domain.adminMain;


import org.jinq.orm.stream.JinqStream;
import org.openpaas.paasta.portal.common.api.config.JinqSource;
import org.openpaas.paasta.portal.common.api.config.dataSource.CcConfig;
import org.openpaas.paasta.portal.common.api.entity.cc.*;
import org.openpaas.paasta.portal.common.api.repository.cc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by indra on 2018-02-12.
 */
@Service
public class AdminMainService {

    @Autowired
    CcConfig ccConfig;

    @Autowired
    SpacesCcRepository spacesCcRepository;

    @Autowired
    OrganizationUsersRepository organizationUsersRepository;

    @Autowired
    OrgCcRepository orgCcRepository;

    @Autowired
    UsersCcRepository usersCcRepository;

    @Autowired
    OrgCccRepository orgCccRepository;

    @Autowired
    JinqSource jinqSource;

    public Map<String, Object>getTotalCountList() {
        JinqStream<OrganizationsCc> streams = jinqSource.streamAllCc(OrganizationsCc.class);
        List<Map<String, Object>> resultList = streams.map(x -> new HashMap<String, Object>(){{
            put("organizationCount", x.getOrganizationCount());
            put("spaceCount", x.getSpaceCount());
            put("applicationCount", x.getApplicationCount());
            put("userCount", x.getUserCount());
        }}).collect(Collectors.toList());

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }

    public Map<String, Object>getTotalCountList(String guid) {
        UsersCc cc = usersCcRepository.findByGuid(guid);
        List<OrganizationUsers> users = organizationUsersRepository.findAllByUserId(cc.getId());
        List<OrganizationsTolCc> organizationsCcs = new ArrayList<>();
        users.forEach(res -> organizationsCcs.add(orgCcRepository.findOne(res.getOrganizationId())));
        AtomicInteger organizationCount = new AtomicInteger();
        AtomicInteger spaceCount = new AtomicInteger();
        AtomicInteger applicationCount = new AtomicInteger();
        AtomicInteger userCount = new AtomicInteger();
        organizationsCcs.forEach(res -> {
            organizationCount.set(organizationCount.get()+1);
            spaceCount.set(spaceCount.get()+res.getSpaceCount());
            applicationCount.set(applicationCount.get()+res.getApplicationCount());
            userCount.set(userCount.get()+res.getUserCount());
        });

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("organizationCount", organizationCount.get());
        resultMap.put("spaceCount", spaceCount.get());
        resultMap.put("applicationCount", applicationCount.get());
        resultMap.put("userCount", userCount.get());
        resultList.add(resultMap);
        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }



    public Map<String, Object>getTotalOrganizationList() {  //Map<String, Object> reqParam
        JinqStream<OrganizationsTolCc> streams = jinqSource.streamAllCc(OrganizationsTolCc.class);
        streams = streams.sortedBy(c -> c.getId());
        List<Map<String, Object>> resultList = streams.map(x -> new HashMap<String, Object>(){{
            put("organizationId", x.getId());
            put("organizationName", x.getName());
            put("spaceCount", x.getSpaceCount());
            put("applicationCount", x.getApplicationCount());
            put("userCount", x.getUserCount());
        }}).collect(Collectors.toList());

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }

    public Map<String, Object>getTotalOrganizationList(String guid) {  //Map<String, Object> reqParam

        UsersCc cc = usersCcRepository.findByGuid(guid);
        List<OrganizationUsers> users = organizationUsersRepository.findAllByUserId(cc.getId());
        List<OrganizationsTolCc> organizationsCcs = new ArrayList<>();
        users.forEach(res -> organizationsCcs.add(orgCcRepository.findOne(res.getOrganizationId())));
        List<Map<String, Object>> resultList = organizationsCcs.stream().map(x -> new HashMap<String, Object>(){{
            put("organizationId", x.getId());
            put("organizationName", x.getName());
            put("spaceCount", x.getSpaceCount());
            put("applicationCount", x.getApplicationCount());
            put("userCount", x.getUserCount());
        }}).collect(Collectors.toList());

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }

    public Map<String, Object>getTotalSpaceList(String organizationId) {
        EntityManager ccEm = ccConfig.ccEntityManager().getNativeEntityManagerFactory().createEntityManager();
        Query q = null;
        if(null != organizationId && !("").equals(organizationId)) {
            q = ccEm.createNamedQuery("SpacesCc.allByOrganizationIdList");
            q.setParameter(1, Integer.parseInt(organizationId));
            //q.setParameter(2, Integer.parseInt(organizationId));
        } else {
            q = ccEm.createNamedQuery("SpacesCc.allList");
        }

        List<SpacesCc> results = q.getResultList();

        List<Map<String, Object>> resultList = results.stream().map(x -> new HashMap<String, Object>(){{
            put("spaceId", x.getId());
            put("applicationCount", x.getApplicationCount());
            put("spaceName", x.getName());
        }}).collect(Collectors.toList());

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }



}
