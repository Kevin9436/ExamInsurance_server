package servlet;

import java.io.BufferedReader;
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

import bean.AddCourseReq;
import bean.BasicResponse;
import model.Teaching;
import utils.JDBCUtils;

/**
 * Servlet implementation class AddCourse_servlet
 */
@WebServlet("/user/teacher/addCourse")
public class AddCourse_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCourse_servlet() {
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
		AddCourseReq req=gson.fromJson(reqJson,AddCourseReq.class);
		
		BasicResponse<Teaching> res=new BasicResponse<Teaching>();
		String check=String.format("SELECT * FROM course WHERE id='%s' AND status=0", req.getCourse_id());
		String newTeach=String.format("INSERT INTO teaching(teacher_id,course_id,course_title,status) VALUES ('%s','%s','%s',0)",
				req.getTeacher_id(),req.getCourse_id(),req.getCourse_title());
		try {
			ResultSet result=JDBCUtils.query(check);
			if(result.next()) {
				//课程已经在数据库中存在，只给老师添加课程即可
				JDBCUtils.update(newTeach);
				Teaching course=new Teaching();
				course.setCourse_id(result.getString("id"));
				course.setCourse_title(result.getString("title"));
				course.setStatus(0);
				res.setResponse(0, "添加成功", course);
			}
			else {
				//课程为新课程，需要更新course表
				String getGrade=String.format("SELECT * FROM grade WHERE course_id='%s'", req.getCourse_id());
				ResultSet gradeset=JDBCUtils.query(getGrade);
				List<Integer> score=new ArrayList<Integer>();
				int sum=0;
				while(gradeset.next()) {
					int grade=gradeset.getInt("score");
					score.add(grade);
					sum+=grade;
				}
				int upper;
				int lower;
				if(score.size()==0) {
					upper=95;
					lower=65;
				}
				else {
					//计算平均值和方差
					double average=sum/score.size();
					double ns=0;
					for(int i=0;i<score.size();i++) {
						ns+= (average-score.get(i))*(average-score.get(i));
					}
					double deviation=Math.sqrt(ns/score.size());
					if(average+1.65*deviation>95)
						upper=(int) (average+1.65*deviation);
					else
						upper=95;
					if(average-1.65*deviation>65)
						lower=(int) (average-1.65*deviation);
					else
						lower=65;
				}
				String newCourse=String.format("INSERT INTO course(id,title,status,upper,lower) VALUES('%s','%s',0,%d,%d)",
						req.getCourse_id(),req.getCourse_title(),upper,lower);
				try {
					JDBCUtils.update(newCourse);
					JDBCUtils.update(newTeach);
					Teaching teach=new Teaching();
					teach.setCourse_id(req.getCourse_id());
					teach.setCourse_title(req.getCourse_title());
					teach.setStatus(0);
					res.setResponse(0, "添加成功", teach);
				}catch(SQLException e) {
					res.setResponse(3, "数据库插入错误", null);
					e.printStackTrace();
				}
			}
		}catch(SQLException e) {
			res.setResponse(2, "数据库查询错误", null);
            e.printStackTrace();
		}
		
		//返回response
		PrintWriter writer = response.getWriter();  
		writer.write(new Gson().toJson(res, BasicResponse.class));  
		writer.flush(); 
	}

}
