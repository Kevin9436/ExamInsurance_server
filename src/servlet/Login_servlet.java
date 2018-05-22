package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.BasicResponse;
import model.Order;
import model.Student;
import utils.JDBCUtils;

/**
 * Servlet implementation class Login_servlet
 */
@WebServlet("/student/login")
public class Login_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");  
	    response.setContentType("text/html;charset=utf-8"); 
	    
		String id=request.getParameter("id");
		String pw=request.getParameter("pw");
		System.out.println("login request:"+id+" "+pw);
		String login = String.format("SELECT * FROM student WHERE id='%s'", id);
		BasicResponse<Student> res=new BasicResponse<Student>();
		try {
			ResultSet result = JDBCUtils.query(login);
			if(result.next()) {
				if(pw.equals(result.getString("pw"))) {
					Student student=new Student();
					student.setId(result.getString("id"));
					student.setPw(result.getString("pw"));
					student.setUsername(result.getString("username"));
					student.setPhone(result.getString("phone"));
					student.setAccount(result.getInt("account"));
					System.out.println(student.getId()+" "+student.getPw()+" "+student.getUsername()+" "+student.getPhone()+" "+student.getAccount());
					String getorder=String.format("SELECT * FROM `order` WHERE student_id='%s'", id);
					ResultSet orderset = JDBCUtils.query(getorder);
					List<Order> history = new ArrayList<Order>();
					while(orderset.next()) {
						Order order=new Order();
						order.setId(orderset.getString("course_id"));
						order.setTitle(orderset.getString("course_title"));
						order.setType(orderset.getInt("type"));
						order.setStatus(orderset.getInt("status"));
						order.setThreshold(orderset.getInt("threshold"));
						String getgrade=String.format("SELECT * FROM grade WHERE course_id='%s' AND student_id='%s'",
								orderset.getString("course_id"),id);
						ResultSet course = JDBCUtils.query(getgrade);
						if(course.next()) {
							order.setScore(course.getInt("score"));
						}
						else {
							order.setScore(0);
						}
						history.add(order);
					}
					student.setHistory(history);
					res.setResponse(0, "登录成功", student);
				}
				else {
					res.setResponse(4,"密码错误",null);
				}
			}
			else {
				res.setResponse(5, "id不存在", null);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("不支持POST方法");
	}

}
