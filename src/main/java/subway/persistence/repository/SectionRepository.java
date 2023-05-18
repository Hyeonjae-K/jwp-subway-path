package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public List<Section> findAll() {
        final List<SectionEntity> sectionEntities = sectionDao.findAll();
        final Map<Long, Station> stationMap = getStationMap(sectionEntities);
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                                sectionEntity.getId(),
                                stationMap.get(sectionEntity.getBeforeStation()),
                                stationMap.get(sectionEntity.getNextStation()),
                                new Distance(sectionEntity.getDistance())
                        )
                ).collect(Collectors.toList());
    }

    private Map<Long, Station> getStationMap(final List<SectionEntity> sectionEntities) {
        final List<Long> stationIds = sectionEntities.stream()
                .flatMap(sectionEntity -> Stream.of(sectionEntity.getBeforeStation(), sectionEntity.getNextStation()))
                .distinct().collect(Collectors.toList());
        return stationDao.findAllById(stationIds).stream()
                .collect(Collectors.toMap(StationEntity::getId, StationEntity::mapToStation));
    }
}