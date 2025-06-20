using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using moes_shopping_list_app.Models;
using moes_shopping_list_app.Repositories;

namespace moes_shopping_list_app.ViewModels
{
    public partial class ShoppingListViewModel : ObservableObject
    {
        // Private field to hold the repository instance for data operations
        private readonly ShoppingItemRepository _repository;

        // Observable property for the collection of shopping items
        [ObservableProperty]
        private ObservableCollection<ShoppingItem> _shoppingItems = [];

        // Observable property for the name of the new shopping item
        [ObservableProperty]
        private string _newItemName = string.Empty;

        // Observable property for the quantity of the new shopping item
        [ObservableProperty]
        private int _newItemQuantity = 1;

        // Constructor that initializes the ViewModel with a repository and loads existing shopping items
        public ShoppingListViewModel(ShoppingItemRepository repository)
        {
            _repository = repository; // Assigning the repository to the private field
            LoadShoppingItemsCommand.Execute(null); // Executing the command to load shopping items
        }

        // Command to load shopping items asynchronously
        [RelayCommand]
        private async Task LoadShoppingItems()
        {
            // Fetching all shopping items from the repository
            var items = await _repository.GetAllShoppingItems();
            // Updating the ObservableCollection with the fetched items
            ShoppingItems.Clear(); // Clear existing items
            foreach (var item in items)
            {
                ShoppingItems.Add(item); // Add each fetched item to the collection
            }
        }

        // Command to add a new shopping item asynchronously
        [RelayCommand]
        private async Task AddShoppingItem()
        {
            // Validating the new item name and quantity
            if (string.IsNullOrWhiteSpace(NewItemName) || NewItemQuantity <= 0)
            {
                // ToDo: Implement error message
                return; // Exiting the method if validation fails
            }

            // Creating a new shopping item with the provided name and quantity
            var newItem = new ShoppingItem
            {
                Name = NewItemName,
                Quantity = NewItemQuantity
            };

            // Adding the new item to the repository
            await _repository.AddShoppingItem(newItem);
            // Reloading the shopping items to reflect the changes
            await LoadShoppingItems();

            // Resetting the input fields for the new item
            NewItemName = string.Empty;
            NewItemQuantity = 1;
        }

        // Command to update an existing shopping item asynchronously
        [RelayCommand]
        private async Task UpdateShoppingItem(ShoppingItem item)
        {
            // Checking if the item is null
            if (item is null)
            {
                // ToDo: Implement error message
                return; // Exiting the method if the item is null
            }

            // Updating the item in the repository
            await _repository.UpdateShoppingItem(item);
            // Reloading the shopping items to reflect the changes
            await LoadShoppingItems();
        }

        // Command to delete a shopping item by its ID asynchronously
        [RelayCommand]
        private async Task DeleteShoppingItem(int id)
        {
            // Deleting the item from the repository using its ID
            await _repository.DeleteShoppingItem(id);
            // Reloading the shopping items to reflect the changes
            await LoadShoppingItems();
        }
    }
}