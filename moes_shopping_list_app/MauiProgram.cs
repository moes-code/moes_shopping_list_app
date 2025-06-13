// Importing the ShoppingItemRepository class from the Repositories namespace
using moes_shopping_list_app.Repositories;
// Importing the ShoppingListViewModel class from the ViewModels namespace
using moes_shopping_list_app.ViewModels;
// Importing the Data namespace, which likely contains data-related classes
using moes_shopping_list_app.Data;
// Importing the logging extensions from Microsoft.Extensions.Logging for logging capabilities
using Microsoft.Extensions.Logging;

// Declaring the namespace for the application, which helps organize code and avoid naming conflicts
namespace moes_shopping_list_app
{
    // Defining a static class named MauiProgram, which will contain methods related to the Maui application setup
    public static class MauiProgram
    {
        // Method to create and configure a MauiApp instance, which is the main application class for a .NET MAUI app
        public static MauiApp CreateMauiApp()
        {
            // Creating a new MauiAppBuilder instance, which is used to configure the application
            var builder = MauiApp.CreateBuilder();

            // Configuring the Maui application with the main App class and registering services
            builder
                .UseMauiApp<App>() // Specifies the main application class to be used
                .Services.AddSingleton<ShoppingItemRepository>() // Registers ShoppingItemRepository as a singleton service, meaning one instance will be used throughout the app
                .AddTransient<ShoppingListViewModel>(); // Registers ShoppingListViewModel as a transient service, meaning a new instance will be created each time it is requested

            // Configuring the ShoppingListDbContext as a singleton service with a specific database path
            builder.Services.AddSingleton<ShoppingListDbContext>(provider =>
            {
                // Combining the application data directory path with the database file name to create the full path
                string dbPath = Path.Combine(FileSystem.AppDataDirectory, "shoppinglist.db");
                // Returning a new instance of ShoppingListDbContext initialized with the database path
                return new ShoppingListDbContext(dbPath);
            });

            // Configuring fonts for the application to enhance UI appearance
            builder.ConfigureFonts(fonts =>
            {
                // Adding the OpenSans-Regular font with the alias "OpenSansRegular" for use in the application
                fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                // Adding the OpenSans-Semibold font with the alias "OpenSansSemibold" for use in the application
                fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
            });

            // Conditional compilation directive for debug mode, allowing for debug-specific code to be included
#if DEBUG
            // Adding debug logging if the application is in debug mode, which helps in troubleshooting during development
            builder.Logging.AddDebug();
#endif

            // Building and returning the configured MauiApp instance, finalizing the setup process
            return builder.Build();
        }
    }
}