# Imports and Modules
from PyQt6.QtWidgets import QPushButton, QWidget, QLabel, QVBoxLayout, QStackedLayout, QHBoxLayout, QInputDialog, QScrollArea, QMessageBox, QApplication, QColorDialog, QLineEdit, QFormLayout, QSizePolicy
from PyQt6.QtCore import Qt, QRect

# Local Classes
from inventory_database import InventoryDatabase
from login import Login
from settings import Settings

# Main Class
class Home(QWidget):
    
    # Constructor (Load up application and do first time setups)
    def __init__(self) -> None:
        super().__init__()
        self.login = Login()
        self.inventory = InventoryDatabase()
        self.initUI()
        self.clickEvents()
    
    # UI Objects and Design
    def initUI(self) -> None:
        # Main window container for widgets
        self.first_page = QWidget()
        ### Login layout ###
        self.login_view = QFormLayout(self.first_page)
        # Username input box
        self.username_input = QLineEdit()
        # Password input box
        self.password_input = QLineEdit()
        self.password_input.setEchoMode(QLineEdit.EchoMode.Password)
        # Login button
        self.login_button = QPushButton("Login")
        # Register button
        self.register_button = QPushButton("Register")
        # Login view
        self.login_view.addRow("Username", self.username_input)
        self.login_view.addRow("Password", self.password_input)
        self.login_view.addRow(self.register_button, self.login_button)

        ### Main layout ###
        self.second_page = QWidget()
        self.main_view = QVBoxLayout(self.second_page)
        self.top_bar = QHBoxLayout()
        self.inventory_area = QVBoxLayout()
        self.inventory_area.setAlignment(Qt.AlignmentFlag.AlignTop)
        self.button_area = QVBoxLayout()

        self.user_session = ""

        # Main page widgets
        self.add_item_button = QPushButton("Add Item")
        self.top_bar.addWidget(QLabel("Current Inventory: "))
        self.button_area.addWidget(self.add_item_button, alignment = Qt.AlignmentFlag.AlignBottom)

        self.main_view.addLayout(self.top_bar, 10)
        self.main_view.addLayout(self.inventory_area, 80)
        self.main_view.addLayout(self.button_area, 10)

        # Stacked layout to hold both layouts
        self.stackedLayout = QStackedLayout()
        self.stackedLayout.addWidget(self.first_page)
        self.stackedLayout.addWidget(self.second_page)

        # Display login layout and name window
        self.setWindowTitle("Inventory Tracker by Ethan Eckert")
        self.setLayout(self.stackedLayout)

    # Helper function for clearing the window
    def clearView(self, layout) -> None:
        while layout.count():
            child = layout.takeAt(0)
            if child.widget():
                child.widget().deleteLater()
    
    # Handle button click events
    def clickEvents(self) -> None:
        self.login_button.clicked.connect(self.loginButton)
        self.register_button.clicked.connect(self.registerButton)
        self.add_item_button.clicked.connect(self.addItemButton)
    
    # Button event for attempting to login
    def loginButton(self) -> None:
        username = self.username_input.text()
        password = self.password_input.text()

        valid = self.login.login(username, password)

        if valid:
            # SEND TO INVENTORY PAGE
            self.clearView(self.login_view)
            # SET USER_SESSION SO WE KNOW WHO IS USING THE APP
            self.user_session = username
            
            # DISPLAY MAIN VIEW
            self.viewList()
            self.stackedLayout.setCurrentIndex(1)
            self.resize(600, 400)
            print("Login successful.")

    # Button event for attempting to register
    def registerButton(self) -> None:
        # Grab the entered username/password
        username = self.username_input.text()
        password = self.password_input.text()

        valid = self.login.register(username, password)

        if valid:
            # NOTIFY USER OF SUCCESS
            print("Registration successful.")
    
    def addItemButton(self) -> None:
        # Dialog box opens to get name and quantity
        name_dialog = QInputDialog()
        name_dialog.setWindowTitle("Add Item")
        name, done = name_dialog.getText(self, "Add Item", "Item Name:")
        
        # Ensure something was entered and the user continued
        if name and done:
            quantity_dialog = QInputDialog()
            quantity, done2 = quantity_dialog.getInt(self, "Quantity", "Quantity:")

            if quantity and done2:
                self.inventory.create(name, quantity, self.user_session)
                self.viewList() # Reload the list view
        else:
            # Display error
            print("Nothing entered")
    
    def viewList(self) -> None:
        # Clear the view for new items
        self.clearView(self.inventory_area)
        # Get all items based on the user logged in
        print(self.user_session)
        items = self.inventory.read(self.user_session)

        # Loop through and create card items
        for item in items:
            # Create a card widget to display inventory items
            # Index 0 is user, Index 1 is item name, Index 2 is quantity
            card = QWidget()
            card.setMaximumHeight(50)
            name = item[1]
            quantity = item[2]

            # Buttons for editing
            button = QPushButton("Edit Quantity", clicked = lambda checked, arg = name, arg2 = self.user_session : self.updateEntry(arg, arg2))
            button2 = QPushButton("X", clicked = lambda checked, arg = name, arg2 = self.user_session : self.deleteEntry(arg, arg2))
            button2.setStyleSheet("background-color: #f44336")

            # Create the card layout
            layout = QHBoxLayout()
            layout.addWidget(QLabel(f"{name}: {quantity}"))
            layout.addWidget(button, alignment=Qt.AlignmentFlag.AlignRight)
            layout.addWidget(button2, alignment=Qt.AlignmentFlag.AlignRight)
            card.setLayout(layout)
            self.inventory_area.addWidget(card)
    
    def deleteEntry(self, item_name, username) -> None:
        # Delete the item from the database and refresh our list
        self.inventory.delete(item_name, username)
        self.viewList()
    
    def updateEntry(self, item_name, username) -> None:
        # Create a dialog box to get a new quantity, then refresh the list
        quantity_dialog = QInputDialog()
        quantity_dialog.setWindowTitle("New Quantity")
        quantity, done = quantity_dialog.getInt(self, "Quantity", "New Quantity:")

        if done:
            self.inventory.update(item_name, username, quantity)
            self.viewList()

    
# Call and run our main function
if __name__ in "__main__":
    app = QApplication([])
    main = Home()
    main.show()
    app.exec()
