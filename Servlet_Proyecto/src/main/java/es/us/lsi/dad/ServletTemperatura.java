package es.us.lsi.dad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletTemperatura extends HttpServlet{
	private static final long serialVersionUID = -6201150158950823811L;

	private List<SensorTemperatura> th;
	
	public void init() throws ServletException {
		th = new ArrayList<SensorTemperatura>();
		
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idth = req.getParameter("idth");
		if(th.contains(idth)){
			response(resp, "Aqui esta su temperatura" + idth);
		}else {
			response(resp, "No existe esa temperatura");
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    BufferedReader reader = req.getReader();
	    Gson gson = new Gson();
		SensorTemperatura stempt = gson.fromJson(reader, SensorTemperatura.class);
	    
		if (!stempt.equals("")) {
			th.add(stempt);
			resp.getWriter().println(gson.toJson(stempt));
			resp.setStatus(201);
			response(resp, "Añadido con éxito");
		}else{
			resp.setStatus(300);
			response(resp, "Please select an id");
		}
	   
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    BufferedReader reader = req.getReader();
	    
	    Gson gson = new Gson();
		
		SensorTemperatura stempt = gson.fromJson(reader, SensorTemperatura.class);
		
		if (!stempt.getIdth().equals("") && th.contains(stempt.getIdth())) {
			th.remove(stempt.getIdth());
			resp.getWriter().println(gson.toJson(stempt));
			resp.setStatus(201);
		}else{
			resp.setStatus(300);
			response(resp, "No existe esa temperatura");
		}
	   
	}
	
	
	
	private void response(HttpServletResponse resp, String msg) throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<t1>" + msg + "</t1>");
		out.println("</body>");
		out.println("</html>");
	}
	
	
	
}
