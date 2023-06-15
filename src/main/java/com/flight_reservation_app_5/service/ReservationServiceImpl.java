package com.flight_reservation_app_5.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight_reservation_app_5.dto.ReservationRequest;
import com.flight_reservation_app_5.entity.Flight;
import com.flight_reservation_app_5.entity.Passenger;
import com.flight_reservation_app_5.entity.Reservation;
import com.flight_reservation_app_5.repository.FlightRepository;
import com.flight_reservation_app_5.repository.PassengerRepository;
import com.flight_reservation_app_5.repository.ReservationRepository;
import com.flight_reservation_app_5.utilities.EmailUtil;
import com.flight_reservation_app_5.utilities.PDFGenerator;



@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private PassengerRepository passengerRepo;
	
	@Autowired
	private FlightRepository flightRepo;
	
	@Autowired 
	private ReservationRepository reservationRepo;
	
	@Autowired
	private PDFGenerator pdfGenerator;
	
	@Autowired
	private EmailUtil emailUtil;

	@Override
	public Reservation bookFlight(ReservationRequest request) {
		
		
		
		Passenger passanger=new Passenger();
		
		passanger.setFirstName(request.getFirstName());
		passanger.setLastName(request.getLastName());
		passanger.setMiddleName(request.getMiddleName());
		passanger.setEmail(request.getEmail());
		passanger.setPhone(request.getPhone());
		
		passengerRepo.save(passanger);
		
		long flightId = request.getFlightId();
		Optional<Flight> findById = flightRepo.findById(flightId);
		Flight flight = findById.get();
		
		Reservation reservation=new Reservation();
		reservation.setFlight(flight);
		reservation.setPassenger(passanger);
		reservation.setCheckedIn(false);
		reservation.setNumberOfBags(0);
		String filePath="C:\\Flight\\Flight Reservation System\\Flight_Reservation_App\\tickets\\reservation"+reservation.getId()+".pdf";

		reservationRepo.save(reservation);
		pdfGenerator.generatePDF(filePath,reservation);
		emailUtil.sendeMail(passanger.getEmail(), filePath);
		return reservation;
	}

	
}
