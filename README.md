# Nile Dot Com E-Store Simulation

Nile Dot Com is a Java-based GUI application that simulates an online e-commerce store. This application allows users to search for items from an inventory, add selected items to a shopping cart, and proceed to checkout. The application calculates subtotals, applies quantity-based discounts, computes tax, and generates a final invoice for the user. Additionally, the application logs each transaction in a CSV file for record-keeping.

The application is designed with a user-friendly interface using JavaFX, and it is built and managed using Maven. It is an educational project to practice event-driven programming and simulate a basic enterprise application.

## Technologies Used

- **Java**: The primary programming language used to develop the application.
- **JavaFX**: Used for creating the graphical user interface (GUI). Key components include:
  - `Application`, `Stage`, `Scene`: For setting up the main application window.
  - `VBox`, `HBox`, `Pane`: For layout management.
  - `Button`, `Label`, `TextField`, `TextArea`: For user interaction.
  - `Alert`: For dialog boxes and user notifications.
  - `DropShadow`: For visual effects on GUI elements.
- **Maven**: Project and dependency management tool. Maven handles the inclusion of external libraries, such as JavaFX, and manages the build process.
- **Java I/O (Input/Output)**: Used for file handling, specifically writing transaction data to a CSV file (`transactions.csv`).
- **Java Time API**: Used for handling date and time, particularly for generating timestamps for transactions (`ZonedDateTime`, `DateTimeFormatter`).
- **IntelliJ IDEA**: The Integrated Development Environment (IDE) used for writing, testing, and debugging the code. Maven integration within IntelliJ was used to manage dependencies and build the project.

## How to Run the Code

### Prerequisites

1. **Java Development Kit (JDK)**:
   - Ensure that JDK 8 or higher is installed on your system. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-downloads.html) or install it via your package manager (e.g., `brew install openjdk` on macOS).

2. **Maven**:
   - Maven is required to manage dependencies and build the project. Maven is often bundled with IDEs like IntelliJ IDEA, but you can also install it manually. Check if Maven is installed by running `mvn -v` in your terminal. If not installed, follow the instructions [here](https://maven.apache.org/install.html).

### Steps to Run the Project

1. **Clone or Download the Project**:
   - Download the project files or clone the repository to your local machine.

2. **Import the Project into Your IDE**:
   - Open your IDE (e.g., IntelliJ IDEA, Eclipse).
   - Choose "Open Project" or "Import Project" and select the folder containing the project files.
   - If you're using IntelliJ IDEA, it should automatically detect the Maven project and import it accordingly.

3. **Build the Project with Maven**:
   - In your IDE, run the Maven build process:
     - In IntelliJ IDEA, this can be done via the Maven tool window (`View -> Tool Windows -> Maven`) or by right-clicking the `pom.xml` file and selecting `Run Maven -> clean install`.
   - This step will download any necessary dependencies and compile the project.

4. **Run the Application**:
   - Once the project is built, locate the `NileDotCom` class in the project explorer.
   - Right-click on `NileDotCom` and select `Run 'NileDotCom.main()'` to start the application.

5. **Using the Application**:
   - The application window will open, allowing you to:
     - Search for items by their ID.
     - Add items to the shopping cart.
     - View the current cart with itemized details.
     - Proceed to checkout to generate an invoice and log the transaction.

### Additional Notes

- **File Locations**: 
  - The `transactions.csv` file, which logs each transaction, will be generated in the project’s root directory.
  - Ensure that any resource files (e.g., images, inventory CSV files) are located in the appropriate directory and accessible in the classpath.
- **Customization**:
  - The tax rate, discount rules, and other parameters can be customized in the code for different scenarios or educational purposes.

## Troubleshooting

- **Dependency Issues**:
  - If Maven fails to resolve dependencies, ensure that your internet connection is stable and that your `pom.xml` file is correctly configured.
  - Try running `mvn clean install` from the terminal to manually force a rebuild.
- **JavaFX Issues**:
  - If you encounter issues related to JavaFX, ensure that your IDE and Maven are correctly set up to include JavaFX. You might need to configure the `--module-path` and `--add-modules` arguments if running from the command line.

---

This `README.md` should provide a clear and detailed guide for anyone who needs to understand, build, and run your project. It includes all the essential information and instructions to make the project accessible and easy to use.
