package com.qwyck.compcalendar.calendar;

import java.util.ArrayList;
import java.util.Date;

public class Competition {

  private long id;
  private ArrayList<Date> dates;
  private String location;
  private String name;
  
  /**
   * Create a new {@link Competition} instance.
   * 
   * @param id
   * @param dates
   * @param location
   * @param names
   * @return the new competition object
   */
  public Competition(final long id, final ArrayList<Date> dates, final String location,
      final String name) {
        super();
    this.id = id;
    this.dates = dates;
    this.location = location;
    this.name = name;
  }

  /**
   * @return the id
   */
  public long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the dates
   */
  public ArrayList<Date> getDates() {
    return dates;
  }

  /**
   * @param dates the dates to set
   */
  public void setDates(ArrayList<Date> dates) {
    this.dates = dates;
  }

  /**
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * @param location the location to set
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
}
