/*
 * —ß…˙¿‡
 */
package model;

import java.util.List;

public class Student {
	private String id;
	private String password;
	private String username;
	private String phone;
	private int account;
	private List<Order> history;
	
	public String getId() {return id;}
	public void setId(String _id) {id=_id;}
	public String getPw() {return password;}
	public void setPw(String _pw) {password=_pw;}
	public String getUsername() {return username;}
	public void setUsername(String _username) {username=_username;}
	public String getPhone() {return phone;}
	public void setPhone(String _phone) {phone=_phone;}
	public int getAccount() {return account;}
	public void setAccount(int _account) {account=_account;}
	public List<Order> getHistory(){return history;}
	public void setHistory(List<Order>_history) {history=_history;}
	
}
