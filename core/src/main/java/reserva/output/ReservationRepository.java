package reserva.output;

import reserva.modelo.Reserva;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    Optional<Reserva> buscarPorId(UUID id);

    boolean guardar(Reserva reserva);
}
