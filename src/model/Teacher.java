/*
 * ΩÃ ¶¿‡
 */
package model;

import java.util.List;

public class Teacher {
	private String id;
    private String password;
    private String username;
    private String phone;
    private List<Teaching> teachingList;

    public String getId(){return id;}
    public void setId(String _id){this.id=_id;}
    public String getPassword(){return password;}
    public void setPassword(String pw){this.password=pw;}
    public String getUsername(){return this.username;}
    public void setUsername(String _username){this.username=_username;}
    public String getPhone(){return phone;}
    public void setPhone(String phone_num){this.phone=phone_num;}
    public List<Teaching> getTeachingList(){return this.teachingList;}
    public void setTeachingList(List<Teaching> teach){this.teachingList=teach;}
}
