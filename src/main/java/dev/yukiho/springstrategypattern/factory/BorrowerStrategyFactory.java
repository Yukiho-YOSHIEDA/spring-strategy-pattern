package dev.yukiho.springstrategypattern.factory;

import dev.yukiho.springstrategypattern.enums.PositionName;
import dev.yukiho.springstrategypattern.strategy.BorrowerStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The factory for BorrowerStrategy
 */
@Component
public class BorrowerStrategyFactory {

    private Map<PositionName, BorrowerStrategy> borrowerStrategyMap;

    @Autowired
    public BorrowerStrategyFactory(Set<BorrowerStrategy> borrowerStrategySet) {
        createStrategy(borrowerStrategySet);
    }

    public BorrowerStrategy findStrategy(PositionName positionName) {
        return borrowerStrategyMap.get(positionName);
    }

    private void createStrategy(Set<BorrowerStrategy> borrowerStrategySet) {
        borrowerStrategyMap = borrowerStrategySet.stream()
                .collect(Collectors.toMap(BorrowerStrategy::getPositionName, borrowerStrategy -> borrowerStrategy));
    }
}
