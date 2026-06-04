package ru.nosova.flowershop.dao.impl;

import ru.nosova.flowershop.model.Address;
import java.util.List;

public interface AddressDaoImpl{
    List<Address> find();
    Address insert (Address address);
    List<Address> findAddress(String street, String house);
}
