// Importing the necessary namespace for the views in the application
using moes_shopping_list_app.Views;

namespace moes_shopping_list_app
{
    // Partial class definition for AppShell, inheriting from Shell
    public partial class AppShell : Shell
    {
        // Constructor for the AppShell class
        public AppShell()
        {
            // Initializes the components defined in the associated XAML file
            InitializeComponent();

            // Registering a route for navigation to the EditItemPage
            Routing.RegisterRoute(nameof(EditItemPage), typeof(EditItemPage));
        }
    }
}