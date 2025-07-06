package cl.veterinary.apigiretcron.service;


import cl.veterinary.apigiretcron.client.LoanClient;
import cl.veterinary.apigiretcron.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanSchedulerService {

    @Autowired
    LoanClient loanClient;


    @Scheduled(cron = "*/30 * * * * *")
    public void updateOverdueLoans() {
        LocalDate today = LocalDate.now();
        System.out.println("Ejecutando tarea programada: Verificando préstamos vencidos. Hoy es " + today);


        final List<Loan> activeLoans = loanClient.findAllLoan();

        for (Loan loan : activeLoans) {
            LocalDate dueDate = LocalDate.parse(loan.getFechaDevolucion());
            if (dueDate.isBefore(today)) {
                loanClient.updateLoanByState("atrasado",loan.getIdPrestamo());
                System.out.println("Prestamo ID " + loan.getIdPrestamo() + " actualizado a Atrasado");
            }
        }
    }


}
