package com.sintaks.mushandi.model.projections;

import java.time.LocalDate;

public interface BatchProjection {
    LocalDate getCycleDate();
    String getInstitution();
    Integer getMale();
    Integer getFemale();
}
