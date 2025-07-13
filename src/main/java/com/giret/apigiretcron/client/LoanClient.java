package com.giret.apigiretcron.client;


import com.giret.apigiretcron.model.Loan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "loanClient", url = "${api.giret.loan.base-url}")
public interface LoanClient {

    @GetMapping(value = "/api/findAllLoan",produces = "application/json")
    List<Loan> findAllLoan();

    @PutMapping(value = "/api/updateLoanByState/{state}/{id}",produces = "application/json")
    Loan updateLoanByState(@PathVariable ("state")String state,@PathVariable ("id")Long id);


}
