package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import bean.BasicResponse;
import bean.ChangeInfoReq;
import utils.JDBCUtils;

/**
 * Servlet implementation class ChangePw_servlet
 */
@WebServlet("/user/changePassword")
public class ChangePw_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePw_servlet() {
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
		ChangeInfoReq req=gson.fromJson(reqJson, ChangeInfoReq.class);
		BasicResponse<String> res=new BasicResponse<String>();
		
		String update;
		if(req.getIdentity()==1)
			update=String.format("UPDATE student SET pw='%s' WHERE id='%s'",req.getChangeItem(), req.getId());
		else
			update=String.format("UPDATE teacher SET pw='%s' WHERE id='%s'", req.getChangeItem(),req.getId());
		try {
			JDBCUtils.update(update);
			res.setResponse(0, "更新成功", req.getChangeItem());
		}catch(SQLException e) {
			res.setResponse(2, "数据库更新错误", null);
            e.printStackTrace();
		}
		
		//返回response
		PrintWriter writer = response.getWriter();  
		writer.write(new Gson().toJson(res, BasicResponse.class));  
		writer.flush(); 
	}
}
