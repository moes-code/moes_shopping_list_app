// Importing the necessary namespace for view models in the application
using moes_shopping_list_app.ViewModels;

namespace moes_shopping_list_app.Views
{
    // Partial class definition for the MainPage, inheriting from ContentPage
    public partial class MainPage : ContentPage
    {
        // Constructor for the MainPage class that takes a ShoppingListViewModel as a parameter
        public MainPage(ShoppingListViewModel viewModel)
        {
            // Initializes the components of the page (UI elements defined in XAML)
            InitializeComponent();

            // Sets the BindingContext of the page to the provided view model
            BindingContext = viewModel;
        }

        // Override method that is called when the page appears
        protected override async void OnAppearing()
        {
            // Calls the base class implementation of OnAppearing
            base.OnAppearing();
            // Checks if the BindingContext is of type ShoppingListViewModel
            if (BindingContext is ShoppingListViewModel viewModel)
            {
                // Executes the LoadShoppingItemsCommand to load shopping items asynchronously
                await viewModel.LoadShoppingItemsCommand.ExecuteAsync(null);
            }
        }
    }
}