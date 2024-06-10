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
		super.init(); 

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idl = req.getParameter("idl");
		String valorluz = req.getParameter("valurluz");
		
		Integer idl2 = Integer.valueOf(idl);
		Integer valorluz2 = Integer.valueOf(valorluz);

		SensorLuz luzz = new SensorLuz(idl2, valorluz2, (long) 1.1);

		if(lh.contains(luzz)){
			response(resp, "Aqui está su temperatura regsitrada" + luzz);
		}else {
			response(resp, "No existe esa temperatura");
		}
	}

	public void destroy() {
		// do nothing.
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
