package com.jab.bankproject;

import com.jab.bankproject.dao.BankAccountDao;
import com.jab.bankproject.dao.TransactionDao;
import com.jab.bankproject.dao.UserDao;
import com.jab.bankproject.entities.BankAccount;
import com.jab.bankproject.entities.Transaction;
import com.jab.bankproject.entities.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class BankprojectApplicationTests {

	@Autowired
	UserDao userDao;

	@Autowired
	BankAccountDao bankAccountDao;

	@Autowired
	TransactionDao transactionDao;

	@BeforeAll
	public static void setUpClass() {
	}

	@AfterAll
	public static void tearDownClass() {
	}

	@BeforeEach
	public void setUp() {
		List<User> users = userDao.getAllUsers();
		for(User user : users) {
			userDao.deleteUserByID(user.getId());
		}

		List<BankAccount> bankAccounts = bankAccountDao.getAllBankAccounts();
		for(BankAccount bankAccount : bankAccounts) {
			bankAccountDao.deleteBankAccountByID(bankAccount.getId());
		}

		List<Transaction> transactions = transactionDao.getAllTransactions();
		for(Transaction transaction : transactions) {
			transactionDao.deleteTransactionByID(transaction.getId());
		}
	}

	@AfterEach
	public void tearDown() {
	}

	@Test
	public void testAddGetUser() {
		User user = new User(-1, "test", "testword");
		userDao.addUser(user);
		User userTest = userDao.getUserByID(user.getId());
		assertEquals(user, userTest);
	}

	@Test
	public void testGetAllUsers() {
		User user1 = new User(-1, "test1", "testword");
		userDao.addUser(user1);
		User user2 = new User(-1, "test2", "testword");
		userDao.addUser(user2);

		List<User> users = userDao.getAllUsers();
		assertEquals(2, users.size());
		assertTrue(users.contains(user1));
		assertTrue(users.contains(user2));

		List<User> usersTest2 = userDao.getUsersByNameAndPassword("test1", "testword");
		assertEquals(1, usersTest2.size());
		assertTrue(usersTest2.contains(user1));
		assertFalse(usersTest2.contains(user2));
	}

	@Test
	public void testDeleteUser() {
		User user = new User(-1, "test", "testword");
		userDao.addUser(user);
		BankAccount bankAccount1 = new BankAccount(-1, user.getId(), "testaccount1", 100.0f);
		bankAccountDao.addBankAccount(bankAccount1);
		BankAccount bankAccount2 = new BankAccount(-1, user.getId(), "testaccount2", 100.0f);
		bankAccountDao.addBankAccount(bankAccount2);
		Transaction transaction = new Transaction(-1, user.getId(), bankAccount1.getId(), bankAccount2.getId(), 10.0f, new Date());
		transactionDao.addTransaction(transaction);

		userDao.deleteUserByID(user.getId());
		List<Transaction> transactions = transactionDao.getTransactionsByUserID(user.getId());
		assertEquals(0, transactions.size());
		List<BankAccount> bankAccounts = bankAccountDao.getBankAccountsByUserID(user.getId());
		assertEquals(0, bankAccounts.size());
		assertNull(userDao.getUserByID(user.getId()));
	}

	@Test
	public void testAddGetBankAccount() {
		User user = new User(-1, "test", "testword");
		userDao.addUser(user);
		BankAccount bankAccount = new BankAccount(-1, user.getId(), "testname", 500.0f);
		bankAccountDao.addBankAccount(bankAccount);
		BankAccount bankAccountTest = bankAccountDao.getBankAccountByID(bankAccount.getId());
		assertEquals(bankAccount, bankAccountTest);
	}

	@Test
	public void testGetAllBankAccounts() {
		User user1 = new User(-1, "test1", "testword");
		userDao.addUser(user1);
		User user2 = new User(-1, "test2", "testword");
		userDao.addUser(user2);

		BankAccount bankAccount1 = new BankAccount(-1, user1.getId(), "testname1", 500.0f);
		bankAccountDao.addBankAccount(bankAccount1);
		BankAccount bankAccount2 = new BankAccount(-1, user1.getId(), "testname2", 1000.0f);
		bankAccountDao.addBankAccount(bankAccount2);
		BankAccount bankAccount3 = new BankAccount(-1, user2.getId(), "testname3", 0.0f);
		bankAccountDao.addBankAccount(bankAccount3);

		List<BankAccount> bankAccountsTest1 = bankAccountDao.getAllBankAccounts();
		assertEquals(3, bankAccountsTest1.size());
		assertTrue(bankAccountsTest1.contains(bankAccount1));
		assertTrue(bankAccountsTest1.contains(bankAccount2));
		assertTrue(bankAccountsTest1.contains(bankAccount3));

		List<BankAccount> bankAccountsTest2 = bankAccountDao.getBankAccountsByUserID(user1.getId());
		assertEquals(2, bankAccountsTest2.size());
		assertTrue(bankAccountsTest2.contains(bankAccount1));
		assertTrue(bankAccountsTest2.contains(bankAccount2));
		assertFalse(bankAccountsTest2.contains(bankAccount3));
	}

	@Test
	public void testUpdateFunds() {
		User user = new User(-1, "test", "testword");
		userDao.addUser(user);
		BankAccount bankAccount = new BankAccount(-1, user.getId(), "testname", 500.0f);
		bankAccountDao.addBankAccount(bankAccount);
		bankAccountDao.updateFunds(1000.0f, bankAccount.getId());
		BankAccount bankAccountTest = bankAccountDao.getBankAccountByID(bankAccount.getId());
		assertEquals(1000.0f, bankAccountTest.getAccountBalance());
	}

	@Test
	public void testDeleteBankAccount() {
		User user = new User(-1, "test", "testword");
		userDao.addUser(user);
		BankAccount bankAccount1 = new BankAccount(-1, user.getId(), "testaccount1", 100.0f);
		bankAccountDao.addBankAccount(bankAccount1);
		BankAccount bankAccount2 = new BankAccount(-1, user.getId(), "testaccount2", 100.0f);
		bankAccountDao.addBankAccount(bankAccount2);
		Transaction transaction = new Transaction(-1, user.getId(), bankAccount1.getId(), bankAccount2.getId(), 10.0f, new Date());
		transactionDao.addTransaction(transaction);

		bankAccountDao.deleteBankAccountByID(bankAccount1.getId());
		List<Transaction> transactions = transactionDao.getTransactionsByUserID(user.getId());
		assertEquals(0, transactions.size());
		List<BankAccount> bankAccounts = bankAccountDao.getBankAccountsByUserID(user.getId());
		assertEquals(1, bankAccounts.size());
		assertFalse(bankAccounts.contains(bankAccount1));
	}

	@Test
	public void testAddGetTransaction() {
		User user = new User(-1, "test", "testword");
		userDao.addUser(user);
		BankAccount bankAccount1 = new BankAccount(-1, user.getId(), "testaccount1", 100.0f);
		bankAccountDao.addBankAccount(bankAccount1);
		BankAccount bankAccount2 = new BankAccount(-1, user.getId(), "testaccount2", 100.0f);
		bankAccountDao.addBankAccount(bankAccount2);
		Transaction transaction = new Transaction(-1, user.getId(), bankAccount1.getId(), bankAccount2.getId(), 10.0f, new Date());
		transactionDao.addTransaction(transaction);

		Transaction transactionTest = transactionDao.getTransactionByID(transaction.getId());
		assertEquals(transactionTest, transaction);
	}

	@Test
	public void testGetAllTransactions() {
		User user1 = new User(-1, "test1", "testword");
		userDao.addUser(user1);
		User user2 = new User(-1, "test2", "testword");
		userDao.addUser(user2);

		BankAccount bankAccount1 = new BankAccount(-1, user1.getId(), "testname1", 500.0f);
		bankAccountDao.addBankAccount(bankAccount1);
		BankAccount bankAccount2 = new BankAccount(-1, user1.getId(), "testname2", 1000.0f);
		bankAccountDao.addBankAccount(bankAccount2);
		BankAccount bankAccount3 = new BankAccount(-1, user2.getId(), "testname3", 0.0f);
		bankAccountDao.addBankAccount(bankAccount3);
		BankAccount bankAccount4 = new BankAccount(-1, user2.getId(), "testname4", 10.0f);
		bankAccountDao.addBankAccount(bankAccount4);

		Transaction transaction1 = new Transaction(-1, user1.getId(), bankAccount1.getId(), bankAccount2.getId(), 10.0f, new Date());
		transactionDao.addTransaction(transaction1);
		Transaction transaction2 = new Transaction(-1, user1.getId(), bankAccount2.getId(), bankAccount1.getId(), 20.0f, new Date());
		transactionDao.addTransaction(transaction2);
		Transaction transaction3 = new Transaction(-1, user2.getId(), bankAccount3.getId(), bankAccount3.getId(), 30.0f, new Date());
		transactionDao.addTransaction(transaction3);


		List<Transaction> transactionsTest1 = transactionDao.getAllTransactions();
		assertEquals(3, transactionsTest1.size());
		assertTrue(transactionsTest1.contains(transaction1));
		assertTrue(transactionsTest1.contains(transaction2));
		assertTrue(transactionsTest1.contains(transaction3));

		List<Transaction> transactionsTest2 = transactionDao.getTransactionsByUserID(user1.getId());
		assertEquals(2, transactionsTest2.size());
		assertTrue(transactionsTest2.contains(transaction1));
		assertTrue(transactionsTest2.contains(transaction2));
		assertFalse(transactionsTest2.contains(transaction3));
	}

	@Test
	public void testDeleteTransactions() {
		User user = new User(-1, "test", "testword");
		userDao.addUser(user);
		BankAccount bankAccount1 = new BankAccount(-1, user.getId(), "testaccount1", 100.0f);
		bankAccountDao.addBankAccount(bankAccount1);
		BankAccount bankAccount2 = new BankAccount(-1, user.getId(), "testaccount2", 100.0f);
		bankAccountDao.addBankAccount(bankAccount2);
		Transaction transaction1 = new Transaction(-1, user.getId(), bankAccount1.getId(), bankAccount2.getId(), 10.0f, new Date());
		transactionDao.addTransaction(transaction1);
		Transaction transaction2 = new Transaction(-1, user.getId(), bankAccount2.getId(), bankAccount1.getId(), 20.0f, new Date());
		transactionDao.addTransaction(transaction2);

		transactionDao.deleteTransactionByID(transaction1.getId());
		List<Transaction> transactions = transactionDao.getAllTransactions();
		assertEquals(1, transactions.size());
		assertFalse(transactions.contains(transaction1));
	}
}
