package usecase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reserva.exception.PersistenceException;
import reserva.exception.ReservationNotFoundException;
import reserva.exception.ReservaYaCanceladaException;
import reserva.modelo.EstadoReserva;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;
import reserva.usecase.CancelReservationUseCase;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelReservationUseCaseTest {

    @Mock
    private ReservationRepository repositorio;

    private static final UUID ID = UUID.fromString("cabbd417-1841-4b25-8798-e8d54df1416e");
    private static final String EMAIL = "cliente@correo.com";
    private static final LocalDate FECHA = LocalDate.of(2026, 10, 20);

    @Test
    public void cancelarReserva_reservaExistente_devuelveReservaCancelada() {
        // Arrange
        CancelReservationUseCase casoDeUso = new CancelReservationUseCase(repositorio);
        Reserva reserva = Reserva.instance(ID, EMAIL, FECHA, 2);

        when(repositorio.buscarPorId(ID)).thenReturn(Optional.of(reserva));
        when(repositorio.guardar(any(Reserva.class))).thenReturn(true);

        // Act
        Reserva reservaCancelada = casoDeUso.cancelarReserva(ID);

        // Assert
        Assertions.assertNotNull(reservaCancelada);
        Assertions.assertEquals(EstadoReserva.CANCELADA, reservaCancelada.getEstado());

        ArgumentCaptor<Reserva> captor = ArgumentCaptor.forClass(Reserva.class);
        verify(repositorio).guardar(captor.capture());

        Reserva reservaGuardada = captor.getValue();
        Assertions.assertEquals(ID, reservaGuardada.getId());
        Assertions.assertEquals(EstadoReserva.CANCELADA, reservaGuardada.getEstado());
    }

    @Test
    public void cancelarReserva_reservaInexistente_lanzaReservationNotFoundException() {
        // Arrange
        CancelReservationUseCase casoDeUso = new CancelReservationUseCase(repositorio);

        when(repositorio.buscarPorId(ID)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ReservationNotFoundException.class, () ->
                casoDeUso.cancelarReserva(ID));

        verify(repositorio, never()).guardar(any(Reserva.class));
    }

    @Test
    public void cancelarReserva_reservaYaCancelada_lanzaReservaYaCanceladaException() {
        // Arrange
        CancelReservationUseCase casoDeUso = new CancelReservationUseCase(repositorio);
        Reserva reserva = Reserva.instance(ID, EMAIL, FECHA, 2);
        reserva.cancelar();

        when(repositorio.buscarPorId(ID)).thenReturn(Optional.of(reserva));

        // Act & Assert
        Assertions.assertThrows(ReservaYaCanceladaException.class, () ->
                casoDeUso.cancelarReserva(ID));

        verify(repositorio, never()).guardar(any(Reserva.class));
    }

    @Test
    public void cancelarReserva_guardarFallido_lanzaPersistenceException() {
        // Arrange
        CancelReservationUseCase casoDeUso = new CancelReservationUseCase(repositorio);
        Reserva reserva = Reserva.instance(ID, EMAIL, FECHA, 2);

        when(repositorio.buscarPorId(ID)).thenReturn(Optional.of(reserva));
        when(repositorio.guardar(any(Reserva.class))).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(PersistenceException.class, () ->
                casoDeUso.cancelarReserva(ID));
    }
}
