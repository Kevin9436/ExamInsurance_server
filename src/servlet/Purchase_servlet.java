package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.BasicResponse;
import bean.PurchaseReq;
import model.Order;
import utils.JDBCUtils;

/**
 * Servlet implementation class Purchase_servlet
 */
@WebServlet("/user/student/purchase")
public class Purchase_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Purchase_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("不支持GET方法");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");  
	    response.setContentType("text/html;charset=utf-8"); 
	    
	    BufferedReader read = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while((line = read.readLine())!=null) {
			sb.append(line);
		}
		String reqJson = sb.toString();
		System.out.println(reqJson);
		Gson gson=new Gson();
		PurchaseReq req=gson.fromJson(reqJson,PurchaseReq.class);
		Order order=req.getOrder();
		
		BasicResponse<Order> res=new BasicResponse<Order>();
		try {
			String getStudent=String.format("SELECT * FROM student WHERE id='%s'", req.getStudentId());
			ResultSet result=JDBCUtils.query(getStudent);
			if(result.next()) {
				//这里默认购买单价为1
				if(result.getInt("account")<1) {
					res.setResponse(6, "余额不足",null);
				}
				else {
					String checkOrder=String.format("SELECT * FROM `order` WHERE student_id='%s' AND course_id='%s' AND status=0", 
							req.getStudentId(),order.getId());
					ResultSet getOrder=JDBCUtils.query(checkOrder);
					if(getOrder.next()) {
						res.setResponse(7, "课程保险已购买", null);
					}
					else {
						order.setScore(0);
						String purchase=String.format("INSERT INTO `order`(student_id,course_id,course_title,type,status,threshold) "
								+ "VALUES('%s','%s','%s',%d,%d,%d)"
								,req.getStudentId(),order.getId(),order.getTitle(),order.getType(),order.getStatus(),order.getThreshold());
						try {
							JDBCUtils.update(purchase);
							res.setResponse(0, "购买成功", order);
						}catch(SQLException e) {
							res.setResponse(3, "保险已购买过", null);
						}
					}
				}
			}
			else {
				res.setResponse(5,"id不存在",null);
			}
		}catch (SQLException e) {
            res.setResponse(2, "数据库查询错误", null);
            e.printStackTrace();
        }
		
		//返回response
		PrintWriter writer = response.getWriter();  
        writer.write(new Gson().toJson(res, BasicResponse.class));  
        writer.flush(); 
	}

}
