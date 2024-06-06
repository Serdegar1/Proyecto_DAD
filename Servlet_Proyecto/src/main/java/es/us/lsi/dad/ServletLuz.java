package es.us.lsi.dad;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletLuz extends HttpServlet {

	private static final long serialVersionUID = 861415467074580048L;

	private List<SensorLuz> lh;

	private Integer luz;
		
	public void init() throws ServletException {
		lh = new ArrayList<SensorLuz>();
		SensorLuz medidasl = new SensorLuz(0, 1,(long) 1.1);
		lh.add(medidasl);
		//ids.add(0);
		super.init(); 
		//throw new ServletException();
	}


	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		String message2 = Calendar.getInstance().getTime().toString();
		PrintWriter out = response.getWriter();
		//number++;
		out.println("<body><h1>" + message2 + "</h1><h2>" + /*number +*/
				 "</h2></body>");
	}

	public void destroy() {
		// do nothing.
	}

}
