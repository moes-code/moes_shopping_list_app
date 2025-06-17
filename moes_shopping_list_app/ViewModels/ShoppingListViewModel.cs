// Importing the ObservableCollection class for managing collections
using System.Collections.ObjectModel;
// Importing MVVM component model for property change notifications
using CommunityToolkit.Mvvm.ComponentModel;
// Importing MVVM input commands
using CommunityToolkit.Mvvm.Input;
// Importing models related to shopping items
using moes_shopping_list_app.Models;
// Importing repositories for data access
using moes_shopping_list_app.Repositories;

// Defining the namespace for the ViewModel
namespace moes_shopping_list_app.ViewModels
{
    // Partial class for ShoppingListViewModel, inheriting from ObservableObject to support property change notifications
    public partial class ShoppingListViewModel : ObservableObject
    {
        // Private field to hold the repository instance for accessing shopping items
        private readonly ShoppingItemRepository _repository;

        // Constructor that initializes the ViewModel with a repository instance
        public ShoppingListViewModel(ShoppingItemRepository repository)
        {
            _repository = repository; // Assigning the repository to the private field
            _shoppingItems = new ObservableCollection<ShoppingItem>(); // Initializing the shopping items collection
            LoadShoppingItemsCommand.Execute(null); // Executing the command to load existing shopping items
        }

        // Private field to hold the collection of shopping items
        private ObservableCollection<ShoppingItem> _shoppingItems;

        // Public property to expose the shopping items collection
        public ObservableCollection<ShoppingItem> ShoppingItems
        {
            get => _shoppingItems; // Getter to return the current collection
            set => SetProperty(ref _shoppingItems, value); // Setter to update the collection and notify changes
        }

        // Private field to hold the name of the new shopping item
        private string _newItemName = string.Empty;
        public string NewItemName
        {
            get => _newItemName; // Getter to return the current new item name
            set
            {
                // Check if the new value is different from the current value
                if (_newItemName != value)
                {
                    _newItemName = value; // Update the new item name
                    OnPropertyChanged(); // Notify that the property has changed
                }
            }
        }

        // Private field to hold the quantity of the new shopping item
        private int _newItemQuantity = 1;
        public int NewItemQuantity
        {
            get => _newItemQuantity; // Getter to return the current new item quantity
            set
            {
                // Check if the new value is different from the current value
                if (_newItemQuantity != value)
                {
                    _newItemQuantity = value; // Update the new item quantity
                    OnPropertyChanged(); // Notify that the property has changed
                }
            }
        }

        // Command to load shopping items asynchronously
        [RelayCommand]
        private async Task LoadShoppingItems()
        {
            // Fetching all shopping items from the repository
            var items = await _repository.GetAllShoppingItems();
            // Simplified collection initialization
            ShoppingItems = new ObservableCollection<ShoppingItem>(items);
        }

        // Command to add a new shopping item asynchronously
        [RelayCommand]
        private async Task AddShoppingItem()
        {
            // Check if the new item name is not empty and quantity is greater than zero
            if (!string.IsNullOrWhiteSpace(NewItemName) && NewItemQuantity > 0)
            {
                // Creating a new shopping item with the specified name and quantity
                var newItem = new ShoppingItem
                {
                    Name = NewItemName,
                    Quantity = NewItemQuantity
                };
                // Adding the new item to the repository
                await _repository.AddShoppingItem(newItem);
                // Reloading the shopping items to reflect the changes
                await LoadShoppingItems();
                // Resetting the new item name and quantity for the next entry
                NewItemName = string.Empty;
                NewItemQuantity = 1;
            }
        }

        // Command to update an existing shopping item asynchronously
        [RelayCommand]
        private async Task UpdateShoppingItem(ShoppingItem item)
        {
            // Check if the item to update is not null
            if (item != null)
            {
                // Updating the item in the repository
                await _repository.UpdateShoppingItem(item);
                // Reloading the shopping items to reflect the changes
                await LoadShoppingItems();
            }
        }

        // Command to delete a shopping item asynchronously by its ID
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