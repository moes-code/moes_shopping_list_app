// Importing necessary namespaces for collections, MVVM components, models, repositories, and views
using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using moes_shopping_list_app.Models;
using moes_shopping_list_app.Repositories;
using moes_shopping_list_app.Views;

namespace moes_shopping_list_app.ViewModels
{
    // ViewModel for managing the shopping list, inheriting from ObservableObject for property change notifications
    public partial class ShoppingListViewModel : ObservableObject
    {
        // Private field to hold the repository instance for data operations
        private readonly ShoppingItemRepository _repository;

        // Observable property for the collection of shopping items
        [ObservableProperty]
        private ObservableCollection<ShoppingItem> _shoppingItems = new ObservableCollection<ShoppingItem>();

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

        // Method to retrieve a shopping item by its ID
        public async Task<ShoppingItem?> GetItemById(int itemId)
        {
            return await _repository.GetItemById(itemId); // Fetching the item from the repository
        }

        // Command to load shopping items asynchronously
        [RelayCommand]
        private async Task LoadShoppingItems()
        {
            // Fetching all shopping items from the repository
            var items = await _repository.GetAllShoppingItems();
            ShoppingItems = new ObservableCollection<ShoppingItem>(items); // Assigning the fetched items to the observable collection
        }

        // Command to add a new shopping item asynchronously
        [RelayCommand]
        private async Task AddShoppingItem()
        {
            // Validating the new item name and quantity
            if (string.IsNullOrWhiteSpace(NewItemName) || NewItemQuantity <= 0)
            {
                // Get the current page from the application context
                var currentPage = Application.Current?.Windows.FirstOrDefault()?.Page;
                // If the current page is not null, display an alert to the user
                if (currentPage != null)
                {
                    await currentPage.DisplayAlert("Error", "Item name cannot be empty and quantity must be greater than zero.", "OK");
                }
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

        // Command to edit an existing shopping item asynchronously
        [RelayCommand]
        public async Task EditShoppingItem(ShoppingItem item)
        {
            // Checking if the item is null
            if (item is not null)
            {
                var editItemViewModel = new EditItemViewModel(item, this); // Creating an instance of the EditItemViewModel
                // Navigating to the EditItemPage with the item's ID as a parameter
                await Shell.Current.GoToAsync($"{nameof(EditItemPage)}?ItemId={item.Id}");
            }
        }

        // Command to update an existing shopping item asynchronously
        [RelayCommand]
        public async Task UpdateShoppingItem(ShoppingItem item)
        {
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