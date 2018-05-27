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
import model.Teacher;
import utils.JDBCUtils;

/**
 * Servlet implementation class TeacherRegister_servlet
 */
@WebServlet("/user/teacher/register")
public class TeacherRegister_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeacherRegister_servlet() {
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
		String check = String.format("SELECT * FROM teacher WHERE id='%s'", req.getId());
		
		BasicResponse<Teacher> res=new BasicResponse<Teacher>();
		try {
            ResultSet result = JDBCUtils.query(check);
            if (result.next()) {
            	res.setResponse(1, "id已存在",null);
            } else {
            	String create = String.format("INSERT INTO teacher(id,pw,username,phone) VALUES('%s','%s','%s','%s')",
            			req.getId(),req.getPw(),req.getUsername(),req.getPhone());
            	try {
            		JDBCUtils.update(create);
            		Teacher teacher=new Teacher();
            		teacher.setId(req.getId());
            		teacher.setPassword(req.getPw());
            		teacher.setUsername(req.getUsername());
            		teacher.setPhone(req.getPhone());
            		teacher.setTeachingList(null);
            		res.setResponse(0, "注册成功", teacher);
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
