package org.bit.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bit.conn.MysqlAccess;
import org.bit.train.Trainer;
import org.bit.train.UnknownDBException;

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
	
		try {
			trainer = new Trainer("com.mysql.jdbc.Driver",getServletContext().getRealPath("/sqlInfo.ini"),
					getServletContext().getRealPath("/sqlInfo.ini"));
			long id = Long.parseLong(req.getParameter("Mail_ID"));
			trainer.untrain(id);
			new MysqlAccess(getServletContext().getRealPath("/sqlInfo.ini")).delete(id);
			resp.getWriter().println("Delete Success!");
			
		} catch (UnknownDBException | SQLException e) {
			e.printStackTrace();
			resp.getWriter().println("Delete Fail.");
		}
		
		
		
		
		
		
	}
	
}
