import sqlite3

# This class is responsible for setting up, creating, and communicating with the inventory database
class InventoryDatabase():

    # On initialization, we create the file if it does not exist, or open it if it already exists
    def __init__(self) -> None:
        self.con = sqlite3.connect("./data/accounts.db")
        self.con.execute("PRAGMA foreign_keys = 1")
        self.cur = self.con.cursor()
        print("Inventory database connection initialized.")
        
        try:
            self.cur.execute("CREATE TABLE inventory(username, item, quantity, FOREIGN KEY (username) REFERENCES accounts(username))")
            print("Table created.")
        except Exception as er:
            print(str(er))
            return
    
    # Create an entry in the database
    def create(self, item, quantity, username) -> None:
        try:
            self.cur.execute("INSERT INTO inventory(username, item, quantity) VALUES (?, ?, ?)", (username, item, quantity))
            self.con.commit()
            print("Item created.")
        except Exception as er:
            print(str(er))
    
    # Read an entry from the database
    def read(self, username):
        try:
            self.cur.execute("SELECT * FROM inventory WHERE username = ?", (username,))
            return self.cur.fetchall()
        except Exception as er:
            print(str(er))
    
    # Update an entry in the database
    def update(self, item, username, quantity) -> bool:
        try:
            self.cur.execute("UPDATE inventory SET quantity = ? WHERE username = ? AND item = ?", (quantity, username, item))
            self.con.commit()
        except Exception as er:
            print(str(er))
    
    # Delete an entry from the database
    def delete(self, item, username) -> None:
        try:
            self.cur.execute("DELETE FROM inventory WHERE username = ? AND item = ?", (username, item))
            self.con.commit()
        except Exception as er:
            print(str(er))
    
