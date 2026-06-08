# Moe's Shopping List App

<a href="https://play.google.com/store/apps/details?id=com.moes_code.moes_shopping_list_app">
  <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" height="60">
</a>

A privacy-first, offline shopping list app for Android. Organize purchases with custom categories. Zero data collection, no account required.

## Features

- Custom categories with expand/collapse
- Add items with name and quantity
- Swipe right to edit, swipe left to delete
- Check off completed items
- **Export/Import backup** — JSON file via system file picker
- Dark theme
- 100% offline

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM (ViewModel + Repository)
- **Database:** Room (SQLite) with Flow-based reactive queries
- **Async:** Kotlin Coroutines + Flow
- **JSON:** Gson
- **Build:** Gradle with Kotlin DSL and version catalogs

## Architecture

```
ShoppingListScreen (Compose UI)
      ↓ observes StateFlow
ShoppingViewModel (AndroidViewModel)
      ↓ calls suspend functions
ShoppingRepository
      ↓ DAO interfaces
Room Database (SQLite)
```

## Project Structure

```
app/src/main/java/com/moes_code/moes_shopping_list_app/
├── database/          Room DB, DAOs, migrations
│   └── dao/          CategoryDao, ShoppingItemDao
├── model/            Category, ShoppingItem entities
├── repository/       ShoppingRepository + BackupData
├── view/
│   ├── components/   CategoryCard, ShoppingItemRow, SwipeableItem, dialogs/
│   ├── screens/      ShoppingListScreen
│   ├── theme/        Colors, Typography, Shapes, Dimensions
│   └── MainActivity.kt
└── viewmodel/        ShoppingViewModel
```

## Database Schema

### `categories`

| Column | Type | Notes |
|---|---|---|
| id | INTEGER | PK, auto-increment |
| name | TEXT | NOT NULL, unique index |
| isExpanded | BOOLEAN | DEFAULT 1 |

### `shopping_items`

| Column | Type | Notes |
|---|---|---|
| id | INTEGER | PK, auto-increment |
| name | TEXT | NOT NULL |
| quantity | INTEGER | NOT NULL |
| category_id | INTEGER | FK → categories(id) ON DELETE CASCADE |
| is_completed | BOOLEAN | DEFAULT 0 |

## Build Instructions

```bash
./gradlew assembleDebug
```

Open in Android Studio and run on a device or emulator (min SDK 29).

## Backup Format

Export produces a JSON file with this structure:

```json
{
  "version": 1,
  "categories": [
    { "id": 1, "name": "Beverages", "isExpanded": true }
  ],
  "shoppingItems": [
    { "id": 1, "name": "Water", "quantity": 6, "categoryId": 1, "isCompleted": false }
  ]
}
```

Import is available from the overflow menu (three dots) in the top bar. **Warning:** importing replaces all current data.

## License

MIT License — see [LICENSE](LICENSE).
