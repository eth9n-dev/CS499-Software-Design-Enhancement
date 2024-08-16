import sqlite3

# This class is responsible for setting up, creating, and communicating with the inventory database
class LoginDatabase():

    # On initialization, we create the file if it does not exist, or open it if it already exists
    def __init__(self) -> None:
        self.con = sqlite3.connect("Enhanced Inventory Tracker/data/accounts.db")
        self.cur = self.con.cursor()
        print("Database connection initialized.")
        
        try:
            self.cur.execute("CREATE TABLE accounts(username, password, PRIMARY KEY (username))")
            print("Table created.")
        except Exception as er:
            print(str(er))
            return
    
    # Create an entry in the database
    def create(self, username : str, password : str) -> None:
        self.cur.execute("INSERT INTO accounts(username, password) VALUES (?, ?)", (username, password))
        self.con.commit()

        print("Account created.")
    
    # Read an entry from the database
    def read(self, username : str) -> str:
        self.cur.execute("SELECT username, password FROM accounts WHERE username = ?", (username, ))
        return self.cur.fetchone()
    