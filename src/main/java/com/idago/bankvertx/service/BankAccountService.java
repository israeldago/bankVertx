/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.idago.bankvertx.service;

import com.idago.bankvertx.dao.AccountDAO;
import com.idago.bankvertx.dao.AppUserDAO;
import com.idago.bankvertx.dao.exceptions.NonexistentEntityException;
import com.idago.bankvertx.entities.db.AccountDB;
import com.idago.bankvertx.entities.db.AppUserDB;
import com.idago.bankvertx.entities.dto.AccountDTO;
import com.idago.bankvertx.entities.dto.AppUserDTO;
import com.idago.bankvertx.entities.dto.ResponseDTO;
import com.idago.bankvertx.entities.dto.RoleDTO;
import com.idago.bankvertx.entities.dto.TransactionDTO;
import static com.idago.bankvertx.entities.dto.enums.TransactionType.DEPOSIT;
import static com.idago.bankvertx.entities.dto.enums.TransactionType.TRANSFER;
import static com.idago.bankvertx.entities.dto.enums.TransactionType.WITHDRAW;
import com.idago.bankvertx.service.gateway.AccountService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Israel Dago at https://github.com/ivoireNoire
 */
public class BankAccountService implements AccountService {

    private final AccountDAO accountDAO;
    private final AppUserDAO appUserDAO;
    private final Random random;

    public BankAccountService() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.idago_bankVertx_jar_1.0-SNAPSHOTPU");
        EntityManager entityManager = emf.createEntityManager();
        this.accountDAO = new AccountDAO(entityManager);
        this.appUserDAO = new AppUserDAO(entityManager);
        this.random = new Random();
    }

    @Override
    public ResponseDTO createClientBankAccount(AccountDTO newAccount) {
        AppUserDB accountClient = appUserDAO.findAppUserDB(newAccount.getHolder().getId());
        ResponseDTO finalResp = new ResponseDTO();
        if (accountClient == null) {
            return finalResp.setRequestDone(false).setMessage("Client does not exist in System");
        } else {
            AccountDB accountDB = new AccountDB().setIban("RO" + random.nextInt(100000) + accountClient.getLastName().toUpperCase() + random.nextInt(1000000))
                    .setAmount(newAccount.getAmount())
                    .setCreationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .setHolder(accountClient);
            accountDAO.create(accountDB);
            return finalResp.setRequestDone(true).setMessage("new account created and attribued to client " + accountClient.getIdentityCardNumber());
        }
    }

    @Override
    public ResponseDTO deleteClientBankAccount(Integer accountID) {
        AccountDB accountDB = accountDAO.findAccountDB(accountID);
        ResponseDTO finalResp = new ResponseDTO();
        if (accountDB != null) {
            try {
                accountDAO.destroy(accountDB.getId());
            } catch (NonexistentEntityException ex) {
                return finalResp.setRequestDone(false).setMessage(ex.getMessage());
            }
            return finalResp.setRequestDone(true);
        } else {
            return finalResp.setRequestDone(false).setMessage("this account does not exist in System");
        }
    }

    @Override
    public ResponseDTO makeABankOperation(TransactionDTO transactionDTO) {
        ResponseDTO finalResp = null;
        AccountDB accountToCredit = accountDAO.findAccountDB(transactionDTO.getDepositAccout_ID());
        AccountDB accountToDebit = accountDAO.findAccountDB(transactionDTO.getWithdrawAccount_ID());

        switch (transactionDTO.getTransactionType()) {
            case DEPOSIT:
                finalResp = creditAccount(transactionDTO.getAmount(), accountToCredit);
                break;
            case WITHDRAW:
                finalResp = debitAccount(transactionDTO.getAmount(), accountToDebit);
                break;
            case TRANSFER:
                finalResp = transferMoney(transactionDTO.getAmount(), accountToDebit, accountToCredit);
                break;
        }
        return finalResp;
    }

    @Override
    public Stream<AccountDTO> allAccounts() {
        return accountDAO.findAll().map(this::toDTO);
    }

    /////Private Methods       
    private ResponseDTO creditAccount(Double amount, AccountDB accountToCredit) {
        try {
            accountToCredit.setAmount(accountToCredit.getAmount() + amount);
            accountDAO.edit(accountToCredit);
        } catch (NonexistentEntityException ex) {
            return new ResponseDTO().setRequestDone(false).setMessage(ex.getMessage());
        }
        return new ResponseDTO().setRequestDone(true);
    }

    private ResponseDTO debitAccount(Double amount, AccountDB accountToDebit) {
        try {
            if (accountToDebit.getAmount() >= amount) {
                return new ResponseDTO().setRequestDone(false).setMessage("Request failed => insufficient funds");
            }
            accountToDebit.setAmount(accountToDebit.getAmount() - amount);
            accountDAO.edit(accountToDebit);
            return new ResponseDTO().setRequestDone(true);
        } catch (NonexistentEntityException ex) {
            return new ResponseDTO().setRequestDone(false).setMessage(ex.getMessage());
        }

    }

    private ResponseDTO transferMoney(Double account, AccountDB accountToDebit, AccountDB accountToCredit) {
        ResponseDTO debitAccountResponse = debitAccount(account, accountToDebit);
        if (debitAccountResponse.isRequestDone()) {
            ResponseDTO creditAccountResponse = creditAccount(account, accountToCredit);
            if (creditAccountResponse.isRequestDone()) {
                return new ResponseDTO().setRequestDone(true);
            } else {
                return creditAccountResponse;
            }
        } else {
            return debitAccountResponse;
        }
    }

    private AccountDTO toDTO(AccountDB db) {
        return new AccountDTO(db.getId(), db.getIban(), db.getAmount(), db.getCreationDate(),
                new AppUserDTO(db.getHolder().getId(), db.getHolder().getLastName(),
                        db.getHolder().getFirstName(), db.getHolder().getIdentityCardNumber(), db.getHolder().getBirthDate(),
                        db.getHolder().getRegisterDate(), db.getHolder().getUsername(), db.getHolder().getPassword(),
                        new RoleDTO(db.getHolder().getRole().getId(), db.getHolder().getRole().getRoleName())));
    }

    ///////////SINGLETON//////////////////
    private static class SingletonHolder {
        private final static BankAccountService SINGLETON = new BankAccountService();
    }

    public static BankAccountService getInstance() {
        return SingletonHolder.SINGLETON;
    }
}
