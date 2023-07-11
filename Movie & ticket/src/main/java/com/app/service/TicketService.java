package com.app.service;

import java.util.List;

import com.app.enitites.Ticket;

public interface TicketService {
	List<Ticket> getAllTickets();
	
	Ticket addTicketDetails(Ticket transientTicket);
	
	Ticket updateTicketDetails(Ticket transientTicket);
	
	Ticket getTicketDedtails(Long tid);
	
	String deleteTicketDetails(Long tid);
	
}
