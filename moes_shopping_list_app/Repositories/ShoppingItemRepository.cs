using Dapper;
using moes_shopping_list_app.Data;
using moes_shopping_list_app.Models;

namespace moes_shopping_list_app.Repositories
{
    public class ShoppingItemRepository
    {
        // Private readonly field to store the database context
        private readonly ShoppingListDbContext _context;

        // Constructor to initialize the repository with a database context
        public ShoppingItemRepository(ShoppingListDbContext context)
        {
            // Assign the provided context to the private field
            _context = context;
        }

        // Public method to retrieve all shopping items from the database
        public async Task<IEnumerable<ShoppingItem>> GetAllShoppingItems()
        {
            // Create a new database connection using the context
            using var connection = _context.GetConnection();
            // Execute a SQL query to select all shopping items and return the result as a list of ShoppingItem objects
            return await connection.QueryAsync<ShoppingItem>("SELECT * FROM ShoppingItems");
        }

        // Public method to add a new shopping item to the database
        public async Task AddShoppingItem(ShoppingItem item)
        {
            // Create a new database connection using the context
            using var connection = _context.GetConnection();
            // Execute a SQL query to insert a new shopping item into the database, using the provided item's properties
            await connection.ExecuteAsync("INSERT INTO ShoppingItems (Name, Quantity) VALUES (@Name, @Quantity)", item);
        }

        // Public method to update an existing shopping item in the database
        public async Task UpdateShoppingItem(ShoppingItem item)
        {
            // Create a new database connection using the context
            using var connection = _context.GetConnection();
            // Execute a SQL query to update an existing shopping item in the database, using the provided item's properties
            await connection.ExecuteAsync("UPDATE ShoppingItems SET Name = @Name, Quantity = @Quantity WHERE Id = @Id", item);
        }

        // Public method to delete a shopping item from the database by its ID
        public async Task DeleteShoppingItem(int id)
        {
            // Create a new database connection using the context
            using var connection = _context.GetConnection();
            // Execute a SQL query to delete a shopping item from the database, using the provided ID
            await connection.ExecuteAsync("DELETE FROM ShoppingItems WHERE Id = @Id", new { Id = id });
        }
    }
}