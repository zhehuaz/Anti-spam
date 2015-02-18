package org.bit.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bit.classifyer.Classifyer;
import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;
import org.bit.mail.Email;
import org.bit.mail.Mail;
import org.bit.train.UnknownDBException;
import org.bit.util.GlobalConstants;

public class Classify extends HttpServlet{

	Classifyer classifyer;
	MailAccess mailAccess;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
	
		try {
			classifyer = new Classifyer("com.mysql.jdbc.Driver", getServletContext().getRealPath(GlobalConstants.SQL_CONFIG_PATH));
			Mail mail = new Email();
			mail.setContent(req.getParameter("content"));
			if(classifyer.classify(mail))
				resp.getWriter().println("This is spam");
			else
				resp.getWriter().println("This is normal");
		} catch (UnknownDBException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
