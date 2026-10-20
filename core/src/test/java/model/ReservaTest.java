package model;
import org.junit.jupiter.api.Test;
import reserva.exception.*;
import reserva.modelo.EstadoReserva;
import reserva.modelo.Reserva;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
public class ReservaTest {

    private static final UUID ID = UUID.fromString("cabbd417-1841-4b25-8798-e8d54df1416e");
    private static final String EMAIL = "cliente@correo.com";
    private static final LocalDate FECHA = LocalDate.of(2026, 10, 20);
    private static final int CANTIDAD_PERSONAS = 2;

    @Test
    void crearReservaConTodosLosAtributos_DevuelveUnaInstanciaConLosMismosValores() {


        Reserva reserva = Reserva.instance(ID, EMAIL, FECHA, CANTIDAD_PERSONAS);


        assertNotNull(reserva);
        assertEquals(ID, reserva.getId());
        assertEquals(EMAIL, reserva.getEmailContacto());
        assertEquals(FECHA, reserva.getFecha());
        assertEquals(CANTIDAD_PERSONAS, reserva.getCantidadPersonas());
        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
    }
    @Test
    void identificadorNulo_LanzaIdentificadorObligatorioException() {
        assertThrows(IdentificadorObligatorioException.class, () ->
                Reserva.instance(null, EMAIL, FECHA, CANTIDAD_PERSONAS));
    }
    @Test
    void fechaNula_LanzaFechaObligatoriaException() {
        assertThrows(FechaObligatoriaException.class, () ->
                Reserva.instance(ID, EMAIL, null, CANTIDAD_PERSONAS));
    }
    @Test
    void emailNuloOVacio_LanzaEmailObligatorioException() {
        assertThrows(EmailObligatorioException.class, () ->
                Reserva.instance(ID, "", FECHA, CANTIDAD_PERSONAS));
        assertThrows(EmailObligatorioException.class, () ->
                Reserva.instance(ID, null, FECHA, CANTIDAD_PERSONAS));
        assertThrows(EmailObligatorioException.class, () ->
                Reserva.instance(ID, "   ", FECHA, CANTIDAD_PERSONAS));
    }
    @Test
    void cantidadPersonasCeroONegativa_LanzaCantidadPersonasInvalidaException() {
        assertThrows(CantidadPersonasInvalidaException.class, () ->
                Reserva.instance(ID, EMAIL, FECHA, 0));
        assertThrows(CantidadPersonasInvalidaException.class, () ->
                Reserva.instance(ID, EMAIL, FECHA, -1));
    }
    @Test
    void cancelarReservaActiva_DevuelveReservaEnEstadoCancelada() {

        Reserva reserva = Reserva.instance(ID, EMAIL, FECHA, CANTIDAD_PERSONAS);


        reserva.cancelar();


        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }
    @Test
    void cancelarReservaYaCancelada_LanzaReservaYaCanceladaException() {
        Reserva reserva = Reserva.instance(ID, EMAIL, FECHA, CANTIDAD_PERSONAS);
        reserva.cancelar();

        assertThrows(ReservaYaCanceladaException.class, reserva::cancelar);
    }
}
