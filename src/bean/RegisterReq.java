package bean;

public class RegisterReq {
	private int identity;
    private String id;
    private String username;
    private String pw;
    private String phone;

    public void setIdentity(int _identity){this.identity=_identity;}
    public int getIdentity(){return this.identity;}
    public void setId(String _id){this.id=_id;}
    public String getId(){return this.id;}
    public void setUsername(String _nickname){this.username=_nickname;}
    public String getUsername(){return this.username;}
    public void setPw(String _pw){this.pw=_pw;}
    public String getPw(){return this.pw;}
    public void setPhone(String _phone){this.phone=_phone;}
    public String getPhone(){return this.phone;}
}
