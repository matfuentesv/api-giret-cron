package com.giret.apigiretcron.service;


import com.giret.apigiretcron.client.LoanClient;
import com.giret.apigiretcron.model.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class LoanSchedulerService {


    private final LoanClient loanClient;

    public LoanSchedulerService(LoanClient loanClient) {
        this.loanClient = loanClient;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void updateOverdueLoans() {
        LocalDate today = LocalDate.now();
        log.info("Ejecutando tarea programada: Verificando préstamos vencidos. Hoy es {}", today);


        final List<Loan> activeLoans = loanClient.findAllLoan()
                                       .stream()
                                       .filter(x->x.getEstado().equalsIgnoreCase("activo"))
                                        .toList();

        for (Loan loan : activeLoans) {

            LocalDate dueDate = LocalDate.parse(loan.getFechaDevolucion());
            if (dueDate.isBefore(today)) {
                loanClient.updateLoanByState("atrasado",loan.getIdPrestamo());
                log.info("Préstamo ID {} actualizado a Atrasado", loan.getIdPrestamo());
            }
        }
    }


}
