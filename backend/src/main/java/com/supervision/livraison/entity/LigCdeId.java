package com.supervision.livraison.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Composite primary key for {@link LigCde} (nocde + refart).
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LigCdeId implements Serializable {

    @Column(name = "nocde")
    private Long nocde;

    @Column(name = "refart", length = 20)
    private String refart;
}
