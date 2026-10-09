import SwiftUI
import UIKit

final class ReactViewController: UIViewController {
    private let moduleName: String
    private let initialProperties: [String: Any]?

    init(moduleName: String, initialProperties: [String: Any]? = nil) {
        self.moduleName = moduleName
        self.initialProperties = initialProperties
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func loadView() {
        view = ReactNativeRuntime.shared.view(
            moduleName: moduleName,
            initialProperties: initialProperties
        )
    }

    deinit {
        Swift.print("[ReactVC] deinit")
    }
}

struct ReactScreen: UIViewControllerRepresentable {
    let moduleName: String
    var initialProperties: [String: Any]? = nil

    func makeUIViewController(context: Context) -> ReactViewController {
        ReactViewController(moduleName: moduleName, initialProperties: initialProperties)
    }

    func updateUIViewController(_ uiViewController: ReactViewController, context: Context) {}
}

struct ReactView: UIViewRepresentable {
    let moduleName: String
    var initialProperties: [String: Any]? = nil

    func makeUIView(context: Context) -> UIView {
        ReactNativeRuntime.shared.view(moduleName: moduleName, initialProperties: initialProperties)
    }

    func updateUIView(_ uiView: UIView, context: Context) {}
}