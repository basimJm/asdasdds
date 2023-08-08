package com.schoofi.utils;



        import java.util.Date;

        import io.github.memfis19.cadar.data.entity.Event;
        import io.github.memfis19.cadar.data.entity.property.EventProperties;

/**
 * Created by memfis on 11/23/16.
 */

public class EventModel implements Event {

    private Long id = (System.currentTimeMillis());
    private String title = "Event #" + String.valueOf(id);
    private String description = "";
    private Date originalStartDate = new Date();
    private Date startDate = (Date) originalStartDate.clone();
    private Date endDate = new Date();
    private String location = "lll";
    private String plannerId="kkk";
    private String className = "jjj";
    private Boolean allDay;
    private String isHoliday = "";
    private String sectionId = "";
    private String isAllDay1="";

    public EventModel() {

    }

    public EventModel(Event event) {
        this.id = event.getEventId();
        this.title = event.getEventTitle();
        this.description = event.getEventDescription();
        this.originalStartDate = event.getOriginalEventStartDate();
        this.startDate = event.getEventStartDate();
        this.endDate = event.getEventEndDate();
        this.location = event.getLocation();
    }

    public EventModel(String title,String location,Date startDate,Date endDate,Date originalStartDate,String plannerId,String className,String description,Boolean allDay,String isHoliday,String sectionId,String isAllDay1)
    {


        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.plannerId = plannerId;
        this.className = className;
        this.description = description;
        this.allDay = allDay;
        this.originalStartDate = originalStartDate;
        this.isHoliday = isHoliday;
        this.sectionId = sectionId;
        this.isAllDay1 = isAllDay1;

    }




    @Override
    public Long getEventId() {
        return id;
    }

    @Override
    public String getEventTitle() {
        return title;
    }

    @Override
    public String getEventDescription() {
        return description;
    }

    @Override
    public Date getOriginalEventStartDate() {
        return originalStartDate;
    }

    @Override
    public Date getEventStartDate() {
        return startDate;
    }

    @Override
    public Date getEventEndDate() {
        return endDate;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getSectionId() {
        return sectionId;
    }

    @Override
    public String getPlannerID() {
        return plannerId;
    }

    @Override
    public String isHoliday() {
        return isHoliday;
    }

    @Override
    public String isAllDay1() {
        return isAllDay1;
    }

    @Override
    public void setIsHoliday(String isHoliday) {

        this.isHoliday = isHoliday;

    }

    @Override
    public void setIsAllDay1(String isAllDay1) {

    }

    @Override
    public void setEventStartDate(Date startEventDate) {
        this.startDate = startEventDate;
    }

    @Override
    public void setEventEndDate(Date endEventDate) {
        this.endDate = endEventDate;
    }

    @Override
    public void setEventTitle(String eventTitle) {
        this.title = title;
    }

    @Override
    public void setEventDescription(String eventDescription) {

    }

    @Override
    public void setEventOriginalStartDate(String eventOriginalStartDate) {

    }

    @Override
    public void setPlannerID(String plannerID) {

    }

    @Override
    public void setSectionID(String sectionID) {

    }

    @Override
    public void setClassName(String className) {

    }

    @Override
    public void setCalendarId(Long calendarId) {

    }

    @Override
    public void setLocation(String location) {

    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public Long getCalendarId() {
        return 0l;
    }

    @Override
    public int getEventRepeatPeriod() {
        return EventProperties.EVERY_WEEK;
    }

    @Override
    public String getEventIconUrl() {
        return "";
    }

    @Override
    public Boolean isEditable() {
        return false;
    }

    @Override
    public Boolean isAllDayEvent() {
        return allDay;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getEventId() == 0) ? 0 : Long.valueOf(this.getEventId()).hashCode());
        result = prime * result + ((this.getEventStartDate() == null) ? 0 : this.getEventStartDate().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (((Object) this).getClass() != obj.getClass())
            return false;

        EventModel other = (EventModel) obj;

        if (this.getEventId() == 0) {
            if (other.getEventId() != 0)
                return false;
        } else if (getEventId() != other.getEventId())
            return false;

        if (this.getEventStartDate() == null) {
            if (other.getEventStartDate() != null)
                return false;
        } else if (!getEventStartDate().equals(other.getEventStartDate()))
            return false;

        return true;
    }
}
