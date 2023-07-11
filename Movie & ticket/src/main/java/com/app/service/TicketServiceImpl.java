package com.app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.enitites.Ticket;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.TicketRepository;

@Service
@Transactional
public class TicketServiceImpl implements TicketService{

	@Autowired
	private TicketRepository tRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public List<Ticket> getAllTickets() {
		// TODO Auto-generated method stub
		return tRepo.findAll();
	}

	@Override
	public Ticket addTicketDetails(Ticket transientTicket) {
		// TODO Auto-generated method stub
		return tRepo.save(transientTicket);
	}

	@Override
	public Ticket updateTicketDetails(Ticket transientTicket) {
		// TODO Auto-generated method stub
		return tRepo.save(transientTicket);
	}

	@Override
	public Ticket getTicketDedtails(Long tid) {
		// TODO Auto-generated method stub
		return tRepo.findById(tid)
				.orElseThrow(()->new ResourceNotFoundException("ticket id invalid"));
	}

	@Override
	public String deleteTicketDetails(Long tid) {
		// TODO Auto-generated method stub
		return null;
	}

}
