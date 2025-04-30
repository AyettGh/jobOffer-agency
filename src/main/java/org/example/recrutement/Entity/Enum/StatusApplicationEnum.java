package org.example.recrutement.Entity.Enum;

import java.util.Map;
import java.util.Set;

public enum StatusApplicationEnum {
    SUBMITTED,REVIEWED,SHORTLISTED,REJECTED;

    private static final Map<StatusApplicationEnum, Set<StatusApplicationEnum>> VALID_TRANSITIONS = Map.of(
            SUBMITTED, Set.of(REVIEWED, REJECTED),
            REVIEWED, Set.of(SHORTLISTED, REJECTED),
            SHORTLISTED, Set.of(REJECTED)
    );

    public boolean isValidTransition(StatusApplicationEnum newStatus) {
        return VALID_TRANSITIONS.getOrDefault(this, Set.of()).contains(newStatus);
    }
}

