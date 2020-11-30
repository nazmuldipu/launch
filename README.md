// Disable all user login
UPDATE h_users SET account_non_expired=false WHERE account_non_expired=true; 

//Enable all user login
UPDATE h_users SET account_non_expired=true WHERE account_non_expired=false;

//show all activated user
SELECT * from h_users WHERE account_non_expired=true; 