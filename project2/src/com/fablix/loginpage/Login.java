package com.fablix.loginpage;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/*
 * 	Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	public Login()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (username.equals("anteater") && password.equals("123456"))
		{
			// login success:
			
			// set this user into the session
			request.getSession().setAttribute("user", new User(username));
			
			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("status", "success");
			responseJsonObject.addProperty("message", "success");
			
			response.getWriter().write(responseJsonObject,toString());
		}
		else
		{
			// login fail
			request.getSession().setAttribute("user", new User(username));
			
			JsonObject responseJsonObject = new JsonObject();
			responseJsonObject.addProperty("status", "fail");
			if (!username.equals("anteater"))
			{
				responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
			}
			else if (!password.equals("123456"))
				responseJsonObject.addProperty("message", "incorrect password");
		}
		
		response.getWriter().write(responseJsonObject.toString());
	}
	
	/*
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
