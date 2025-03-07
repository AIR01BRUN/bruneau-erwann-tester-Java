package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        calculateFare(ticket, false);

    }

    public void calculateFare(Ticket ticket, boolean discount) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        int inHour = ticket.getInTime().getHours();
        int outHour = ticket.getOutTime().getHours();

        // TODO: Some tests are failing here. Need to check if this logic is correct
        // int duration = outHour - inHour;

        long durationInMilliseconde = Math.abs(ticket.getOutTime().getTime() - ticket.getInTime().getTime());
        float durationInMinute = TimeUnit.MILLISECONDS.toMinutes(durationInMilliseconde);
        float durationInHour = durationInMinute / 60;

        if (durationInHour < 0.5) {
            ticket.setPrice(0);
        } else {

            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(durationInHour * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationInHour * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unkown Parking Type");
            }

            if (discount == true) {
                ticket.setPrice(ticket.getPrice() * 0.95);
            }
            System.out.println("Price ++ : " + ticket.getPrice());
        }

    }

}