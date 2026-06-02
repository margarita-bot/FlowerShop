package ru.nosova.flowershop.dao.impl;

import ru.nosova.flowershop.model.Client;
import java.util.List;

public interface ClientDaoImpl<T, ID>{
    List<Client> getClient();
    List<Client> findClient(String name, String lastname, String phone);
    Client insert(Client client);
    Client update (Client client);
    void deleteId (ID id);
    void delete(Client client);
}
