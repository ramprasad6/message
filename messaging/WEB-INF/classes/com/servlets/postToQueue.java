package com.servlets;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.jms.*;
import javax.naming.*;

public class postToQueue extends HttpServlet
{
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
		{
			response.setContentType("text/html");
			ServletOutputStream out = response.getOutputStream();

			String message = request.getParameter("message");
			if (message.equals(""))
			{
				out.print("Enter a message to post to the queue");
			}

			else
			{
				try {
					QueueConnectionFactory qconFactory;
					QueueConnection qcon;
					QueueSession qsession;
					QueueSender qsender;
					Queue queue;
					TextMessage msg;

					InitialContext ic = new InitialContext();

					qconFactory = (QueueConnectionFactory) ic.lookup("javax.jms.QueueConnectionFactory");
					qcon = qconFactory.createQueueConnection();
					qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

					queue = (Queue) ic.lookup("dizzyworldQueue");

					qsender = qsession.createSender(queue);
					msg = qsession.createTextMessage();
					qcon.start();

					msg.setText(message);
					qsender.send(msg);
					System.out.println("The message, "+ message +", has been sent to the dizzyworldQueue.");

					out.print("<FONT SIZE='4' COLOR='navy'>");
					out.print("Your message has been posted<BR>");
					out.print("Monitor the message in the Administration Console<BR>");
					out.print("</FONT>");
					qsender.close();
					qsession.close();
					qcon.close();
				} catch (Exception e) {System.out.print("error " + e); }

			}

			out.print("<BR><A HREF='./welcome.html'>Back To Home Page</A><BR>");

		}
}