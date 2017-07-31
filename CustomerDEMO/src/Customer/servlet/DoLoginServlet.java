package Customer.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import Customer.beans.UserAccount;
import Customer.utils.DBUtils;
import Customer.utils.MyUtils;

/**
 * Servlet implementation class DoLoginServlet
 */
@WebServlet(urlPatterns = { "/doLogin" })
public class DoLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userName = request.getParameter("userName");
	      String password = request.getParameter("password");
	      String rememberMeStr = request.getParameter("rememberMe");
	      boolean remember= "Y".equals(rememberMeStr);
	 
	      
	      UserAccount user = null;
	      boolean hasError = false;
	      String errorString = null;
	 
	      if (userName == null || password == null
	               || userName.length() == 0 || password.length() == 0) {
	          hasError = true;
	          errorString = "Required username and password!";
	      } else {
	          Connection conn = MyUtils.getStoredConnection(request);
	          try {
	              // Tìm user trong DB.
	              user = DBUtils.findUser(conn, userName, password);
	              
	              if (user == null) {
	                  hasError = true;
	                  errorString = "User Name or password invalid";
	              }
	          } catch (SQLException e) {
	              e.printStackTrace();
	              hasError = true;
	              errorString = e.getMessage();
	          }
	      }
	      // Trong trường hợp có lỗi, forward về trang /WEB-INF/views/login.jsp
	      if (hasError) {
	          user = new UserAccount();
	          user.setUserName(userName);
	          user.setPassword(password);
	          
	          // Ghi các thông tin vào request trước khi forward.
	          request.setAttribute("errorString", errorString);
	          request.setAttribute("user", user);
	 
	          // Chuyển tiếp tới trang /WEB-INF/views/login.jsp
	          RequestDispatcher dispatcher //
	          = this.getServletContext().getRequestDispatcher("/WEB-INF/view/loginView.jsp");
	 
	          dispatcher.forward(request, response);
	      }
	      // Trường hợp không có lỗi.
	      // Lưu thông tin người dùng vào Session.
	      // Và chuyển hướng sang trang userInfo.
	      else {
	          HttpSession session = request.getSession();
	          MyUtils.storeLoginedUser(session, user);
	          
	          // Nếu người dùng chọn lưu thông tin đăng nhập vào Cookie
	          if(remember)  {
	              MyUtils.storeUserCookie(response,user);
	          }
	          // Ngược lại xóa Cookie
	          else  {
	              MyUtils.deleteUserCookie(response);
	          }            
	          
	          // Rồi chuyển hướng sang trang /userInfo.
	          response.sendRedirect(request.getContextPath() + "/userInfo");
	      }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
