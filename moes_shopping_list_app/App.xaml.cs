namespace moes_shopping_list_app
{
    // Partial class definition for the main application, inheriting from Application
    public partial class App : Application
    {
        // Constructor for the App class
        public App()
        {
            // Initializes the components defined in the associated XAML file
            InitializeComponent();
        }

        // Method to create the main application window
        protected override Window CreateWindow(IActivationState? activationState)
        {
            // Returning a new Window instance with the AppShell as its content
            return new Window(new AppShell());
        }
    }
}