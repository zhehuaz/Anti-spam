package org.bit.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bit.conn.MailAccess;
import org.bit.conn.MysqlAccess;
import org.bit.mail.Email;
import org.bit.mail.Mail;
import org.bit.train.Trainer;
import org.bit.train.UnknownDBException;
import org.bit.util.GlobalConstants;

public class Train extends HttpServlet{
	Trainer trainer;
	Mail mail;
	MailAccess mailAccess;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
		req.setCharacterEncoding("UTF-8");
		
		try {
			trainer = new Trainer("com.mysql.jdbc.Driver",getServletContext().getRealPath(GlobalConstants.SQL_CONFIG_PATH),
					getServletContext().getRealPath(GlobalConstants.SQL_CONFIG_PATH));
			mail = new Email();
			String str = req.getParameter("content");
			System.out.println(str);
			mail.setContent(req.getParameter("content"));
			if(req.getParameter("isSpam").equals("true"))
				mail.setTag(true);
			else
				mail.setTag(false);
			trainer.train(mail,true);//train and insert the mail into database
			
			resp.getWriter().println("Success!");
		} catch (SQLException | UnknownDBException e) {
			e.printStackTrace();
		}
	}
	
	
}
