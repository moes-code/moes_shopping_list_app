// Importing necessary namespaces for SQLite and Dapper
using Microsoft.Data.Sqlite;
using Dapper;

namespace moes_shopping_list_app.Data
{
    // Class representing the database context for the shopping list application
    public class ShoppingListDbContext
    {
        // Private field to store the path to the SQLite database file
        private readonly string _dbPath;

        // Initializes a new instance of the ShoppingListDbContext class
        public ShoppingListDbContext(string dbPath)
        {
            // Initialize the _dbPath field with the provided database path
            _dbPath = dbPath;
        }

        // Gets a connection to the SQLite database
        public SqliteConnection GetConnection()
        {
            // Create a new connection to the database using the provided path
            return new SqliteConnection($"Data Source={_dbPath}");
        }

        // Creates the ShoppingItems table in the database if it does not already exist
        public void CreateTable()
        {
            // Get a connection to the database using the GetConnection method
            using var connection = GetConnection();
            // Execute a SQL query to create the ShoppingItems table
            connection.Execute(@"
                -- Create the ShoppingItems table with the specified columns
                CREATE TABLE IF NOT EXISTS ShoppingItems (
                    -- Unique identifier for each shopping item
                    Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    -- Name of the shopping item
                    Name TEXT NOT NULL,
                    -- Quantity of the shopping item
                    Quantity INTEGER NOT NULL
                );
            ");
        }
    }
}