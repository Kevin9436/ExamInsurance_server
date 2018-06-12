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

import bean.ApplyReq;
import bean.ApplyRes;
import bean.BasicResponse;
import utils.JDBCUtils;

/**
 * Servlet implementation class Apply_servlet
 */
@WebServlet("/user/student/apply")
public class Apply_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Apply_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("��֧��GET����");
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
		ApplyReq req=gson.fromJson(reqJson,ApplyReq.class);
		
		BasicResponse<ApplyRes> res=new BasicResponse<ApplyRes>();
		String getOrder=String.format("SELECT * FROM `order` WHERE student_id='%s' AND course_id='%s' AND status=0", 
				req.getSudentId(),req.getCourseId());
		try {
			ResultSet orderResult=JDBCUtils.query(getOrder);
			if(orderResult.next()) {
				String getScore=String.format("SELECT * FROM grade WHERE course_id='%s' AND student_id='%s'",
						req.getCourseId(),req.getSudentId());
				ResultSet scoreResult=JDBCUtils.query(getScore);
				if(scoreResult.next()) {
					//�ɼ��ѷ���
					int score=scoreResult.getInt("score");
					ApplyRes applyres=new ApplyRes();
					applyres.setCourseId(req.getCourseId());
					applyres.setScore(score);
					if(orderResult.getInt("type")==0) {
						//�߷ֽ������
						if(score>=orderResult.getInt("threshold")) {
							applyres.setReward(10);
						}
						else {
							applyres.setReward(0);
						}
					}
					else {
						//�ͷ��⳥���
						if(score<=orderResult.getInt("threshold")) {
							applyres.setReward(20);
						}
						else {
							applyres.setReward(0);
						}
					}
					//���¸��˼�¼
					String getStudent=String.format("SELECT * FROM student WHERE id='%s'", req.getSudentId());
					ResultSet student=JDBCUtils.query(getStudent);
					student.next();
					int account=student.getInt("account");
					account=account + applyres.getReward();
					String updateOrder=String.format("UPDATE `order` SET status=1 WHERE student_id='%s' AND course_id='%s'",
							req.getSudentId(),req.getCourseId());
					String updateAccount=String.format("UPDATE student SET account=%d WHERE id='%s'", account,req.getSudentId());
					try {
						JDBCUtils.update(updateOrder);
						JDBCUtils.update(updateAccount);
						res.setResponse(0, "���߳ɹ�", applyres);
					}catch(SQLException e) {
						res.setResponse(10, "���ݿ���´���", null);
			            e.printStackTrace();
					}
				}
				else {
					res.setResponse(9, "�ɼ�δ����", null);
				}
			}
			else {
				res.setResponse(8, "����������", null);
			}
		}catch(SQLException e) {
			res.setResponse(2, "���ݿ��ѯ����", null);
            e.printStackTrace();
		}
		
		//����response
		PrintWriter writer = response.getWriter();  
		writer.write(new Gson().toJson(res, BasicResponse.class));  
		writer.flush(); 
	}
}
