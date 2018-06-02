package com.qwyck.compcalendar.templates;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Competitions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Competitions {

  private ArrayList<Competition> competitions;

  /**
   * Default constructor
   */
  public Competitions() {
    super();
  }

  /**
   * Construct a {@link Competitions} with an array list of {@link Competition} objects
   * 
   * @param competitions
   * @return the competitions object
   */
  public Competitions(final ArrayList<Competition> competitions) {
    super();
    this.competitions = competitions;
  }

  /**
   * @return the competitions
   */
  public ArrayList<Competition> getCompetitions() {
    if (competitions == null) {
      competitions = new ArrayList<Competition>();
    }
    return competitions;
  }

  /**
   * @param competitions the competitions to set
   */
  public void setCompetitions(ArrayList<Competition> competitions) {
    this.competitions = competitions;
  }

  @Override
  public String toString() {
    return "Competitions{competitions=" + competitions + "}";
  }
}
