/*
 * 对客户端学生申诉请求的封装
 */
package bean;

public class ApplyReq {
	private String studentId;
    private String courseId;

    public String getSudentId(){return studentId;}
    public void setSudentId(String _studentId){studentId=_studentId;}
    public String getCourseId(){return courseId;}
    public void setCourseId(String _courseId){courseId=_courseId;}
}
