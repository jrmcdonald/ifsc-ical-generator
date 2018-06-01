package com.qwyck.compcalendar.calendar;

import java.util.ArrayList;

/**
 * Calendar
 */
public class Calendar {

  private ArrayList<Competition> competitions;

  /**
   * Default constructor
   */
  public Calendar() {
    super();
  }

  /**
   * Construct a {@link Calendar} with an array list of {@link Competition} objects
   * 
   * @param competitions
   * @return the calendar object
   */
  public Calendar(final ArrayList<Competition> competitions) {
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
}
