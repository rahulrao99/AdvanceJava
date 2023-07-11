package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.enitites.Movie;
import com.app.enitites.Ticket;
import com.app.service.TicketService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins="http://licalhost:3000")
public class TicketController {

	@Autowired
	private TicketService tService;
	
	public TicketController() {
		System.out.println("in ticket ctr " +getClass());
		
	}
	@GetMapping
	public List<Ticket> listTickets() {
		System.out.println("in list movie");
			return tService.getAllTickets();
	}

	@PostMapping
	public ResponseEntity<?> saveticketDetails(@RequestBody Ticket transientTicket){
		System.out.println("in save " +transientTicket);
		return new ResponseEntity<>(tService.addTicketDetails(transientTicket),HttpStatus.CREATED);
	}
	
	@PutMapping
	public Ticket updateTicketDetails(@RequestBody Ticket transientTicket) {
		System.out.println("in save "+ transientTicket);
		return tService.updateTicketDetails(transientTicket);
	}

	@DeleteMapping("/{tid}")
	public String deleteTicketDetails(@PathVariable Long tid) {
		System.out.println("in del " +tid);
		String msg="deleted";
		 tService.deleteTicketDetails(tid);
		 return msg;
	} 
	
}
