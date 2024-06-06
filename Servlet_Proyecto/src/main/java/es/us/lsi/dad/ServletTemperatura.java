package es.us.lsi.dad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
		SensorTemperatura medidast = new SensorTemperatura(0, 1.1,(long) 1.1);
		th.add(medidast);
		//ids.add(0);
		super.init();		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idth = req.getParameter("idth");
		String temperatura = req.getParameter("temperatura");
		
		Integer idth2 = Integer.valueOf(idth);
		Double temperatura2 = Double.valueOf(temperatura);

		SensorTemperatura temp = new SensorTemperatura(idth2, temperatura2, (long) 1.1);

		if(th.contains(temp)){
			response(resp, "Aqui está su temperatura regsitrada" + temp);
		}else {
			response(resp, "No existe esa temperatura");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    BufferedReader reader = req.getReader();
	    Gson gson = new Gson();
	    SensorTemperatura Stemp = gson.fromJson(reader, SensorTemperatura.class);
		
		if ( !Stemp.getIdth().equals("") && !Stemp.getTemperatura().equals("") && !Stemp.getTimestamp().equals("")) {
			SensorTemperatura temp = new SensorTemperatura(Stemp.getIdth(), Stemp.getTemperatura(), Stemp.getTimestamp());
			
			th.add(temp);
			resp.getWriter().println(gson.toJson(Stemp));
			resp.setStatus(201);
		}else{
			resp.setStatus(300);
			response(resp, "Objeto del tipo incorrecto");
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
