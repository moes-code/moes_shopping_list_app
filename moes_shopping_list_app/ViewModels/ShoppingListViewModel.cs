using System.Collections.ObjectModel;
using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using moes_shopping_list_app.Models;
using moes_shopping_list_app.Repositories;

namespace moes_shopping_list_app.ViewModels
{
    public partial class ShoppingListViewModel : ObservableObject
    {
        private readonly ShoppingItemRepository _repository;

        // Wenn du das CommunityToolkit verwendest geht das auch für Properties.
        // Gem. Concention werden private Felder mit einem Unterstrich "_" am Anfang benannt.
        // Der Code-Generator von CommunityToolkit erstellt automatisch die Property für dich.
        [ObservableProperty]
        private ObservableCollection<ShoppingItem> _shoppingItems = [];

        [ObservableProperty]
        private string _newItemName = string.Empty;

        [ObservableProperty]
        private int _newItemQuantity = 1;

        public ShoppingListViewModel(ShoppingItemRepository repository)
        {
            _repository = repository; 
            LoadShoppingItemsCommand.Execute(null); 
        }



        [RelayCommand]
        private async Task LoadShoppingItems()
        {
            var items = await _repository.GetAllShoppingItems();


            // Simplified collection initialization
            ShoppingItems = [.. items];
        }

        [RelayCommand]
        private async Task AddShoppingItem()
        {
            if (string.IsNullOrWhiteSpace(NewItemName) || NewItemQuantity <= 0)
            {
                // Hier könntest du eine Fehlermeldung anzeigen, wenn die Eingaben ungültig sind
                return;
            }


            var newItem = new ShoppingItem
            {
                Name = NewItemName,
                Quantity = NewItemQuantity
            };

            await _repository.AddShoppingItem(newItem);

            await LoadShoppingItems();

            NewItemName = string.Empty;
            NewItemQuantity = 1;
        }

        [RelayCommand]
        private async Task UpdateShoppingItem(ShoppingItem item)
        {
            // Arbeite lieber mit Guard Clauses, um sicherzustellen, dass die Eingaben gültig sind
            // Als neues C# feature kann auch "is" oder "is not" verwendet werden, um den Typ zu prüfen

            if (item is null)
                return;

            await _repository.UpdateShoppingItem(item);
            await LoadShoppingItems();
        }

        [RelayCommand]
        private async Task DeleteShoppingItem(int id)
        {
            await _repository.DeleteShoppingItem(id);
            await LoadShoppingItems();
        }
    }
}
