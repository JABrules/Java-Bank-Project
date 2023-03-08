use bankproject;

delete from transactions where transactionID > 0;
delete from bankaccounts where accountID > 0;
delete from users where userID > 0;

insert into users (userID, username, userpassword) values
	(1, 'user', 'password'),
    (2, 'admin', '1234');

insert into bankaccounts (accountID, userID, accountname, accountbalance) values
	(1, 1, 'savings', 10000.00),
    (2, 1, 'current', 200.00),
    (3, 2, 'savings2', 300.00),
    (4, 2, 'current2', 400.00);
    
insert into transactions (transactionID, userID, fromaccountID, toaccountID, transferamount, transferdate) values
	(1, 1, 1, 2, 100.00, '2023-01-03'),
	(2, 1, 2, 1, 200.00, '2023-02-05'),
	(3, 2, 3, 4, 300.00, '2023-03-04'),
	(4, 2, 4, 3, 400.00, '2023-03-07');

select * from users;
select * from bankaccounts;
select * from transactions;