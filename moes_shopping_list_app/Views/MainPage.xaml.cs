// Importing the ShoppingListViewModel class from the ViewModels namespace
using moes_shopping_list_app.ViewModels;

// Declaring the namespace for the Views in the application
namespace moes_shopping_list_app.Views
{
    // Defining a partial class named MainPage that inherits from ContentPage
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
    }
}