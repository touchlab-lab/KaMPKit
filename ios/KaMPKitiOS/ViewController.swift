//
//  ViewController.swift
//  KaMPKitiOS
//
//  Created by Kevin Schildhorn on 12/18/19.
//  Copyright © 2019 Touchlab. All rights reserved.
//

import Combine
import KMPNativeCoroutinesCombine
import UIKit
import shared

class BreedsViewController: UIViewController {

    @IBOutlet weak var breedTableView: UITableView!
    var data: [Breed] = []
        
    let log = koin.get(objCClass: Kermit.self, parameter: "ViewController") as! Kermit
    private let refreshControl = UIRefreshControl()

    lazy var adapter: NativeViewModel = NativeViewModel(
        onLoading: { /* Loading spinner is shown automatically on iOS */},
        onSuccess: { _ in },
        onError: { _ in },
        onEmpty: { /* Show "No doggos found!" message */        }
    )
    
    var cancellable: AnyCancellable?
    
    // MARK: View Lifecycle

    @objc
    func getBreedsForced() {
        adapter.refreshBreeds(forced: true)
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        breedTableView.dataSource = self
        // Add Refresh Control to Table View
        breedTableView.refreshControl = refreshControl
        // Configure Refresh Control
        refreshControl.addTarget(self, action: #selector(self.getBreedsForced), for: .valueChanged)
        refreshControl.beginRefreshing()
        
        
        cancellable = createPublisher(for: adapter.breedsNativeFlow)
            .receive(on: DispatchQueue.main)
            .sink(
                receiveCompletion: { completion in
                    print("Received Completion \(completion)")
                },
                receiveValue: { [weak self] value in
                    let dataState: DataStateNative<ItemDataSummary> = toDataStateNative(value)
                    switch dataState {
                    case .Success(let data):
                        self?.refreshControl.endRefreshing()
                        self?.data = data.allItems
                        self?.viewUpdateSuccess(for: data)
                    case .Error(let error):
                        self?.refreshControl.endRefreshing()
                        self?.errorUpdate(for: error)
                    case .Empty:
                        self?.refreshControl.endRefreshing()
                    case .Loading:
                        guard let self = self else { return }
                        if (!(self.refreshControl.isRefreshing)) {
                            self.refreshControl.beginRefreshing()
                        }
                    }
                }
            )
        adapter.refreshBreeds(forced: false)

    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        adapter.onDestroy()
    }
    
    // MARK: BreedModel Closures
    
    private func viewUpdateSuccess(for summary: ItemDataSummary) {
        log.d(withMessage: {"View updating with \(summary.allItems.count) breeds"})
        data = summary.allItems
        breedTableView.reloadData()
    }
    
    private func errorUpdate(for errorMessage: String) {
        log.e(withMessage: {"Displaying error: \(errorMessage)"})
        let alertController = UIAlertController(title: "error", message: errorMessage, preferredStyle: .actionSheet)
        alertController.addAction(UIAlertAction(title: "Dismiss", style: .default))
        present(alertController, animated: true, completion: nil)
    }
    
}

// MARK: - UITableViewDataSource
extension BreedsViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BreedCell", for: indexPath)
        if let breedCell = cell as? BreedCell {
            let breed = data[indexPath.row]
            breedCell.bind(breed)
            breedCell.delegate = self
        }
        return cell
    }
}

// MARK: - BreedCellDelegate
extension BreedsViewController: BreedCellDelegate {
    func toggleFavorite(_ breed: Breed) {
        adapter.updateBreedFavorite(breed: breed)
    }
}

enum DataStateNative<T> {
    case Success(_ data: T)
    case Error(_ error: String)
    case Empty
    case Loading
}

class ObservableDataState: ObservableObject {
    @Published var dataStateNative: DataStateNative<ItemDataSummary>
    
    init(dataStateNative: DataStateNative<ItemDataSummary>) {
        self.dataStateNative = dataStateNative
    }
}

func toDataStateNative<T>(_ dataState: DataState<T>) -> DataStateNative<T> {
    switch dataState {
    case let success as DataStateSuccess<T>:
        return DataStateNative.Success(success.data!)
    case let error as DataStateError:
        return DataStateNative.Error(error.exception)
    case is DataStateEmpty:
        return DataStateNative.Empty
    case is DataStateLoading:
        return DataStateNative.Loading
    default:
        return DataStateNative.Empty
    }
    
}
