package reserva.modelo;

import reserva.exception.CantidadPersonasInvalidaException;
import reserva.exception.EmailObligatorioException;
import reserva.exception.FechaObligatoriaException;
import reserva.exception.IdentificadorObligatorioException;
import reserva.exception.ReservaYaCanceladaException;

import java.time.LocalDate;
import java.util.UUID;

public class Reserva {

    private final UUID id;
    private final String emailContacto;
    private final LocalDate fecha;
    private final int cantidadPersonas;
    private EstadoReserva estado;

    private Reserva(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {
        this.id = id;
        this.emailContacto = emailContacto;
        this.fecha = fecha;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = EstadoReserva.ACTIVA;
    }

    public static Reserva instance(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {
        if (id == null) {
            throw new IdentificadorObligatorioException("El identificador es obligatorio");
        }
        if (emailContacto == null || emailContacto.isBlank()) {
            throw new EmailObligatorioException("El email de contacto es obligatorio");
        }
        if (fecha == null) {
            throw new FechaObligatoriaException("La fecha es obligatoria");
        }
        if (cantidadPersonas <= 0) {
            throw new CantidadPersonasInvalidaException("La cantidad de personas debe ser mayor a cero");
        }
        return new Reserva(id, emailContacto, fecha, cantidadPersonas);
    }

    public UUID getId() {
        return id;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void cancelar() {
        if (estado == EstadoReserva.CANCELADA) {
            throw new ReservaYaCanceladaException("La reserva ya se encuentra cancelada");
        }
        this.estado = EstadoReserva.CANCELADA;
    }
}
