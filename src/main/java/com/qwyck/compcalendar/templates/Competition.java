package com.qwyck.compcalendar.templates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Competition {

    @JsonProperty("cat_id")
    private long cat;

    @JsonProperty("date")
    private Date startDate;

    @JsonProperty("date_end")
    private Date endDate;

    private String name;
    private String homepage;
    private long duration;

    public Competition() {}

    /**
     * @return the cat
     */
    public long getCat() {
        return cat;
    }

    /**
     * @param cat the cat to set
     */
    public void setCat(long cat) {
        this.cat = cat;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    /**
     * @return the homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage the homepage to set
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return the duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format(
                "Competition{ cat=%d, name='%s', startDate=%tD, endDate=%tD, homepage=%s }", cat,
                name, startDate, endDate, homepage);
    }
}
