/*
 * 对客户端学生购买保险请求的封装
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
