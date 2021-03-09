package org.openpaas.paasta.portal.api.model;

import lombok.Data;

@Data
public class MoisUser {
    private String userName;
    private String projectId;
    private String Token;
    private String clientId;
    private String clientSecret;

}
