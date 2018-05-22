package bean;

public class BasicResponse<T> {
	public int errno;
	public String msg;
	public T data;
	public void setResponse(int _errno,String _msg,T _data) {
		errno=_errno;
		msg=_msg;
		data=_data;
	}
}
