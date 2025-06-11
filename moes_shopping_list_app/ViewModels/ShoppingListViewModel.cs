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
        }

        // Private field to hold the collection of shopping items
        private ObservableCollection<ShoppingItem> _shoppingItems;

        // Public property to expose the shopping items collection
        public ObservableCollection<ShoppingItem> ShoppingItems
        {
            get => _shoppingItems; // Getter to return the current collection
            set => SetProperty(ref _shoppingItems, value); // Setter to update the collection and notify changes
        }

        // Command to load shopping items asynchronously
        [RelayCommand]
        private async Task LoadShoppingItems()
        {
            // Fetching all shopping items from the repository
            var items = await _repository.GetAllShoppingItems();
            // Updating the ShoppingItems property with the fetched items
            ShoppingItems = new ObservableCollection<ShoppingItem>(items);
        }

        // Command to add a new shopping item asynchronously
        [RelayCommand]
        private async Task AddShoppingItem(ShoppingItem item)
        {
            // Adding the new item to the repository
            await _repository.AddShoppingItem(item);
            // Reloading the shopping items to reflect the changes
            await LoadShoppingItems();
        }

        // Command to update an existing shopping item asynchronously
        [RelayCommand]
        private async Task UpdateShoppingItem(ShoppingItem item)
        {
            // Updating the item in the repository
            await _repository.UpdateShoppingItem(item);
            // Reloading the shopping items to reflect the changes
            await LoadShoppingItems();
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