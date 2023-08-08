package com.schoofi.utils;

/**
 * Created by Schoofi on 30-01-2017.
 */

public class DiarySubVO {

    String initials;
    String title;
    String description;
    String rating;
    String emoticon;
    String subject;
    String profileUrl;
    String fileUrl;
    String time;
    String diarySubId;
    String diaryModuleId;
    String attachment;
    String roleId;
    String ratingParameter;
    String type;

    public String getRatingParameter() {
        return ratingParameter;
    }

    public void setRatingParameter(String ratingParameter) {
        this.ratingParameter = ratingParameter;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getDiarySubId() {
        return diarySubId;
    }

    public void setDiarySubId(String diarySubId) {
        this.diarySubId = diarySubId;
    }

    public String getDiaryModuleId() {
        return diaryModuleId;
    }

    public void setDiaryModuleId(String diaryModuleId) {
        this.diaryModuleId = diaryModuleId;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getEmoticon() {
        return emoticon;
    }

    public void setEmoticon(String emoticon) {
        this.emoticon = emoticon;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShareable() {
        return shareable;
    }

    public void setShareable(String shareable) {
        this.shareable = shareable;
    }

    String shareable;
}
