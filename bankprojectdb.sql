drop database if exists bankproject;
create database bankproject;
use bankproject;

create table users (
	userID int not null auto_increment,
    username varchar(100) not null,
    userpassword varchar(100) not null,
    constraint PK_userID primary key (userID)
);

create table bankaccounts (
	accountID int not null auto_increment,
    userID int not null,
    accountname varchar(100) not null,
    accountbalance decimal(10,2),
    constraint PK_accountID primary key (accountID),
    constraint FK_bankaccounts_users_userID foreign key (userID)
		references users (userID)
);

create table transactions (
	transactionID int not null auto_increment,
    userID int not null,
    fromaccountID int not null,
    toaccountID int not null,
    transferamount decimal(10,2) not null,
    transferdate date not null,
    constraint PK_transactionID primary key (transactionID),
    constraint FK_transactions_users_userID foreign key (userID)
		references users (userID),
    constraint FK_transactions_bankaccounts_fromaccountID foreign key (fromaccountID)
		references bankaccounts (accountID),
    constraint FK_transactions_bankaccounts_toaccountID foreign key (toaccountID)
		references bankaccounts (accountID)
);