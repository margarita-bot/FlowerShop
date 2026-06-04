package ru.nosova.flowershop.dao.impl;

import ru.nosova.flowershop.model.Client;
import java.util.List;

public interface ClientDaoImpl<T, ID>{
    /**
     * Метод получает список клиентов
     * @return список клиентов
     */
    List<Client> getClient();

    /**
     * Метод поиска клиентов по параметрам
     * @param name имя клиента
     * @param lastname фамилия клиента
     * @param phone номер телефона клиента
     * @return список найденных клиентов
     */
    List<Client> findClient(String name, String lastname, String phone);

    /**
     * Метод добавления клиента
     * @param client создаваемый объект
     * @return созданного клиента
     */
    Client insert(Client client);

    /**
     * Метод обновления клиента
     * @param client конкретный клиент
     * @return измененого клиента
     */
    Client update (Client client);

    /**
     * Метод удаления клиента
     * @param id клиента
     */
    void deleteId (ID id);

    /**
     * Метод удаления клиента
     * @param client конкретный клиент
     */
    void delete(Client client);
}
