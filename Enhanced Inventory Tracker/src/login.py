from login_database import LoginDatabase
from argon2 import PasswordHasher, exceptions

# This class is responsible for authenticating users
class Login():

    # Constructor
    def __init__(self) -> None:
        self.db = LoginDatabase()

    # Login and Authentication
    def login(self, user : str, password : str) -> bool:
        # IF USER EXISTS GET HASHED PASSWORD
        if self.db.read(user):
            hash = self.db.read(user)[1]
            ph = PasswordHasher()
            
            # VERIFY PASSWORD AGAINST HASH
            try:
                ph.verify(hash, password)
            except exceptions.VerifyMismatchError:
                print("Password is invalid.")
                return False
            
            # PASSING THE TRY CLAUS ENSURES VALID PASSWORD
            return True

        return False
    
    # Connect to the database and check if the hashed passwords match, returns a boolean to check
    def register(self, user: str, password : str) -> bool:
        # IF USER EXISTS REJECT
        if self.db.read(user):
            print("Account already exists.")
            return False
        else:
            # HASH PASSWORD FOR STORAGE
            ph = PasswordHasher()
            hash = ph.hash(password)
            # ADD USER/PASS TO DATABASE
            self.db.create(user, hash)
            return True
