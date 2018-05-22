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
import model.Course;
import utils.JDBCUtils;

/**
 * Servlet implementation class CourseList_servlet
 */
@WebServlet("/course/list")
public class CourseList_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseList_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");  
	    response.setContentType("text/html;charset=utf-8"); 
	    String getcourse="SELECT * FROM course WHERE status=0";
	    BasicResponse<List<Course>> res=new BasicResponse<List<Course>>();
	    List<Course> courseList=new ArrayList<Course>();
	    try {
	    	ResultSet result = JDBCUtils.query(getcourse);
	    	while(result.next()) {
	    		Course course=new Course();
	    		course.setId(result.getString("id"));
	    		course.setTitle(result.getString("title"));
	    		course.setUpper_threshold(result.getInt("upper"));
	    		course.setLower_threshold(result.getInt("lower"));
	    		System.out.println(course.getId()+" "+course.getTitle()+" "+course.getUpper_threshold()+" "+course.getLower_threshold());
	    		courseList.add(course);
	    	}
	    	res.setResponse(0, "查询成功", courseList);
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
