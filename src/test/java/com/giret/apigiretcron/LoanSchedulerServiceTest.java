package com.giret.apigiretcron;

import com.giret.apigiretcron.client.LoanClient;
import com.giret.apigiretcron.model.Loan;
import com.giret.apigiretcron.service.LoanSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoanSchedulerServiceTest {

    private LoanClient loanClient;
    private LoanSchedulerService loanSchedulerService;

    @BeforeEach
    void setUp() {
        loanClient = mock(LoanClient.class);
        loanSchedulerService = new LoanSchedulerService(loanClient);
    }

    @Test
    void testUpdateOverdueLoans_marksOnlyOverdueActiveLoans() {
        LocalDate today = LocalDate.now();
        LocalDate overdueDate = today.minusDays(1);
        LocalDate dueToday = today;
        LocalDate futureDate = today.plusDays(1);

        Loan overdueLoan = Loan.builder()
                .idPrestamo(1L)
                .estado("activo")
                .fechaDevolucion(overdueDate.toString())
                .build();

        Loan dueTodayLoan = Loan.builder()
                .idPrestamo(2L)
                .estado("activo")
                .fechaDevolucion(dueToday.toString())
                .build();

        Loan futureLoan = Loan.builder()
                .idPrestamo(3L)
                .estado("activo")
                .fechaDevolucion(futureDate.toString())
                .build();

        Loan inactiveLoan = Loan.builder()
                .idPrestamo(4L)
                .estado("cerrado")
                .fechaDevolucion(overdueDate.toString())
                .build();

        when(loanClient.findAllLoan()).thenReturn(List.of(overdueLoan, dueTodayLoan, futureLoan, inactiveLoan));

        loanSchedulerService.updateOverdueLoans();

        verify(loanClient, times(1)).updateLoanByState("atrasado", 1L);
        verify(loanClient, never()).updateLoanByState("atrasado", 2L);
        verify(loanClient, never()).updateLoanByState("atrasado", 3L);
        verify(loanClient, never()).updateLoanByState("atrasado", 4L);
    }

    @Test
    void testUpdateOverdueLoans_handlesEmptyListGracefully() {
        when(loanClient.findAllLoan()).thenReturn(List.of());
        loanSchedulerService.updateOverdueLoans();
        verify(loanClient, never()).updateLoanByState(anyString(), anyLong());
    }





    @Test
    void testUpdateOverdueLoans_multipleOverdueLoans() {
        LocalDate today = LocalDate.now();
        LocalDate overdueDate1 = today.minusDays(2);
        LocalDate overdueDate2 = today.minusDays(5);

        Loan loan1 = Loan.builder()
                .idPrestamo(7L)
                .estado("activo")
                .fechaDevolucion(overdueDate1.toString())
                .build();

        Loan loan2 = Loan.builder()
                .idPrestamo(8L)
                .estado("activo")
                .fechaDevolucion(overdueDate2.toString())
                .build();

        when(loanClient.findAllLoan()).thenReturn(List.of(loan1, loan2));

        loanSchedulerService.updateOverdueLoans();

        verify(loanClient, times(1)).updateLoanByState("atrasado", 7L);
        verify(loanClient, times(1)).updateLoanByState("atrasado", 8L);
    }

    @Test
    void testBuilderAndGettersAndSetters() {
        Loan loan = Loan.builder()
                .idPrestamo(1L)
                .estado("activo")
                .fechaDevolucion("2025-07-15")
                .build();

        assertEquals(1L, loan.getIdPrestamo());
        assertEquals("activo", loan.getEstado());
        assertEquals("2025-07-15", loan.getFechaDevolucion());


        loan.setEstado("cerrado");
        assertEquals("cerrado", loan.getEstado());
    }

    @Test
    void testEqualsAndHashCode() {
        Loan loan1 = Loan.builder()
                .idPrestamo(1L)
                .estado("activo")
                .fechaDevolucion("2025-07-15")
                .build();

        Loan loan2 = Loan.builder()
                .idPrestamo(1L)
                .estado("activo")
                .fechaDevolucion("2025-07-15")
                .build();

        assertEquals(loan1, loan2);
        assertEquals(loan1.hashCode(), loan2.hashCode());
    }

    


}
