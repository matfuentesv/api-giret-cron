package com.giret.apigiretcron.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Loan {

    private Long idPrestamo;
    private Long recursoId;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String solicitante;
    private String estado;


    @Override
    public String toString() {
        return "Loan{" +
                "idPrestamo=" + idPrestamo +
                ", recursoId=" + recursoId +
                ", fechaPrestamo='" + fechaPrestamo + '\'' +
                ", fechaDevolucion='" + fechaDevolucion + '\'' +
                ", solicitante='" + solicitante + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }

}
