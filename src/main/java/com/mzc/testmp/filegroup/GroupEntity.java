package com.mzc.testmp.filegroup;

/**
 * create by zhencai.ma on 2019/11/7
 */
public class GroupEntity {
    private String id;
    private String groupId;
    private Float quota;

    public GroupEntity(String line) {
//        System.out.println(line);
        String[] tmp = line.split("，");
        id = tmp[0].trim();
        groupId = tmp[1].trim();
        quota = Float.parseFloat(tmp[2].trim());
    }

    @Override
    public String toString() {
        return "GroupEntity{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", quota=" + quota +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Float getQuota() {
        return quota;
    }

    public void setQuota(Float quota) {
        this.quota = quota;
    }
}
