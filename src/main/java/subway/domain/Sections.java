package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sort(sections);
    }

    // TODO: 정렬 로직 리팩터링
    private static List<Section> sort(final List<Section> sections) {
        if (sections.size() == 0) {
            return sections;
        }

        final List<Section> sortedSections = new LinkedList<>();
        sortedSections.add(sections.get(0));
        while (sortedSections.size() < sections.size()) {
            final Station head = sortedSections.get(0).getBeforeStation();
            final Station tail = sortedSections.get(sortedSections.size() - 1).getNextStation();
            for (final Section section : sections) {
                if (section.getNextStation().equals(head)) {
                    sortedSections.add(0, section);
                    break;
                } else if (section.getBeforeStation().equals(tail)) {
                    sortedSections.add(section);
                    break;
                }
            }
        }

        return sortedSections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isHeadStation(final Station station) {
        return sections.isEmpty() || sections.get(0).getBeforeStation().equals(station);
    }

    public boolean isTailStation(final Station station) {
        return sections.get(sections.size() - 1).getNextStation().equals(station);
    }

    public void validateDuplicate(final Section newSection) {
        if (newSection.getBeforeStation().equals(newSection.getNextStation())) {
            throw new IllegalArgumentException("이전 역과 다음 역은 동일할 수 없습니다.");
        }
        if (isExist(newSection.getBeforeStation()) && isExist(newSection.getNextStation())) {
            throw new IllegalArgumentException("이미 존재하는 역입니다.");
        }
    }

    private boolean isExist(final Station station) {
        return (sections.size() > 0 && sections.get(0).getBeforeStation().equals(station)) ||
                sections.stream()
                        .anyMatch(section -> section.getNextStation().equals(station));
    }

    public Sections addHead(final Section section) {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.add(0, section);
        return new Sections(newSections);
    }

    public Sections addTail(final Section section) {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.add(sections.size(), section);
        return new Sections(newSections);
    }

    public Sections addCentral(final Section section) {
        final List<Section> newSections = new LinkedList<>(sections);

        final Section originSection = findOriginSection(section, newSections);

        final int originIndex = newSections.indexOf(originSection);
        newSections.remove(originSection);
        newSections.add(originIndex, section);
        newSections.add(originIndex + 1,
                new Section(
                        section.getNextStation(),
                        originSection.getNextStation(),
                        originSection.getDistance().minusValue(section.getDistance())
                )
        );

        return new Sections(newSections);
    }

    private Section findOriginSection(final Section section, final List<Section> newSections) {
        return newSections.stream()
                .filter(element -> element.getBeforeStation().equals(section.getBeforeStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이전 역을 찾을 수 없습니다."));
    }

    public Sections removeHead() {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.remove(0);
        return new Sections(newSections);
    }

    public Sections removeTail() {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.remove(sections.size() - 1);
        return new Sections(newSections);
    }

    public Sections removeCentral(final Station station) {
        final List<Section> newSections = new LinkedList<>(sections);

        final Section beforeSection = findBeforeSection(station, newSections);
        final Section nextSection = findNextSection(station, newSections);

        final int index = newSections.indexOf(beforeSection);
        newSections.remove(beforeSection);
        newSections.remove(nextSection);
        final Section newSection = new Section(
                beforeSection.getBeforeStation(),
                nextSection.getNextStation(),
                beforeSection.getDistance().plusValue(nextSection.getDistance())
        );
        newSections.add(index, newSection);
        return new Sections(newSections);
    }

    private static Section findBeforeSection(final Station station, final List<Section> newSections) {
        return newSections.stream()
                .filter(section -> section.getNextStation().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    private static Section findNextSection(final Station station, final List<Section> newSections) {
        return newSections.stream()
                .filter(section -> section.getBeforeStation().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    public Sections getDifferenceOfSet(final Sections otherSections) {
        final List<Section> result = new LinkedList<>(sections);
        result.removeAll(otherSections.getSections());
        return new Sections(result);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
