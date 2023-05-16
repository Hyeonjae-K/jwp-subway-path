package subway.persistence.entity;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public StationEntity(final String name) {
        this(null, name);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}