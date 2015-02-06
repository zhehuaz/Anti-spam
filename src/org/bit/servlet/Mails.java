package org.bit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;
import org.bit.mail.Mail;


/**
 * Show mails in the database
 * It's better use jsp here but I can't.
 * TODO change into jsp
 * */

public class Mails extends HttpServlet{

	//TODO temperarily MAX
	private final static int MAX = 1000; 

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<head><title>Mails</title></head>");
		out.println("<body>");
		out.println("<h1>Mails</h1>");
		out.println("<table>");
		out.println("<tr>");
		out.println("<th>Mails</th>");
		out.println("<th>Operations</th>");
		out.println("</tr>");

		try {
			MailAccess mailAccess = new MysqlAccess(getServletContext().getRealPath("/sqlInfo.ini"));
		
			// TODO temporarily for
			for(int i = 0;i < MAX;i ++)
			{
				out.println("<tr><form>");
				out.println("<td>");
				out.println("<input name=\"Mail_ID\" type=\"hidden\" value="+ i +"/>");
				out.println("<p>");
				Mail mail = mailAccess.query(i);
				if(mail != null)
					out.println(mail.getContent());
				out.println("</p></td>");
				
				out.println("<td>");
				
				// TODO WRONG HERE!
				out.println("<a href=\"delete\">Delete</a>");
				out.println("<a href=\"untrain\">Untrain</a>");
				out.println("</td>");
				out.println("</form></tr>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");
		
	}

	
}
