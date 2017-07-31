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

import Customer.beans.Product;
import Customer.utils.DBUtils;
import Customer.utils.MyUtils;

/**
 * Servlet implementation class DoEditProductServlet
 */
@WebServlet(urlPatterns = { "/doEditProduct" })
public class DoEditProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoEditProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 Connection conn = MyUtils.getStoredConnection(request);
		 
	      String code = (String) request.getParameter("code");
	      String name = (String) request.getParameter("name");
	      String priceStr = (String) request.getParameter("price");
	      float price = 0;
	      try {
	          price = Float.parseFloat(priceStr);
	      } catch (Exception e) {
	      }
	      Product product = new Product(code, name, price);
	 
	      String errorString = null;
	 
	      try {
	          DBUtils.updateProduct(conn, product);
	      } catch (SQLException e) {
	          e.printStackTrace();
	          errorString = e.getMessage();
	      }
	      // Lưu thông tin vào request attribute trước khi forward sang views.
	      request.setAttribute("errorString", errorString);
	      request.setAttribute("product", product);
	 
	      // Nếu có lỗi forward sang trang edit
	      if (errorString != null) {
	          RequestDispatcher dispatcher = request.getServletContext()
	                  .getRequestDispatcher("/WEB-INF/view/editProductView.jsp");
	          dispatcher.forward(request, response);
	      }
	      // Nếu mọi thứ tốt đẹp.
	      // Redirect sang trang danh sách sản phẩm.
	      else {
	          response.sendRedirect(request.getContextPath() + "/productList");
	      }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

}
