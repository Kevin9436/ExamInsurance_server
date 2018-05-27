package bean;

public class ChangeInfoReq {
	private int identity;
	private String id;
    private String changeItem;
    
    public int getIdentity(){return identity;}
    public void setIdentity(int _identity){identity=_identity;}
    public String getId(){return id;}
    public void setId(String _id){id=_id;}
    public String getChangeItem(){return changeItem;}
    public void setChangeItem(String item){changeItem=item;}
}
