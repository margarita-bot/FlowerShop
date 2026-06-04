package ru.nosova.flowershop.dao.impl;

import ru.nosova.flowershop.model.Orders;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface OrdersDaoImpl<T, ID> {
    List<Orders> find();
    Orders createOrders(Long clientID, Long floristId, Long bouquetId, Boolean delivery, Long addressId, Long statusId, Time deliveryTime);
    void delete (Orders orders);
    void deleteID (ID id);
    List<Orders> getOrders();
    List<Orders> find(String name, String lastname, String status, LocalDate date);
}
