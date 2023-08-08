package com.schoofi.utils;

public class GroupTypeVO {

    String GroupName,groupId;

    public GroupTypeVO(String groupName, String groupId) {
        GroupName = groupName;
        this.groupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
