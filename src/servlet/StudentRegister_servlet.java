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
import bean.RegisterReq;
import model.Student;
import utils.JDBCUtils;

/**
 * Servlet implementation class Register_servlet
 */
@WebServlet("/user/student/register")
public class StudentRegister_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentRegister_servlet() {
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
		RegisterReq req=gson.fromJson(reqJson,RegisterReq.class);
		String check = String.format("SELECT * FROM student WHERE id='%s'", req.getId());
		//数据库操作
		BasicResponse<Student> res=new BasicResponse<Student>();
		try {
            ResultSet result = JDBCUtils.query(check); // 数据库查询操作
            if (result.next()) {
            	res.setResponse(1, "id已存在",null);
            } else {
            	String create = String.format("INSERT INTO student(id,pw,username,phone,account) VALUES('%s','%s','%s','%s',0)",
            			req.getId(),req.getPw(),req.getUsername(),req.getPhone());
            	try {
            		JDBCUtils.update(create);
            		Student student = new Student();
            		student.setId(req.getId());
            		student.setPw(req.getPw());
            		student.setUsername(req.getUsername());
            		student.setPhone(req.getPhone());
            		student.setAccount(0);
            		student.setHistory(null);
            		res.setResponse(0, "注册成功", student);
            	}catch(SQLException e) {
            		res.setResponse(3,"数据插入错误",null);
            	}
            }
        } catch (SQLException e) {
            res.setResponse(2, "数据库查询错误", null);
            e.printStackTrace();
        }
		
		//返回response
		PrintWriter writer = response.getWriter();  
        writer.write(new Gson().toJson(res, BasicResponse.class));  
        writer.flush(); 
	}

}
