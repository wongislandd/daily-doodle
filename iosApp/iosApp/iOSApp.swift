import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        AppModuleKt.initializeKoin(context: nil)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}