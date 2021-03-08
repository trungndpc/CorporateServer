package vn.com.insee.corporate.repository.custom;

import vn.com.insee.corporate.repository.custom.metrics.ConstructionLocationMetric;

import java.util.List;

public interface ConstructionRepositoryCustom {
    List<ConstructionLocationMetric> statisticConstructionByLocation();
}
