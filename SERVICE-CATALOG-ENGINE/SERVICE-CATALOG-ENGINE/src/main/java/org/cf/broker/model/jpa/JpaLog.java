package org.cf.broker.model.jpa;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JpaLog {

    @JsonSerialize
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @JsonSerialize
    @Column(name = "logs", columnDefinition = "TEXT")
    @JsonProperty("logs")
    private String logs;

    @JsonSerialize
    @Column(name = "type")
    @JsonProperty("type")
    private String type;
}
