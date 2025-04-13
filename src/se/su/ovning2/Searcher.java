package se.su.ovning2;

import java.util.Collection;
import java.util.SortedSet;
import java.util.*;
import java.util.stream.Collectors;

public class Searcher implements se.su.ovning2.SearchOperations {

  private final Map<String, Recording> recordingsByTitle = new HashMap<>();
  private final Map<String, Set<Recording>> recordingsByArtist = new HashMap<>();
  private final Map<String, Set<Recording>> recordingsByGenre = new HashMap<>();
  private final Map<Integer, Set<Recording>> recordingsByYear = new HashMap<>();

  public Searcher(Collection<Recording> data) {
    for (Recording rec : data) {
      recordingsByTitle.putIfAbsent(rec.getTitle(), rec);

      recordingsByArtist
              .computeIfAbsent(rec.getArtist(), k -> new HashSet<>())
              .add(rec);

      for (String genre : rec.getGenre()) {
        recordingsByGenre
                .computeIfAbsent(genre, k -> new HashSet<>())
                .add(rec);
      }

      recordingsByYear
              .computeIfAbsent(rec.getYear(), k -> new HashSet<>())
              .add(rec);
    }
  }

  @Override
  public long numberOfArtists() {
    return recordingsByArtist.keySet().size();
  }

  @Override
  public long numberOfGenres() {
    return recordingsByGenre.keySet().size();
  }

  @Override
  public long numberOfTitles() {
    return recordingsByTitle.keySet().size();
  }

  @Override
  public boolean doesArtistExist(String name) {
    return recordingsByArtist.containsKey(name);
  }

  @Override
  public Collection<String> getGenres() {
    return Collections.unmodifiableSet(recordingsByGenre.keySet());
  }

  @Override
  public Recording getRecordingByName(String title) {
    return recordingsByTitle.getOrDefault(title, null);
  }

  @Override
  public Collection<Recording> getRecordingsAfter(int year) {
    return recordingsByYear.entrySet().stream()
            .filter(e -> e.getKey() >= year)
            .flatMap(e -> e.getValue().stream())
            .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public SortedSet<Recording> getRecordingsByArtistOrderedByYearAsc(String artist) {
    SortedSet<Recording> sorted = new TreeSet<>(Comparator.comparingInt(Recording::getYear));
    sorted.addAll(recordingsByArtist.getOrDefault(artist, Set.of()));
    return Collections.unmodifiableSortedSet(sorted);
  }

  @Override
  public Collection<Recording> getRecordingsByGenre(String genre) {
    return Collections.unmodifiableCollection(recordingsByGenre.getOrDefault(genre, Set.of()));
  }

  @Override
  public Collection<Recording> getRecordingsByGenreAndYear(String genre, int yearFrom, int yearTo) {
    return recordingsByGenre.getOrDefault(genre, Set.of()).stream()
            .filter(r -> r.getYear() >= yearFrom && r.getYear() <= yearTo)
            .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Collection<Recording> offerHasNewRecordings(Collection<Recording> offered) {
    Set<String> existing = recordingsByTitle.keySet();
    return offered.stream()
            .filter(r -> !existing.contains(r.getTitle()))
            .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public Collection<Recording> optionalGetRecordingsBefore(int year) {
    return recordingsByYear.entrySet().stream()
            .filter(e -> e.getKey() < year)
            .flatMap(e -> e.getValue().stream())
            .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public SortedSet<Recording> optionalGetRecordingsByArtistOrderedByTitleAsc(String artist) {
    TreeSet<Recording> sorted = recordingsByArtist.getOrDefault(artist, Set.of()).stream()
            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Recording::getTitle))));
    return Collections.unmodifiableSortedSet(sorted);
  }

  @Override
  public Collection<Recording> optionalGetRecordingsFrom(int year) {
    return Collections.unmodifiableCollection(recordingsByYear.getOrDefault(year, Set.of()));
  }
}