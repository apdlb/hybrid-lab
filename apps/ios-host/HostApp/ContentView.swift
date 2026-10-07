import SwiftUI
import Playgrounds

struct ContentView: View {
    @State private var count = 0

    var body: some View {
        let _ = print("[Home] body")
        NavigationStack {
            VStack(spacing: 16) {
                Text("Count: \(count)")
                    .font(.title)
                Button("Inmcrement") { count += 1 }
                    .buttonStyle(.borderedProminent)
                NavigationLink("Open detail") {
                    DetailView(count: count)
                }
                NavigationLink("Open UIKit screen") {
                    LifecycleScreen()
                }
                NavigationLink("Open RN screen") {
                    ReactScreen(
                        moduleName: "HelloScreen",
                        initialProperties: ["source": "SwiftUI Home (count: \(count))"]
                    )
                    .ignoresSafeArea()
                }
            }
            .navigationTitle("Home")
            .onAppear { print("[Home] onAppear") }
            .onDisappear { print("[Home] onDisappear") }
        }
        Text("Hello, world!")
            .padding()
    }
}

struct DetailView: View {
    let count: Int

    var body: some View {
         Text("Count received: \(count)")
            .navigationTitle("Detail")
            .onAppear { print("[Detail] onAppear") }
            .onDisappear { print("[Detail] onDisappear") }
    }
}

#Preview {
    ContentView()
}

#Playground {
    _ = 1 + 2
}
