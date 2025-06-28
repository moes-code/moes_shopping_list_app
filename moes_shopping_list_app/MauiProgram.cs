using moes_shopping_list_app.Repositories;
using moes_shopping_list_app.ViewModels;
using moes_shopping_list_app.Data;
using Microsoft.Extensions.Logging;

namespace moes_shopping_list_app
{
    public static class MauiProgram
    {
        public static MauiApp CreateMauiApp()
        {
            var builder = MauiApp.CreateBuilder();

            builder
                .UseMauiApp<App>()
                .Services.AddSingleton<ShoppingItemRepository>()
                .AddTransient<ShoppingListViewModel>();

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

            builder.ConfigureFonts(fonts =>
            {
                fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
            });

#if DEBUG
            builder.Logging.AddDebug();
#endif
            return builder.Build();
        }
    }
}