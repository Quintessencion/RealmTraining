package com.simbirsoft.igorverbkin.realmtraining;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Event extends RealmObject {

    @PrimaryKey
    private String id;
    private String eventName;
    private String start;
    private String end;
    private String fundName;
    private String address;
    private RealmList<String> phones;
    private String content;
    private String email;
    private String webSite;
    private RealmList<String> contributors;
    private String category;
    private RealmList<String> categoriesHelp;
    private RealmList<String> photos;
    private String descriptionAssistance;

    @Override
    public String toString() {
        return "\nid='" + id + '\'' +
                ",\neventName = '" + eventName + '\'' +
                ",\nstart = '" + start + '\'' +
                ",\nend = '" + end + '\'' +
                ",\nfundName = '" + fundName + '\'' +
                ",\naddress = '" + address + '\'' +
                ",\nphones = " + phones +
                ",\ncontent = '" + content + '\'' +
                ",\nemail = '" + email + '\'' +
                ",\nwebSite = '" + webSite + '\'' +
                ",\ncontributors = " + contributors +
                ",\ncategory = '" + category + '\'' +
                ",\ncategoriesHelp = " + categoriesHelp +
                ",\nphotos = " + photos +
                ",\ndescriptionAssistance = '" + descriptionAssistance + '\'';
    }
}
