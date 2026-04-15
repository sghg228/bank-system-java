# Bank System

Simple console banking project written in Java.

I made this project to practice Java Core. This system does not claim to be a real banking system. It was simply created to test my skills without going into the details of how real banking systems work and without using AI assistance

## Features

- create users
- create bank accounts
- deposit money
- withdraw money
- transfer money between accounts
- return operation results with `Result<T>`
- save users and accounts to files
- async file saving with `ExecutorService`
- audit logging for main operations

## Project structure

- `model` — classes like `User`, `Account`, `Result`
- `service` — main bank logic
- `storage` — saving and loading data from files
- `logging` — audit logger
- `Main` — console menu

## Future improvements
- Replace file storage with database
- Change the transaction logic
- Make money a separate entity
- Add support for multiple currencies and currency conversion

