import SwiftUI

@main struct MyApp: App {
    @Environment(\.scenePhase) private var scenePhase

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
        .onChange(of: scenePhase) { oldPhase, newPhase in
            print("[App] scenePhase: \(oldPhase) -> \(newPhase)")
        }
    }
}
