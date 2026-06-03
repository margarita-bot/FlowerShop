package ru.nosova.flowershop.dao;

import ru.nosova.flowershop.dao.impl.ClientDaoImpl;
import ru.nosova.flowershop.model.Client;
import ru.nosova.flowershop.utils.DButils;
import ru.nosova.flowershop.utils.SQLStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements ClientDaoImpl<Client, Long>{
    private final SQLStatements sql = new SQLStatements();

    @Override
    public List<Client> getClient() {
        String query = sql.getQuery("client.select");
        List<Client> list = new ArrayList<>();
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Client client = Client.builder()
                        .client_id(rs.getLong("client_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .middleName(rs.getString("middle_name"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .build();
                list.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

        @Override
    public Client insert(Client client) {
        String query = sql.getQuery("client.insert");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query, new String[] {"client_id"})){
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getMiddleName());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getPhone());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                client.setClient_id(rs.getLong("client_id"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return client;
    }

    @Override
    public Client update(Client client) {
        String query = sql.getQuery("client.update");
        try(PreparedStatement statement = DButils.getConnection().prepareStatement(query)) {
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getMiddleName());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getPhone());
            statement.setLong(6, client.getClient_id());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    @Override
    public List<Client> findClient(String name, String lastname, String phone) {
        String query = sql.getQuery("client.find");
        List<Client> list = new ArrayList<>();

        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setString(1, name);
            statement.setString(2, lastname);
            statement.setString(3, phone);

            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Client client = Client.builder()
                        .client_id(rs.getLong("client_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .middleName(rs.getString("middle_name"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .build();
                list.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void delete(Client client) {
        deleteId(client.getClient_id());
    }
    @Override
    public void deleteId(Long aLong) {
        String query = sql.getQuery("client.delete");
        try (PreparedStatement statement = DButils.getConnection().prepareStatement(query)){
            statement.setInt(1, aLong.intValue());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
