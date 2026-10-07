import SwiftUI
import UIKit

final class LifecycleViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        Swift.print("[UIKit] viewDidLoad")
        view.backgroundColor = .systemBackground

        let label = UILabel()
        label.text = "UIKit screen"
        label.font = .preferredFont(forTextStyle: .title1)
        label.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(label)
        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            label.centerYAnchor.constraint(equalTo: view.centerYAnchor),
        ])
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        Swift.print("[UIKit] viewWillAppear")
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        Swift.print("[UIKit] viewDidAppear")
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        Swift.print("[UIKit] viewWillDisappear")
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        Swift.print("[UIKit] viewDidDisappear")
    }

    deinit {
        Swift.print("[UIKit] deinit")
    }
}

struct LifecycleScreen: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> LifecycleViewController {
        LifecycleViewController()
    }

    func updateUIViewController(_ uiViewController: LifecycleViewController, context: Context) {}
}