package org.bit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;
import org.bit.mail.Mail;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;


/**
 * Show mails in the database
 * It's better use jsp here but I can't.
 * TODO change into jsp
 * */

public class Mails extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		
		MailAccess mailAccess;
		ResultSet rs;

		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<head><title>Mails</title>");
		out.println("<script>\n"
				+"function deletemail()\n"
				+"{\n"
				+	"document.form.action=\"delete\";\n"
				+	"document.form.submit();\n"
				+"}\n"
				+"function untrain()\n"
				+"{\n"
				+	"document.form.action=\"untrain\";\n"
				+	"document.form.submit();\n"
				+"}\n"
				+"</script>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Mails</h1>");

		try {

			mailAccess = new MysqlAccess(getServletContext().getRealPath("/sqlInfo.ini"));
			rs = mailAccess.query("SELECT * FROM Mail");
		
			out.println("<table>");
			out.println("<tr>");
			out.println("<th>Mails</th>");
			out.println("<th>Operations</th>");
			out.println("</tr>");
			while(rs.next())
			{
				
				out.println("<tr>");
				out.println("<td>");
				out.print("<font"
						+ (rs.getBoolean("Mail_tag") ? " color=\"red\" ":"") + ">" 
						);
				out.println("<p>");
				
				out.println(rs.getString("Mail_content"));
				out.println("</p></font></td>");
				
				out.println("<td>");
				out.println("<form name=\"form\" method=\"post\">");
				out.println("<input name=\"Mail_ID\" type=\"hidden\" value="+ rs.getLong("Mail_id") +"  />");
				out.println("<input type=\"button\" name=\"delete\" value=\"Delete\" onclick=\"form.action='delete';form.submit();\"/>");
				out.println("<input type=\"button\" name=\"untrain\" value=\"Untrain\" onclick=\"form.action='untrain';form.submit();\"/>");
				out.println("</form></td>");
				out.println("</tr>");
				out.println("</table>");
			}

		} catch (MySQLSyntaxErrorException e) {
			// show database state to front-end
			e.printStackTrace(out);
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace(out);
			e.printStackTrace();
		}

		
		out.println("</body>");
		out.println("</html>");
		
		
	}

	
}
