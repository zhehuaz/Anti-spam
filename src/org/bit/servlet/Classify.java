package org.bit.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bit.classifyer.Classifyer;
import org.bit.conn.MailAccess;
import org.bit.mail.Email;
import org.bit.mail.Mail;
import org.bit.train.UnknownDBException;

public class Classify extends HttpServlet{

	Classifyer classifyer;
	MailAccess mailAccess;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(getServletContext().getRealPath("/sqlInfo.ini")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String url = prop.getProperty("url");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		
		try {
			classifyer = new Classifyer("com.mysql.jdbc.Driver", url, user, password);
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
