package org.bit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.Global;
import org.bit.conn.MysqlAccess;
import org.bit.train.Trainer;
import org.bit.train.UnknownDBException;
import org.bit.util.GlobalConstants;

public class Delete extends HttpServlet{
	/**
	 * Delete a mail from database.
	 * */
	Trainer trainer;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		try {
			trainer = new Trainer("com.mysql.jdbc.Driver",getServletContext().getRealPath(GlobalConstants.SQL_CONFIG_PATH),
					getServletContext().getRealPath(GlobalConstants.SQL_CONFIG_PATH));
			long id = Long.parseLong(req.getParameter("Mail_ID"));
			trainer.untrain(id);
			new MysqlAccess(getServletContext().getRealPath("/sqlInfo.ini")).delete(id);
			out.println("Delete Success!");
			out.println("<a href=\"mails?action=view\">");
			
		} catch (UnknownDBException | SQLException e) {
			e.printStackTrace();
			resp.getWriter().println("Delete Fail.");
		}
		
		
		
		
		
		
	}
	
}
