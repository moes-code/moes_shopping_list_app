// Importing the ShoppingItemRepository class from the Repositories namespace
using moes_shopping_list_app.Repositories;
// Importing the ShoppingListViewModel class from the ViewModels namespace
using moes_shopping_list_app.ViewModels;
// Importing the logging extensions from Microsoft.Extensions.Logging
using Microsoft.Extensions.Logging;

// Declaring the namespace for the application
namespace moes_shopping_list_app
{
    // Defining a static class named MauiProgram
    public static class MauiProgram
    {
        // Method to create and configure a MauiApp instance
        public static MauiApp CreateMauiApp()
        {
            // Creating a new MauiAppBuilder instance
            var builder = MauiApp.CreateBuilder();

            // Configuring the Maui application with the main App class and registering services
            builder
                .UseMauiApp<App>() // Specifies the main application class
                .Services.AddSingleton<ShoppingItemRepository>() // Registers ShoppingItemRepository as a singleton service
                .AddTransient<ShoppingListViewModel>(); // Registers ShoppingListViewModel as a transient service

            // Configuring fonts for the application
            builder.ConfigureFonts(fonts =>
            {
                // Adding the OpenSans-Regular font with the alias "OpenSansRegular"
                fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                // Adding the OpenSans-Semibold font with the alias "OpenSansSemibold"
                fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
            });

            // Conditional compilation directive for debug mode
#if DEBUG
            // Adding debug logging if the application is in debug mode
            builder.Logging.AddDebug();
#endif

            // Building and returning the configured MauiApp instance
            return builder.Build();
        }
    }
}