package reserva.input;

import reserva.modelo.Reserva;

import java.util.UUID;

public interface CancelReservationInput {

    Reserva cancelarReserva(UUID id);
}
