// Importing necessary namespaces for models and view models in the application
using moes_shopping_list_app.Models;
using moes_shopping_list_app.ViewModels;

namespace moes_shopping_list_app.Views;

// Attribute to define a query property for navigation, linking the ItemId property to the query parameter
[QueryProperty(nameof(ItemId), nameof(ItemId))]
public partial class EditItemPage : ContentPage
{
    // Private field to hold the reference to the ShoppingListViewModel
    private readonly ShoppingListViewModel _shoppingListViewModel;

    // Public property to hold the ID of the item being edited
    public int ItemId { get; set; }

    // Constructor for the EditItemPage class that takes a ShoppingListViewModel as a parameter
    public EditItemPage(ShoppingListViewModel shoppingListViewModel)
    {
        // Initializes the components of the page (UI elements defined in XAML)
        InitializeComponent();
        // Assigns the provided view model to the private field
        _shoppingListViewModel = shoppingListViewModel;
    }

    // Override method that is called when the page appears
    protected override async void OnAppearing()
    {
        // Calls the base class implementation of OnAppearing
        base.OnAppearing();
        // Checks if the ItemId is not zero (indicating a valid item ID)
        if (ItemId is not 0)
        {
            // Retrieves the item from the view model using the ItemId
            var item = await _shoppingListViewModel.GetItemById(ItemId);
            // Checks if the item was found
            if (item is not null)
            {
                // Sets the BindingContext to a new EditItemViewModel initialized with the found item and the view model
                BindingContext = new EditItemViewModel(item, _shoppingListViewModel);
            }
            else
            {
                // Throws an exception if the item with the specified ID was not found
                throw new InvalidOperationException($"Item with ID {ItemId} not found.");
            }
        }
    }
}