package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;

import bean.BasicResponse;

/**
 * Servlet implementation class TeacherUpload_servlet
 */
@WebServlet("/user/teacher/upload")
public class TeacherUpload_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeacherUpload_servlet() {
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
		System.out.println("get post request");
		String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
		File file = new File(savePath);
		if(!file.exists() && !file.isDirectory()) {
			System.out.println(savePath+"目录不存在，需要创建");
            //创建目录
            file.mkdir();
		}
		
		BasicResponse<String> res=new BasicResponse<String>();
		
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			if(!ServletFileUpload.isMultipartContent(request)){
                //按照传统方式获取数据
                return;
            }
			List<FileItem> list = upload.parseRequest(request);
			System.out.println("multiparts part number: "+list.size());
			for(FileItem item : list){
				if(item.isFormField()){
                    String name = item.getFieldName();
                    String value = item.getString("UTF-8");
                    System.out.println(name + "=" + value);
				}
				else {
					String filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
                    filename = filename.substring(filename.lastIndexOf("\\")+1);
                    System.out.println(filename);
                    InputStream in = item.getInputStream();
                    FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
                    byte buffer[] = new byte[1024];
                    int len = 0;
                    while((len=in.read(buffer))>0){
                    	out.write(buffer, 0, len);
                    }
                    in.close();
                    out.close();
                    item.delete();
                    res.setResponse(0, "success", "success");
				}
			}
		}catch (Exception e) {
            res.setResponse(1,"failed","failed");
            e.printStackTrace();
            
        }
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();  
        writer.write(new Gson().toJson(res, BasicResponse.class));  
        writer.flush(); 
	}
}
