/**
 * 
 */
package org.bit.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author langley
 *
 */
public class ServletDemo extends HttpServlet {

	Properties prop = new Properties();
	UserAccess userAccess ;
	
	//Haha...I don't know why I use GET...
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		userAccess = new UserAccess("jdbc:mysql://localhost:3306/test","langley", "");
		
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		try {
			if(userAccess.check(req.getParameter("user"),req.getParameter("password")))
			{
				out.println("<a href=\"/Anti-Spam/train.html\"> Train </a>");
				out.println("</br>");
				out.println("<a href=\"/Anti-Spam/classify.html\"> Classify </a>");
			}
			else
				out.println("<p>Permission denied.</p>");
		} catch (SQLException e) {
			out.println("Database error");
			e.printStackTrace();
		}
		out.println("</body>");
		out.println("</html>");
		
	}
}
