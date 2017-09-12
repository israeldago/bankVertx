/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.service.gateway;

import com.idago.bankvertx.entities.dto.AccountDTO;
import com.idago.bankvertx.entities.dto.ResponseDTO;
import com.idago.bankvertx.entities.dto.TransactionDTO;
import java.util.stream.Stream;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public interface AccountService {
    public ResponseDTO createClientBankAccount(AccountDTO newAccount);
    public ResponseDTO deleteClientBankAccount(Integer accountID);
    public Stream<AccountDTO> allAccounts();
    public ResponseDTO makeABankOperation(TransactionDTO transactionDTO);
}
