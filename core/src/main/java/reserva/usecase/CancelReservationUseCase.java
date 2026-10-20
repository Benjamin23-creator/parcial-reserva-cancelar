package reserva.usecase;

import reserva.exception.PersistenceException;
import reserva.exception.ReservationNotFoundException;
import reserva.input.CancelReservationInput;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.util.UUID;

public class CancelReservationUseCase implements CancelReservationInput {

    private final ReservationRepository repositorio;

    public CancelReservationUseCase(ReservationRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Reserva cancelarReserva(UUID id) {
        Reserva reserva = repositorio.buscarPorId(id)
                .orElseThrow(() -> new ReservationNotFoundException(
                        "No existe una reserva con el identificador " + id));

        reserva.cancelar();

        if (!repositorio.guardar(reserva)) {
            throw new PersistenceException("No se pudo guardar la reserva cancelada");
        }

        return reserva;
    }
}
