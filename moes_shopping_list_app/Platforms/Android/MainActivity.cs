// Importing necessary namespaces for Android application development
using Android.App;
using Android.Content.PM;
using Android.OS;
using Android.Views;

namespace moes_shopping_list_app
{
    // Define the MainActivity class, which is the entry point of the application
    // The Activity attribute specifies the theme, main launcher, launch mode, and configuration changes to handle
    [Activity(Theme = "@style/Maui.SplashTheme", MainLauncher = true, LaunchMode = LaunchMode.SingleTop,
              ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation |
                                   ConfigChanges.UiMode | ConfigChanges.ScreenLayout |
                                   ConfigChanges.SmallestScreenSize | ConfigChanges.Density)]
    public class MainActivity : MauiAppCompatActivity
    {
        // Override the OnCreate method, which is called when the activity is first created
        protected override void OnCreate(Bundle? savedInstanceState)
        {
            // Call the base class implementation of OnCreate
            base.OnCreate(savedInstanceState);

            // Check if the Window object is not null
            if (Window != null)
            {
                // Set the status bar color to black
                Window.SetStatusBarColor(Android.Graphics.Color.Black);
                // Set the navigation bar color to black
                Window.SetNavigationBarColor(Android.Graphics.Color.Black);
            }
        }
    }
}