package it.presentation;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import it.business.RubricaEjbImpl;
import it.data.Contatto;

/**
 * Servlet implementation class CercaServlet
 */
public class CercaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    @EJB
    RubricaEjbImpl rubricaEjb;
    public CercaServlet() {
        super();
    }

	/*
	 * Servlet che i gestisce la ricerca del conttatto sia per nome che per cognome.
	 * In base alla scelta fatta nella select verra' utilizzato il metodogetContattoByNumero() oppure il
	 * getContattoByCognome 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 	HttpSession session=request.getSession();
 	Contatto contatto =new Contatto();
 	String azione="cerca";
		if(request.getParameter("azione").equals("cercaNumero")) {
 		contatto=rubricaEjb.getContattoByNumero(request.getParameter("numtel"));
 	} else if(request.getParameter("azione").equals("cercaCognome")) {
 		contatto=rubricaEjb.getContattoByCognome(request.getParameter("cognome"));
 		
		
 	}
		session.setAttribute("contatto", contatto);
		session.setAttribute("azione", azione);
 	request.getServletContext().getRequestDispatcher("/visualizza.jsp").forward(request, response);
	}

}
