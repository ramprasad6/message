package com.servlets;


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.jms.*;
import javax.naming.*;

public class postToTopic extends HttpServlet
{
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
		{
			response.setContentType("text/html");
			ServletOutputStream out = response.getOutputStream();

			String message = request.getParameter("message");
			if (message.equals(""))
			{
				out.print("Enter a message to post to the Topic");
			}

			else
			{
				try {
					TopicConnectionFactory tconFactory;
					TopicConnection tcon;
					TopicSession tsession;
					TopicPublisher tpublish;
					Topic topic;
					TextMessage msg;

					InitialContext ic = new InitialContext();

					tconFactory = (TopicConnectionFactory) ic.lookup("javax.jms.TopicConnectionFactory");
					tcon = tconFactory.createTopicConnection();
					tsession = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

					topic = (Topic) ic.lookup("dizzyworldTopic");

					tpublish = tsession.createPublisher(topic);
					msg = tsession.createTextMessage();
					tcon.start();

					msg.setText(message);
					tpublish.publish(msg);
					System.out.println("The message, " + message + ", has been sent to dizzyworldTopic.");

					out.print("<FONT SIZE='4' COLOR='navy'>");
					out.print("Your message has been posted<BR>");
					out.print("Monitor the message in the Administration Console<BR>");
					out.print("</FONT>");

					tpublish.close();
					tsession.close();
					tcon.close();
				} catch (Exception e) {out.print("error " + e); }

			}

			out.print("<BR><A HREF='./welcome.html'>Back To Home Page</A><BR>");

		}
}