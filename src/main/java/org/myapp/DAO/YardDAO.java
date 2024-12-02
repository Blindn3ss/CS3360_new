package org.myapp.DAO;

import org.myapp.Model.Yard;

import java.math.BigDecimal;
import java.util.List;

public interface YardDAO {
    boolean createYard(Yard yard);

    Yard getYardById(int yardId);

    boolean updateYard(Yard yard);

    boolean deleteYard(int yardId);

    List<Yard> getAllYards();

    List<Yard> getYardsWithFilter(Integer minCapacity, Integer maxCapacity, String location, String surfaceType, Double minPrice, Double maxPrice);
}
