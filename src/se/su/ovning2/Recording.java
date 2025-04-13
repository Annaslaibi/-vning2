package se.su.ovning2;

import java.util.Objects;
import java.util.Set;

public class Recording implements Comparable<Recording> {
  private final int year;
  private final String artist;
  private final String title;
  private final String type;
  private final Set<String> genre;

  public Recording(String title, String artist, int year, String type, Set<String> genre) {
    this.title = title;
    this.artist = artist;
    this.year = year;
    this.type = type;
    this.genre = genre;
  }

  public String getArtist() { return artist; }
  public Set<String> getGenre() { return genre; }
  public String getTitle() { return title; }
  public String getType() { return type; }
  public int getYear() { return year; }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Recording)) return false;
    Recording that = (Recording) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public int compareTo(Recording other) {
    // Sortera på år först, sen titel (för TreeSet etc.)
    int cmp = Integer.compare(this.year, other.year);
    return cmp != 0 ? cmp : this.title.compareTo(other.title);
  }

  @Override
  public String toString() {
    return String.format("{ %s | %s | %s | %d | %s }", artist, title, genre, year, type);
  }
}