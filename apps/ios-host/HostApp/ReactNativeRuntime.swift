import React
import React_RCTAppDelegate
import ReactAppDependencyProvider
import UIKit

final class ReactNativeRuntime {
    static let shared = ReactNativeRuntime()

    private let delegate: ReactNativeDelegate
    private let factory: RCTReactNativeFactory

    private init() {
        delegate = ReactNativeDelegate()
        delegate.dependencyProvider = RCTAppDependencyProvider()
        factory = RCTReactNativeFactory(delegate: delegate)
    }

    func view(moduleName: String, initialProperties: [String: Any]? = nil) -> UIView {
        factory.rootViewFactory.view(withModuleName: moduleName, initialProperties: initialProperties)
    }
}

final class ReactNativeDelegate: RCTDefaultReactNativeFactoryDelegate {
    override func sourceURL(for bridge: RCTBridge) -> URL? {
        bundleURL()
    }

    override func bundleURL() -> URL? {
        #if DEBUG
        RCTBundleURLProvider.sharedSettings().jsBundleURL(forBundleRoot: "index")
        #else
        Bundle.main.url(forResource: "main", withExtension: "jsbundle")
        #endif
    }
}