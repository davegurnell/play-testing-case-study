# Play Testing Case Study

Copyright Dave Gurnell, 2019.

Licensed Apache 2.0.

## The Case Study

This project contains a web application representing
a simple piggy bank application. Users can:

- Create a bank account by specifying a user ID, password, and opening balance.
- Ask for the current balance of an account.
- Transfer money between two accounts.
- Apply interest to all accounts as a percentage multiplier.

Unfortunately, the developer we hired to write this code
quit before they finished implementing the spec.
Also they were a bit of a cowboy and only wrote one test!
It's your job to fix that!

Here are some requirements for the web application.
Some of them are implemented, some are not.
Implement tests for each requirement.
If the tests fail, fix the code!

As you work, think about your testing strategy,
the testability of the code, how you can improve it,
and how you can do so without breaking anything!
Use whatever tools/libraries you want.

- If a user attempts to read the balance of a non-existent account,
  the web service should return a 404.

- A user cannot read the balance of an account
  without specifying the correct password.

- A user can't open an account with a negative balance.

- A user can't open an account if the account ID already exists.

- A user cannot transfer money from account to account
  without specifying the source account's password.

- A user cannot transfer money from account to account
  if either account would go overdrawn

- A user can't apply a negative interest rate.

- Interest adjustments should apply to all accounts
  unless they are overdrawn.

- A user must specify their password to close their account.

- Closing an account distributes its balance evenly
  to the other accounts in the app.

At the end of the process (or after a fixed time),
you'll be asked to talk about your approach.
