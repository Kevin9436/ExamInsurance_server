/*
 * �Կͻ���ѧ������������ķ�װ
 */
package bean;

import model.Order;

public class PurchaseReq {
	private String studentId;
    private Order order;

    public String getStudentId(){return studentId;}
    public void setStudentId(String _studentId){studentId=_studentId;}
    public Order getOrder(){return order;}
    public void setOrder(Order _order){order=_order;}
}
