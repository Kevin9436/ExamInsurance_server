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
import model.Teacher;
import model.Teaching;
import utils.JDBCUtils;

/**
 * Servlet implementation class TeacherLogin_servlet
 */
@WebServlet("/user/teacher/login")
public class TeacherLogin_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeacherLogin_servlet() {
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
		System.out.println("teacher login request:"+id+" "+pw);
		String login = String.format("SELECT * FROM teacher WHERE id='%s'", id);
		BasicResponse<Teacher> res=new BasicResponse<Teacher>();
		try {
			ResultSet result = JDBCUtils.query(login);
			if(result.next()) {
				if(pw.equals(result.getString("pw"))) {
					Teacher teacher=new Teacher();
					teacher.setId(result.getString("id"));
					teacher.setPassword(result.getString("pw"));
					teacher.setUsername(result.getString("username"));
					teacher.setPhone(result.getString("phone"));
					System.out.println(teacher.getId()+" "+teacher.getPassword()+" "+teacher.getUsername()+" "+teacher.getPhone());
					String getCourse=String.format("SELECT * FROM teaching WHERE teacher_id='%s'", id);
					ResultSet courseset = JDBCUtils.query(getCourse);
					List<Teaching> teach = new ArrayList<Teaching>();
					while(courseset.next()) {
						Teaching course = new Teaching();
						course.setCourse_id(courseset.getString("course_id"));
						course.setCourse_title(courseset.getString("course_title"));
						course.setStatus(courseset.getInt("status"));
						teach.add(course);
					}
					teacher.setTeachingList(teach);
					res.setResponse(0, "登录成功", teacher);
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
