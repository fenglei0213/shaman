package org.shaman.example.vo;

import org.shaman.dao.annotation.FieldMeta;

/**
 * Created by fenglei on 2016/6/11.
 */
public class UserInfo {

    public static final String ID = "id";
    public static final String USER_NAME = "user_name";
    public static final String PASSWD = "passwd";

    public UserInfo() {

    }

    public UserInfo(String userName, String passwd) {
        this.setUserName(userName);
        this.setPasswd(passwd);
    }

    @FieldMeta(id = true)
    private Long id;
    @FieldMeta
    private String userName;
    @FieldMeta
    private String passwd;
    @FieldMeta
    private String nickName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return new StringBuffer("id: ").append(id)
                .append("\n userName: ").append(userName)
                .append("\n passwd: ").append(passwd)
                .toString();
    }
}
