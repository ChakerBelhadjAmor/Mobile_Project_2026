package com.supervision.livraison.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic (groupId, groupLabel, etat, count) row used by the dashboard endpoints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatDto {
    private Long   groupId;
    private String groupLabel;
    private String etat;
    private Long   count;
}
