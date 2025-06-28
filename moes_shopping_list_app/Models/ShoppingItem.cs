namespace moes_shopping_list_app.Models
{
    // Class representing a shopping item in the application
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