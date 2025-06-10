// Define a namespace for the shopping list application models
namespace moes_shopping_list_app.Models
{
    // Define a class to represent an item in the shopping list
    public class ShoppingItem
    {
        // Property to store the unique identifier for the shopping item
        public int Id { get; set; }
        // Property to store the name of the shopping item
        // The 'required' keyword indicates that this property must be provided
        public required string Name { get; set; }
        // Property to store the quantity of the shopping item
        public int Quantity { get; set; }
    }
}