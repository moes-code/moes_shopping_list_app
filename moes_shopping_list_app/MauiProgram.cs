// Importing necessary namespaces for the application
using moes_shopping_list_app.Repositories; // For repository classes
using moes_shopping_list_app.ViewModels; // For view model classes
using moes_shopping_list_app.Data; // For data-related classes
using Microsoft.Extensions.Logging; // For logging capabilities

namespace moes_shopping_list_app
{
    // Static class that contains the method to create the Maui application
    public static class MauiProgram
    {
        // Method to create and configure the Maui application
        public static MauiApp CreateMauiApp()
        {
            // Creating a new Maui application builder
            var builder = MauiApp.CreateBuilder();

            // Configuring the application with the main app class and registering services
            builder
                .UseMauiApp<App>() // Setting the main application class
                .Services.AddSingleton<ShoppingItemRepository>() // Registering ShoppingItemRepository as a singleton
                .AddTransient<ShoppingListViewModel>() // Registering ShoppingListViewModel as a transient service
                .AddTransient<EditItemViewModel>(); // Registering EditItemViewModel as a transient service

            // Configuring the ShoppingListDbContext as a singleton service with a specific database path
            builder.Services.AddSingleton<ShoppingListDbContext>(provider =>
            {
                // Combining the application data directory path with the database file name to create the full path
                string dbPath = Path.Combine(FileSystem.AppDataDirectory, "shopping_list.db");
                // Creating a new instance of ShoppingListDbContext initialized with the database path
                var dbContext = new ShoppingListDbContext(dbPath);
                // Call CreateTable to ensure the ShoppingItems table is created
                dbContext.CreateTable();
                // Return the initialized dbContext
                return dbContext;
            });

            // Configuring fonts for the application
            builder.ConfigureFonts(fonts =>
            {
                // Adding OpenSans-Regular font
                fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                // Adding OpenSans-Semibold font
                fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
            });

#if DEBUG
            // Adding debug logging in debug mode
            builder.Logging.AddDebug();
#endif
            // Building and returning the configured Maui application
            return builder.Build();
        }
    }
}