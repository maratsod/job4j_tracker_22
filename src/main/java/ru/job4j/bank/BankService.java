package ru.job4j.bank;

import java.util.*;
import java.util.stream.Stream;

/**
 * Класс описывает работу банковской системы
 * @author MARAT
 * @version 1.0
 */
public class BankService {
    /**
     * Это поле содержит всех пользователей системы с привязанными к ним счетами
     * Хранение осуществляется в коллекции типа HashMap
     */

    private final Map<User, List<Account>> users = new HashMap<>();
    /**
     * Этот метод должен добавить пользователя в систему
     * В методе должна быть проверка, что такого пользователя еще нет в системе.
     * Если он есть, то нового добавлять не надо.
     * @param user, который добавляется в систему
     */
    public void addUser(User user) {
        users.putIfAbsent(user, new ArrayList<>());
    }
    /**
     * Метод должен добавить новый счет к пользователю
     * Первоначально пользователя нужно найти по паспорту.
     * Для этого нужно использовать метод findByPassport.
     * @param passport пользователя и account
     */
    public void addAccount(String passport, Account account) {
        Optional<User> user = this.findByPassport(passport);
        if (user.isPresent()) {
            List<Account> accounts = users.get(user.get());
            if (!accounts.contains(account)) {
                accounts.add(account);
            }
        }
    }
    /**
     * Метод ищет пользователя по номеру паспорта
     * @param passport номер, по которому будет поиск пользователя
     * @return возвращает пользователя или null
     */
    public Optional<User> findByPassport(String passport) {
        return users.keySet()
                .stream()
                .filter(s -> s.getPassport().equals(passport))
                .findFirst();
    }

    /**
     * Метод ищет счет пользователя по реквизитам
     * @param requisite пользователя и passport номер
     * @return возвращает account пользователя или null
     */
    public Optional<Account> findByRequisite(String passport, String requisite) {
        return   findByPassport(passport)
                .flatMap(u -> users.get(u)
                .stream()
                .filter(s -> s.getRequisite().equals(requisite))
                .findFirst());
    }

    /**
     * Метод предназначен для перечисления денег с одного счёта на другой счёт
     * @param srcPassport номер паспорта и srcRequisite реквизиты счета источника
     * destPassport номер паспорта и destRequisite реквизиты счета куда
     * нужно перечислить amount денег
     * @return false - если счёт не найден или не хватает денег
     */
    public boolean transferMoney(String srcPassport, String srcRequisite,
                                 String destPassport, String destRequisite, double amount) {
        Optional<Account> srcAccount = this.findByRequisite(srcPassport, srcRequisite);
        Optional<Account> destAccount = this.findByRequisite(destPassport, destRequisite);
        if (srcAccount.isEmpty() || srcAccount.get().getBalance() < amount
                || destAccount.isEmpty()) {
            return false;
        }

        else {
            srcAccount.get().setBalance(srcAccount.get().getBalance() - amount);
            destAccount.get().setBalance(destAccount.get().getBalance() + amount);
            return true;
        }
    }
}