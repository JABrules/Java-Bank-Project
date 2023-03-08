package com.jab.bankproject.dao;

import com.jab.bankproject.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoDB implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<User> getAllUsers() {
        final String GET_ALL_USERS = "SELECT * FROM users";
        return jdbc.query(GET_ALL_USERS, new UserMapper());
    }

    @Override
    public List<User> getUsersByNameAndPassword(String username, String password) {
        final String GET_USERS_BY_DETAILS = "SELECT * FROM users WHERE username = ? AND userpassword = ?";
        return jdbc.query(GET_USERS_BY_DETAILS, new UserMapper(), username, password);
    }

    @Override
    public User getUserByID(int id) {
        try {
            final String GET_USER_BY_ID = "SELECT * FROM users WHERE userID = ?";
            return jdbc.queryForObject(GET_USER_BY_ID, new UserMapper(), id);
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    @Transactional
    public User addUser(User user) {
        final String INSERT_USER = "INSERT INTO users(username, userpassword) " +
                "VALUES(?,?)";
        jdbc.update(INSERT_USER,
                user.getUsername(),
                user.getPassword());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        user.setId(newId);
        return user;
    }

    @Override
    @Transactional
    public void deleteUserByID(int id) {
        final String DELETE_TRANSACTIONS = "DELETE FROM transactions WHERE userID = ?";
        jdbc.update(DELETE_TRANSACTIONS, id);

        final String DELETE_ACCOUNTS = "DELETE FROM bankaccounts WHERE userID = ?";
        jdbc.update(DELETE_ACCOUNTS, id);

        final String DELETE_USER = "DELETE FROM users WHERE userID = ?";
        jdbc.update(DELETE_USER, id);
    }

    protected static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(
                    rs.getInt("userID"),
                    rs.getString("username"),
                    rs.getString("userpassword")
            );
            return user;
        }
    }
}
