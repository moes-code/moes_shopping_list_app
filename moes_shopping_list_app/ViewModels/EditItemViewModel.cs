// Importing necessary namespaces for MVVM components and models
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using moes_shopping_list_app.Models;

namespace moes_shopping_list_app.ViewModels
{
    // ViewModel for editing a shopping item, inheriting from ObservableObject for property change notifications
    public partial class EditItemViewModel : ObservableObject
    {
        // Private field to hold a reference to the ShoppingListViewModel
        private readonly ShoppingListViewModel _shoppingListViewModel;

        // Observable property to store the shopping item being edited
        [ObservableProperty]
        private ShoppingItem _item;

        // Constructor to initialize the EditItemViewModel with the item to edit and the shopping list view model
        public EditItemViewModel(ShoppingItem item, ShoppingListViewModel shoppingListViewModel)
        {
            _item = item; // Set the item to be edited
            _shoppingListViewModel = shoppingListViewModel; // Store the reference to the shopping list view model
        }

        // Asynchronous method to load an item by its ID
        private async Task LoadItem(int itemId)
        {
            // Retrieve the item from the shopping list view model
            var item = await _shoppingListViewModel.GetItemById(itemId);
            // Check if the item was found
            if (item is not null)
            {
                Item = item; // Set the observable property to the found item
            }
            else
            {
                // Throw an exception if the item was not found
                throw new InvalidOperationException($"Item with ID {itemId} not found.");
            }
        }

        // Command to save the edited item
        [RelayCommand]
        private async Task SaveItem()
        {
            // Update the shopping item in the shopping list view model
            await _shoppingListViewModel.UpdateShoppingItem(Item);
            // Navigate back to the previous page
            await Shell.Current.GoToAsync("..");
        }

        // Command to cancel the editing and navigate back
        [RelayCommand]
        private async Task Cancel()
        {
            // Navigate back to the previous page without saving
            await Shell.Current.GoToAsync("..");
        }
    }
}